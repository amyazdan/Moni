package com.app.moni.sms

import android.content.Context
import android.util.Log
import com.app.moni.data.db.AppDatabase
import com.app.moni.data.model.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * کلاس SmsParser مسئول استخراج اطلاعات از متن پیامک‌های بانکی است.
 */
class SmsParser(private val context: Context) {

    private val db = AppDatabase.getDatabase(context)

    // Regex برای الگوهای مختلف پیامک بانکی
    private val regexPatterns = mapOf(
        "بانک ملی" to mapOf(
            "amount_with_sign" to """(انتقال|انتقالي|خريداينترنتي)[:\s]([\d,]+)([+-])""",
            "balance" to """مانده[:\s]([\d,]+)""",
            "account" to """حساب[:\s]([\d,]+)"""
        ),
        "رسالت" to mapOf(
            "amount" to """(-[\d,]+)""",
            "balance" to """مانده[:\s]([\d,]+)"""
        ),
        "بلو" to mapOf(
            "amount" to """([\d,]+) ریال""",
            "balance" to """موجودی: ([\d,]+) ریال""",
            "withdrawal" to """برداشت پول|انتقال پل""",
            "deposit" to """واریز پول|سود شما"""
        )
    )

    /**
     * پیامک را پردازش کرده و تراکنش را در پایگاه داده ذخیره می‌کند.
     */
    fun parseAndSaveTransaction(sender: String, message: String) {
        if (message.length < 1) {
            Log.d("Moni", "Skipping short SMS message.")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val bankName = getBankNameBySender(sender)
            if (bankName == null) {
                Log.d("Moni", "SMS sender not recognized as a known bank.")
                return@launch
            }
            val patterns = regexPatterns[bankName]
            if (patterns == null) {
                Log.d("Moni", "Regex patterns not found for bank: $bankName")
                return@launch
            }
            val amountWithSignMatch = patterns["amount_with_sign"]?.toRegex()?.find(message)
            val balanceMatch = patterns["balance"]?.toRegex()?.find(message)
            val accountMatch = patterns["account"]?.toRegex()?.find(message)
            val sign = amountWithSignMatch?.groupValues?.get(3)
            val isWithdrawal = sign == "-"
            val isDeposit = sign == "+"
            if (amountWithSignMatch != null) {
                val amountStr = amountWithSignMatch.groupValues[2].replace(",", "")
                val amount = amountStr.toDoubleOrNull() ?: 0.0
                val balanceStr = balanceMatch?.groupValues?.get(1)?.replace(",", "")
                val balance = balanceStr?.toDoubleOrNull() ?: 0.0
                val transactionType = if (isWithdrawal) "outcome" else "income"
                val accountNumber = accountMatch?.groupValues?.get(1) ?: "Unknown"

                val newTransaction = TransactionEntity(
                    transactionType = transactionType,
                    rawSmsText = message,
                    note = null,
                    dateTime = System.currentTimeMillis(),
                    createdAt = System.currentTimeMillis(),
                    category = listOf("uncategorized"),
                    bankName = bankName,
                    balanceAfter = balance,
                    amount = amount,
                    accountNumber = accountNumber
                )
                val transactionId = db.transactionDao().insertTransaction(newTransaction)
                Log.d("Moni", "Transaction saved successfully. ID: $transactionId")

            } else {
                Log.d("Moni", "SMS message not recognized as a bank transaction for bank: $bankName")
            }
        }
    }

    private suspend fun getBankNameBySender(sender: String): String? {
        return db.bankDao().getBankNameBySender(sender)
    }
}

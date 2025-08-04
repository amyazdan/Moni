package com.app.moni.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

/**
 * BroadcastReceiver برای دریافت پیامک‌ها.
 */
class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (message in messages) {
                val sender = message.originatingAddress ?: "Unknown"
                val messageBody = message.messageBody

                Log.d("Moni", "SMS received from: $sender")
                Log.d("Moni", "Message: $messageBody")

                // ایجاد نمونه‌ای از SmsParser و ارسال پیامک برای پردازش
                val smsParser = SmsParser(context)
                smsParser.parseAndSaveTransaction(sender, messageBody)
            }
        }
    }
}

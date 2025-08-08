package com.app.moni.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.moni.data.model.TransactionEntity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailsScreen(transaction: TransactionEntity, onBackClick: () -> Unit) {

    fun formatNumber(number: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.Builder().setLanguage("fa").setRegion("IR").build())
        return formatter.format(number.toInt())
    }

    fun formatDate(timestamp: Long): String {
        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.Builder().setLanguage("fa").setRegion("IR").build())
        return formatter.format(Date(timestamp))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("جزئیات تراکنش", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "${if (transaction.transactionType == "income") "+" else "-"} ${formatNumber(transaction.amount)} تومان",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = if (transaction.transactionType == "income") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))

            DetailRow(label = "نوع تراکنش", value = if (transaction.transactionType == "income") "درآمد" else "هزینه")
            DetailRow(label = "تاریخ و زمان", value = formatDate(transaction.dateTime))
            DetailRow(label = "بانک", value = transaction.bankName ?: "نامشخص")
            DetailRow(label = "شماره حساب", value = transaction.accountNumber ?: "نامشخص")
            DetailRow(label = "موجودی پس از تراکنش", value = "${formatNumber(transaction.balanceAfter)} تومان")
            DetailRow(label = "دسته‌بندی", value = transaction.category.joinToString())
            DetailRow(label = "یادداشت", value = transaction.note ?: "ندارد")

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "متن خام پیامک:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = transaction.rawSmsText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
    }
}

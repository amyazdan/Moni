package com.app.moni.ui.uncategorized

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.moni.data.model.TransactionEntity
import com.app.moni.ui.transactions.TransactionsViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UncategorizedScreen(viewModel: TransactionsViewModel, onCategorizeClick: (Long) -> Unit) {
    val transactions by viewModel.transactions.collectAsState()

    // فیلتر کردن تراکنش‌های بدون دسته‌بندی
    val uncategorizedTransactions = transactions.filter { it.category.contains("uncategorized") }

    fun formatNumber(number: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.Builder().setLanguage("fa").setRegion("IR").build())
        return formatter.format(number.toInt())
    }

    fun formatDate(timestamp: Long): String {
        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.Builder().setLanguage("fa").setRegion("IR").build())
        return formatter.format(Date(timestamp))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "تراکنش‌های بدون دسته‌بندی",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uncategorizedTransactions.isEmpty()) {
            Text(
                text = "همه تراکنش‌ها دسته‌بندی شده‌اند.",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        } else {
            LazyColumn {
                items(uncategorizedTransactions) { transaction ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onCategorizeClick(transaction.id) }, // قابلیت کلیک برای دسته‌بندی
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = transaction.bankName ?: "نامشخص",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${if (transaction.transactionType == "income") "+" else "-"} ${formatNumber(transaction.amount)} تومان",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "تاریخ: ${formatDate(transaction.dateTime)}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

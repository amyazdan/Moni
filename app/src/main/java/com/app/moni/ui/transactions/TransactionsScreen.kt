package com.app.moni.ui.transactions

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.moni.data.model.TransactionEntity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel,
    onTransactionClick: (TransactionEntity) -> Unit,
    onShowUncategorizedClick: () -> Unit // پارامتر جدید برای نمایش تراکنش‌های دسته‌بندی نشده
) {
    val transactions by viewModel.transactions.collectAsState()
    var searchText by remember { mutableStateOf("") }

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
            text = "تراکنش‌ها",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("جستجو در تراکنش‌ها") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onShowUncategorizedClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "نمایش تراکنش‌های بدون دسته‌بندی")
        }

        LazyColumn {
            items(transactions) { transaction ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onTransactionClick(transaction) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (transaction.transactionType == "income") MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = transaction.bankName ?: "نامشخص",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (transaction.transactionType == "income") MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = "${if (transaction.transactionType == "income") "+" else "-"} ${formatNumber(transaction.amount)} تومان",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (transaction.transactionType == "income") MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onError
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "دسته‌بندی: ${transaction.category.joinToString()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (transaction.transactionType == "income") MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "تاریخ: ${formatDate(transaction.dateTime)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (transaction.transactionType == "income") MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

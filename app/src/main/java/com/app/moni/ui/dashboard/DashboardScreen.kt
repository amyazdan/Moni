package com.app.moni.ui.dashboard

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
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val expensesByCategory by viewModel.expensesByCategory.collectAsState()

    // تابع کمکی برای فرمت‌دهی اعداد
    fun formatNumber(number: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale("fa", "IR"))
        return formatter.format(number.toInt())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "داشبورد ماهانه",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "مرداد ۱۴۰۳",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card for expenses
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "مجموع هزینه‌ها", style = MaterialTheme.typography.bodySmall)
                    Text(text = formatNumber(totalExpenses), style = MaterialTheme.typography.titleLarge)
                    Text(text = "تومان", style = MaterialTheme.typography.bodySmall)
                }
            }

            // Card for income
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "مجموع درآمد", style = MaterialTheme.typography.bodySmall)
                    Text(text = formatNumber(totalIncome), style = MaterialTheme.typography.titleLarge)
                    Text(text = "تومان", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "هزینه‌ها بر اساس دسته‌بندی",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn {
            items(expensesByCategory.toList()) { (category, amount) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = category)
                    Text(text = formatNumber(amount))
                }
            }
        }
    }
}

package com.app.moni.ui.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.moni.ui.budget.BudgetViewModel
import com.app.moni.ui.budget.BudgetItem // Import BudgetItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBudgetScreen(viewModel: BudgetViewModel, onSave: () -> Unit) {
    var category by remember { mutableStateOf("") }
    var budgetAmount by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("افزودن بودجه جدید", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("دسته‌بندی") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = budgetAmount,
                onValueChange = { budgetAmount = it },
                label = { Text("مبلغ بودجه") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val newBudget = BudgetItem(
                        category = category,
                        budgetAmount = budgetAmount.toDoubleOrNull() ?: 0.0
                    )
                    viewModel.saveBudget(newBudget)
                    onSave()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = category.isNotBlank() && budgetAmount.isNotBlank() && budgetAmount.toDoubleOrNull() != null
            ) {
                Text("ذخیره")
            }
        }
    }
}

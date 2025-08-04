package com.app.moni.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.moni.data.dao.TransactionDao
import com.app.moni.data.model.TransactionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DashboardViewModel(private val transactionDao: TransactionDao) : ViewModel() {

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome

    private val _totalExpenses = MutableStateFlow(0.0)
    val totalExpenses: StateFlow<Double> = _totalExpenses

    private val _expensesByCategory = MutableStateFlow<Map<String, Double>>(emptyMap())
    val expensesByCategory: StateFlow<Map<String, Double>> = _expensesByCategory

    init {
        viewModelScope.launch {
            transactionDao.getAllTransactions().collect { transactions ->
                calculateFinancialData(transactions)
            }
        }
    }

    private fun calculateFinancialData(transactions: List<TransactionEntity>) {
        val income = transactions.filter { it.transactionType == "income" }.sumOf { it.amount }
        val expenses = transactions.filter { it.transactionType == "outcome" }.sumOf { it.amount }
        _totalIncome.value = income
        _totalExpenses.value = expenses

        val categoryMap = transactions
            .filter { it.transactionType == "outcome" }
            .flatMap { transaction ->
                // Flatmap the categories to create a single list of (category, amount) pairs
                transaction.category.map { category -> category to transaction.amount }
            }
            .groupBy { it.first } // Group by category string
            .mapValues { (_, value) -> value.sumOf { it.second } } // Sum amounts for each category

        _expensesByCategory.value = categoryMap
    }
}

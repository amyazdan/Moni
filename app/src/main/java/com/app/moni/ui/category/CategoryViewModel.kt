package com.app.moni.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.moni.data.dao.TransactionDao
import com.app.moni.data.model.TransactionEntity
import kotlinx.coroutines.launch

class CategoryViewModel(private val transactionDao: TransactionDao) : ViewModel() {

    // این لیست در آینده از دیتابیس خوانده می‌شود
    val allCategories = listOf("uncategorized", "خرید روزمره", "رستوران و کافه", "حمل و نقل", "سرگرمی", "قبوض", "حقوق")

    fun updateTransactionCategory(transactionId: Long, newCategories: List<String>) {
        viewModelScope.launch {
            val transaction = transactionDao.getTransactionById(transactionId)
            if (transaction != null) {
                val updatedTransaction = transaction.copy(category = newCategories)
                transactionDao.updateTransaction(updatedTransaction)
            }
        }
    }
}

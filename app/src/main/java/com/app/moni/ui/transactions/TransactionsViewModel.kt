package com.app.moni.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.moni.data.dao.TransactionDao
import com.app.moni.data.model.TransactionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionsViewModel(private val transactionDao: TransactionDao) : ViewModel() {

    private val _transactions = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactions: StateFlow<List<TransactionEntity>> = _transactions

    init {
        viewModelScope.launch {
            transactionDao.getAllTransactions().collect { transactionList ->
                _transactions.value = transactionList
            }
        }
    }
}

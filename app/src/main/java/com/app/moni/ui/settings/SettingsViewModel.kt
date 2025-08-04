package com.app.moni.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.moni.data.dao.BankDao
import com.app.moni.data.model.BankEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val bankDao: BankDao) : ViewModel() {

    private val _banks = MutableStateFlow<List<BankEntity>>(emptyList())
    val banks: StateFlow<List<BankEntity>> = _banks

    init {
        viewModelScope.launch {
            bankDao.getAllBanks().collect { bankList ->
                _banks.value = bankList
            }
        }
    }

    fun saveBank(bank: BankEntity) {
        viewModelScope.launch {
            bankDao.insertBank(bank)
        }
    }
}

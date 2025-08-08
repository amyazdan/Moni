package com.app.moni.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.moni.data.dao.BudgetDao
import com.app.moni.data.dao.TransactionDao
import com.app.moni.data.model.BudgetEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// کلاس BudgetItem برای نمایش بودجه در UI
data class BudgetItem(
    val id: Long = 0,
    val category: String,
    val budgetAmount: Double,
    val spentAmount: Double = 0.0 // مقدار خرج شده
)

class BudgetViewModel(private val transactionDao: TransactionDao, private val budgetDao: BudgetDao) : ViewModel() {

    private val _budgets = MutableStateFlow<List<BudgetItem>>(emptyList())
    val budgets: StateFlow<List<BudgetItem>> = _budgets

    init {
        viewModelScope.launch {
            // ترکیب داده‌های بودجه از پایگاه داده با هزینه‌های واقعی از تراکنش‌ها
            combine(
                budgetDao.getAllBudgets(),
                transactionDao.getAllTransactions()
            ) { budgetEntities, transactions ->
                budgetEntities.map { budgetEntity ->
                    val spentForCategory = transactions
                        .filter { it.transactionType == "outcome" }
                        .filter { transaction -> // فیلتر کردن تراکنش‌هایی که دسته‌بندی بودجه را شامل می‌شوند
                            transaction.category.contains(budgetEntity.category)
                        }
                        .sumOf { it.amount } // جمع کردن مبلغ تراکنش‌ها
                    BudgetItem(
                        id = budgetEntity.id,
                        category = budgetEntity.category,
                        budgetAmount = budgetEntity.budgetAmount,
                        spentAmount = spentForCategory
                    )
                }
            }.collect { updatedBudgets ->
                _budgets.value = updatedBudgets
            }
        }
    }

    fun saveBudget(budgetItem: BudgetItem) {
        viewModelScope.launch {
            // بررسی می‌کنیم که آیا بودجه‌ای با این دسته‌بندی از قبل وجود دارد یا خیر
            val existingBudget = budgetDao.getBudgetByCategory(budgetItem.category)
            if (existingBudget != null) {
                // اگر وجود دارد، آن را به‌روزرسانی می‌کنیم
                budgetDao.updateBudget(existingBudget.copy(budgetAmount = budgetItem.budgetAmount))
            } else {
                // اگر وجود ندارد، یک بودجه جدید اضافه می‌کنیم
                budgetDao.insertBudget(BudgetEntity(category = budgetItem.category, budgetAmount = budgetItem.budgetAmount))
            }
        }
    }
}

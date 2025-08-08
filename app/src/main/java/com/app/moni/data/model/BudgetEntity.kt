package com.app.moni.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * مدل داده‌ای برای ذخیره اطلاعات بودجه‌های تعریف شده توسط کاربر.
 */
@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String, // دسته‌بندی که بودجه برای آن تعریف شده است
    val budgetAmount: Double, // مبلغ بودجه
    val createdAt: Long = System.currentTimeMillis() // زمان ایجاد بودجه
)

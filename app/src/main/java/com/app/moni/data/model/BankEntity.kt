package com.app.moni.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * مدل داده‌ای برای ذخیره اطلاعات بانک‌ها و سرشماره‌های تعریف شده توسط کاربر.
 */
@Entity(tableName = "banks")
data class BankEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val senderNumber: String, // شماره یا نام فرستنده پیامک (مثلاً: "100060" یا "بلو")
    val bankName: String
)

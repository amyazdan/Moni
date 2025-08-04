package com.app.moni.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val transactionType: String, // "income" or "outcome"
    val rawSmsText: String,
    val note: String?, // nullable
    val dateTime: Long, // timestamp
    val createdAt: Long, // timestamp
    val category: List<String>,
    val bankName: String?, // nullable
    val balanceAfter: Double,
    val amount: Double,
    val accountNumber: String
)

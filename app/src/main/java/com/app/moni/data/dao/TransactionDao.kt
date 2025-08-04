package com.app.moni.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.moni.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY dateTime DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>
}

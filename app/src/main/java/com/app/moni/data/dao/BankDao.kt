package com.app.moni.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.app.moni.data.model.BankEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) برای انجام عملیات‌های پایگاه داده روی BankEntity.
 */
@Dao
interface BankDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBank(bank: BankEntity)

    @Query("SELECT * FROM banks")
    fun getAllBanks(): Flow<List<BankEntity>>

    @Query("SELECT bankName FROM banks WHERE senderNumber = :sender")
    suspend fun getBankNameBySender(sender: String): String?
}

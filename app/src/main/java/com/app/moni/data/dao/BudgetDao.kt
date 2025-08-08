package com.app.moni.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.app.moni.data.model.BudgetEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) برای انجام عملیات‌های پایگاه داده روی BudgetEntity.
 */
@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity)

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Query("SELECT * FROM budgets ORDER BY category ASC")
    fun getAllBudgets(): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE category = :categoryName LIMIT 1")
    suspend fun getBudgetByCategory(categoryName: String): BudgetEntity?
}

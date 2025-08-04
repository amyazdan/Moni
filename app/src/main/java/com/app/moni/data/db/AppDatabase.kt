package com.app.moni.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.moni.data.converter.StringListConverter
import com.app.moni.data.dao.BankDao
import com.app.moni.data.dao.TransactionDao
import com.app.moni.data.model.BankEntity
import com.app.moni.data.model.TransactionEntity

@Database(entities = [TransactionEntity::class, BankEntity::class], version = 1, exportSchema = false)
@TypeConverters(StringListConverter::class) // افزودن TypeConverter
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun bankDao(): BankDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "moni_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

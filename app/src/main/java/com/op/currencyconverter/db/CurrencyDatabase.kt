package com.op.currencyconverter.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.op.currencyconverter.Models.CurrencyModel

@Database(entities = [CurrencyModel::class], version = 1, exportSchema = false)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun daoAccess(): DaoAccess
}
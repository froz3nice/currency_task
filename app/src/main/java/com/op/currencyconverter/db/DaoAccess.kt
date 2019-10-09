package com.op.currencyconverter.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.op.currencyconverter.Models.CurrencyModel


@Dao
interface DaoAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(values: List<CurrencyModel>)

    @Query("UPDATE currencies SET amount=:amount WHERE currency = :currency")
    suspend fun updateCurrency(amount: Float,currency: String)

    @Query("UPDATE currencies SET totalCommission=:fee WHERE currency = :currency")
    suspend fun updateCommission(fee: Float,currency: String)

    @Query("SELECT * FROM currencies where currency = :currency ")
    suspend fun getCurrency(currency: String): CurrencyModel

    @Query("SELECT * FROM currencies")
    suspend fun getAllCurrencies(): List<CurrencyModel>
}


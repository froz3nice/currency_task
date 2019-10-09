package com.op.currencyconverter.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "currencies")
data class CurrencyModel(
    @ColumnInfo(name = "amount")
    @SerializedName("amount")
    @Expose
    var amount: Float,

    @ColumnInfo(name = "currency")
    @SerializedName("currency")
    @PrimaryKey
    @Expose var currency: String,

    @ColumnInfo(name = "totalCommission")
    var totalCommission: Float
)

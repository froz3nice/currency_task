package com.op.currencyconverter

import com.op.currencyconverter.Models.CurrencyModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface ApiService {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET
    fun getConversion(@Url url: String): Call<CurrencyModel?>
}
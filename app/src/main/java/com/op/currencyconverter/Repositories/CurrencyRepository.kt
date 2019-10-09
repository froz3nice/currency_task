package com.op.currencyconverter.Repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.op.currencyconverter.ApiService
import com.op.currencyconverter.Models.CurrencyModel
import com.op.currencyconverter.Models.DataModel
import com.op.currencyconverter.db.DaoAccess
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CurrencyRepository : BaseRepository() {

    fun getConversionResponse(
        query: String,
        api: ApiService,
        liveData: MutableLiveData<CurrencyModel>
    ) {
        val call = api.getConversion(query)
        call.enqueue { result ->
            when (result) {
                is Result.Success -> {
                    liveData.value = result.response.body()
                }
                is Result.Failure -> {
                    liveData.value = null
                    result.error.printStackTrace()
                }
            }
        }
    }

    fun initCurrencies(db: DaoAccess, liveData: MutableLiveData<List<CurrencyModel>>) {
        GlobalScope.launch {
            val values = ArrayList<CurrencyModel>()
            values.add(CurrencyModel(1000f, "EUR", 0f))
            values.add(CurrencyModel(0f, "USD", 0f))
            values.add(CurrencyModel(0f, "JPY", 0f))
            db.insertCurrencies(values)
            liveData.postValue(db.getAllCurrencies())
        }
    }

    fun updateCurrencies(
        dao: DaoAccess,
        data: DataModel,
        liveData: MutableLiveData<List<CurrencyModel>>
    ) {
        GlobalScope.launch {
            Log.d("current", dao.getCurrency(data.sourceCurrency).amount.toString())
            Log.d("sourceCurrency", data.inputAmount.toString())
            val inputAndCommission = data.inputAmount + data.fee
            val changedAmountFrom = dao.getCurrency(data.sourceCurrency).amount - inputAndCommission
            dao.updateCurrency(changedAmountFrom, data.sourceCurrency)

            val changedAmountTo =
                dao.getCurrency(data.destinationCurrency).amount + data.destinationAmount
            dao.updateCurrency(changedAmountTo, data.destinationCurrency)

            val totalFee = dao.getCurrency(data.sourceCurrency).totalCommission + data.fee
            dao.updateCommission(totalFee, data.sourceCurrency)

            liveData.postValue(dao.getAllCurrencies())
        }
    }

    fun getCurrencyData(dao: DaoAccess, liveData: MutableLiveData<List<CurrencyModel>>) {
        GlobalScope.launch {
            liveData.postValue(dao.getAllCurrencies())
        }
    }

}
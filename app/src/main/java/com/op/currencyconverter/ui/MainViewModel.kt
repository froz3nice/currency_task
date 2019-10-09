package com.op.currencyconverter.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.op.currencyconverter.Constants.BASE_URL
import com.op.currencyconverter.db.DaoAccess
import com.op.currencyconverter.Models.CurrencyModel
import com.op.currencyconverter.Models.DataModel
import com.op.currencyconverter.Provider.webservice
import com.op.currencyconverter.Repositories.CurrencyRepository

class MainViewModel(private val dao: DaoAccess) : ViewModel() {

    private var conversionLiveData = MutableLiveData<CurrencyModel>()

    private var allCurrenciesRoomLiveData = MutableLiveData<List<CurrencyModel>>()

    fun getConversionLiveData(): MutableLiveData<CurrencyModel> {
        return conversionLiveData
    }

    fun getAllCurrenciesLiveData(): MutableLiveData<List<CurrencyModel>> {
        return allCurrenciesRoomLiveData
    }

    private val repository: CurrencyRepository = CurrencyRepository()
    fun doCurrencyConversion(data: DataModel) {

        val query =
            BASE_URL + "currency/commercial/exchange/${data.inputAmount}-" + data.sourceCurrency + "/" + data.destinationCurrency + "/latest"
        repository.getConversionResponse(query, webservice, conversionLiveData)
    }

    fun initCurrencies() {
        repository.initCurrencies(dao, allCurrenciesRoomLiveData)
    }

    fun updateCurrencies(data: DataModel) {
        repository.updateCurrencies(dao, data, allCurrenciesRoomLiveData)
    }

    fun getCurrencies() {
        repository.getCurrencyData(dao, allCurrenciesRoomLiveData)
    }
}
package com.op.currencyconverter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatSpinner
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.op.currencyconverter.db.CurrencyDatabase
import com.op.currencyconverter.Factories.ViewModelFactory
import com.op.currencyconverter.Models.CurrencyModel
import com.op.currencyconverter.Models.DataModel
import com.op.currencyconverter.Prefs.getInt
import com.op.currencyconverter.Prefs.putInt
import com.op.currencyconverter.R
import com.op.currencyconverter.Utils
import com.op.currencyconverter.Utils.getCurrencyList
import com.op.currencyconverter.Utils.getFee
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    var currencyList = ArrayList<CurrencyModel>()
    var data = DataModel()
    lateinit var db: CurrencyDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = Room.databaseBuilder(
            applicationContext,
            CurrencyDatabase::class.java, "currencies.db"
        ).build()

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(db.daoAccess())
        ).get(MainViewModel::class.java)

        observe()
        checkIfFirstTimeStarted()
        initSpinner(spinner_from)
        initSpinner(spinner_to)
        initConfirmListener()

    }

    private fun initConfirmListener() {
        btn_confirm.setOnClickListener {
            val isDataCorrect = Utils.validateFields(
                this, et_input.text.toString(),
                spinner_from.selectedItem.toString(),
                spinner_to.selectedItem.toString(),
                getCurrencyObj(currencyList,spinner_from.selectedItem.toString()).amount
            )
            if (isDataCorrect) {
                data.inputAmount = et_input.text.toString().toFloat()
                data.sourceAmount =
                    currencyList.find { it.currency == spinner_from.selectedItem.toString() }!!.amount
                data.sourceCurrency = spinner_from.selectedItem.toString()
                data.destinationCurrency = spinner_to.selectedItem.toString()
                viewModel.doCurrencyConversion(data)
            }
        }
    }

    private fun checkIfFirstTimeStarted() {
        if (getInt(this, "firstTimeOpened", 0) == 0) {
            viewModel.initCurrencies()
            putInt(this, "firstTimeOpened", 1)
        } else {
            viewModel.getCurrencies()
        }
    }

    private fun observe() {
        viewModel.getConversionLiveData().observe(this, Observer {
            if (it != null && data.inputAmount != 0f) {
                putInt(this, "conversionCount", getInt(this, "conversionCount", 0) + 1)
                data.fee = getFee(this, data.inputAmount)
                data.destinationCurrency = it.currency
                data.destinationAmount = it.amount
                viewModel.updateCurrencies(data)
                data.fee = getFee(this, data.inputAmount)
                Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show()            }
        })

        viewModel.getAllCurrenciesLiveData().observe(this, Observer {
            currencyList = it as ArrayList<CurrencyModel>
            tv_commissions.text = String.format(
                "Total commission fees : JPY - %.2f , EUR - %.2f , USD - %.2f",
                getCurrencyObj(it, "JPY").totalCommission,
                getCurrencyObj(it, "EUR").totalCommission,
                getCurrencyObj(it, "USD").totalCommission
            )
            tv_euro.text = String.format("%.2f eur", getCurrencyObj(it, "EUR").amount)
            tv_usd.text = String.format("%.2f usd", getCurrencyObj(it, "USD").amount)
            tv_jpy.text = String.format("%.0f jpy", getCurrencyObj(it, "JPY").amount)
        })
    }

    private fun getCurrencyObj(model: ArrayList<CurrencyModel>, currency: String): CurrencyModel {
        return model.find { it.currency == currency }!!
    }

    private fun initSpinner(spinner: AppCompatSpinner) {
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getCurrencyList(this))
    }
}

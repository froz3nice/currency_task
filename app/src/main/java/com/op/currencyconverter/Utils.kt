package com.op.currencyconverter

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.op.currencyconverter.Prefs.getInt

object Utils {
    fun validateFields(
        context: Context,
        input: String,
        sourceCurrency: String,
        destinationCurrency: String,
        sourceAmount: Float
    ): Boolean {
        if (!isNetworkAvailable(context)) {
            Toast.makeText(context, R.string.warning_network_unavailable, Toast.LENGTH_LONG).show()
            return false
        }
        if (input.isEmpty()) {
            Toast.makeText(context, R.string.warning_empty_field, Toast.LENGTH_LONG).show()
            return false
        }
        if (sourceCurrency == destinationCurrency) {
            Toast.makeText(context, R.string.warning_same_currencies, Toast.LENGTH_LONG).show()
            return false
        }
        val amountAfterComission = input.toFloat() + getFee(context,input.toFloat())

        if (sourceAmount < amountAfterComission) {
            Toast.makeText(context, R.string.warning_insufficient_balance, Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    fun getCurrencyList(context: Context):Array<String>{
        val myStrings = arrayOf(
            context.getString(R.string.label_jpy),
            context.getString(R.string.label_usd),
            context.getString(R.string.label_eur)
        )
        return myStrings
    }

    fun getFee(context: Context,value: Float): Float {
        val isNotFree = getInt(context, "conversionCount", 0) > 5
        val isEvery10thFree = getInt(context, "conversionCount", 0) % 10 == 0
        if (isNotFree) {
            return value * 0.007f
        }
        return 0f
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                val ni = cm.activeNetworkInfo
                if (ni != null) {
                    return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
                }
            } else {
                val n = cm.activeNetwork
                if (n != null) {
                    val nc = cm.getNetworkCapabilities(n)
                    return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI)
                }
            }
        }
        return false
    }
}
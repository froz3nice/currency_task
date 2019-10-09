package com.op.currencyconverter

import android.content.Context
import androidx.preference.PreferenceManager

object Prefs {
    fun putInt(context: Context, key: String, value: Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).apply()
    }
    fun getInt(context: Context, key: String, defaultValue: Int): Int {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defaultValue)
    }

}
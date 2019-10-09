package com.op.currencyconverter.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.op.currencyconverter.db.DaoAccess
import com.op.currencyconverter.ui.MainViewModel

class ViewModelFactory(private val dao: DaoAccess) : ViewModelProvider.Factory {
    // Passing Providers here

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(dao) as T
    }
}
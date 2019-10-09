package com.op.currencyconverter.Models

data class DataModel(
    var sourceCurrency: String = "",
    var sourceAmount: Float = 0f,
    var destinationCurrency: String = "",
    var destinationAmount: Float = 0f,
    var inputAmount: Float = 0f,
    var fee: Float = 0f
) {
    override fun toString(): String = String.format(
        "You have converted %.2f %s to %.2f %s. Commission Fee - %.2f %s.",
        inputAmount,
        sourceCurrency,
        destinationAmount,
        destinationCurrency,
        fee,
        sourceCurrency
    )


}
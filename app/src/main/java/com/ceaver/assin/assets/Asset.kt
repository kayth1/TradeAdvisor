package com.ceaver.assin.assets

import com.ceaver.assin.markets.Title
import java.math.BigDecimal

data class Asset(//
        val title: Title,
        val label: String?,
        val quantity: BigDecimal,
        val open: Quotes,
        val current: Quotes
) {
    data class Quotes(
            val valueCrypto: BigDecimal,
            val valueFiat: BigDecimal)
}
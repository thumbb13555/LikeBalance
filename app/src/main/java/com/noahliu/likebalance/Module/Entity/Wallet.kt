package com.noahliu.likebalance.Module.Entity

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

data class Wallet(var balances:ArrayList<Balance>) {
    class Balance(var denom: String, var amount: String){
        fun getChangeAmount(): String {
            val v: Double = amount.toDouble() / 10.0.pow(9.0)
            return BigDecimal(v).setScale(4, RoundingMode.HALF_EVEN).toString()
        }
    }
}

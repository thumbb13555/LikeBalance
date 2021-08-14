package com.noahliu.likebalance.Module.Entity

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

data class Wallet(var height:String, var result:Result) {

    class Result(var type:String, var value:Value)
    class Coin (var denom:String, var amount:String){
        fun getChangeAmount():String{
            val v :Double = amount.toDouble()/ 10.0.pow(9.0)
            return BigDecimal(v).setScale(4, RoundingMode.HALF_EVEN).toString()
        }
    }
    class PublicKey (var type: String, var value:String)
    class Value (var address:String, var coins:List<Coin>, var public_key:PublicKey, var account_number:String, var sequence:String)

}
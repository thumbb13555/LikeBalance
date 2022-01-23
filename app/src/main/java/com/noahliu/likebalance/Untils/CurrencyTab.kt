package com.noahliu.likebalance.Untils

import com.noahliu.likebalance.Module.Entity.LikeQuote
import java.util.*

object CurrencyTab {

    fun getCountryCurrency(data:LikeQuote.Data):Double{
        return when(Locale.getDefault().country){
            "TW"-> data.TWD
            "HK"-> data.HKD
            "JP"-> data.JPY
            "CN"-> data.CNY
            "IN"-> data.IND
            "MY"-> data.MYR
            "SG"-> data.SGD
            else -> data.USD
        }
    }
    fun getCountryTAB():String{
        return when(Locale.getDefault().country){
            "TW"-> "TWD"
            "HK"-> "HKD"
            "JP"-> "JPY"
            "CN"-> "CNY"
            "IN"-> "IND"
            "MY"-> "MYR"
            "SG"-> "SGD"
            else -> "USD"
        }
    }
}
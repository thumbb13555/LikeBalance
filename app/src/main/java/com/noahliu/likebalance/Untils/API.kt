package com.noahliu.likebalance.Untils

import android.appwidget.AppWidgetProvider
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Pattern

abstract class API : AppCompatActivity() {

    companion object{
        const val BASE_MATTERS = "https://server.matters.news/graphql"
        const val ERROR_CODE = "Error_wfowefiwoejfwejfoweifnwnwe15a1v3"
        private val WALLET_PATTERN: Pattern = Pattern.compile("[cosmos]{6}[0-9a-z]{39}")
        fun accountRequest(account:String):String{
            return "https://api.like.co/users/id/${account}/min"
        }

        fun balanceRequest(address: String):String{
            return "https://mainnet-node.like.co/cosmos/bank/v1beta1/balances/$address/nanolike"
        }

        fun getCheckHttp(input:String):String{
            val match = WALLET_PATTERN.matcher(input).matches()
            return if (!match) accountRequest(input)
            else balanceRequest(input)
        }

    }

}

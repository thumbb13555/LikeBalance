package com.noahliu.likebalance.Module.Entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class LikeQuote(var base:String, var time:String, var data:Data) {
    class Data (@SerializedName("2909") var likeCoin:LikeCoin,
                var USD:Double,
                var TWD:Double,
                var JPY:Double,
                var HKD:Double,
                var CNY:Double,
                var MYR:Double,
                var SGD:Double,
                var IND:Double,
    )
    class LikeCoin (
        var id:Int = 0,
        var name: String,
        var symbol: String,
        var slug: String,
        var num_market_pairs:Int,
        var date_added: Date,
        var tags: List<String>,
        var max_supply:Int,
        var circulating_supply:Double,
        var total_supply:Double,
        var is_active:Int,
        var cmc_rank:Int,
        var is_fiat:Int,
        var last_updated: Date,
        var quote: Quote
    )
    class Quote(var TWD: TWD, var USDT:USDT)
    class TWD (
        var price:Double,
        var volume_24h:Double,
        var percent_change_1h:Double,
        var percent_change_24h:Double,
        var percent_change_7d:Double,
        var percent_change_30d:Double,
        var percent_change_60d:Double,
        var percent_change_90d:Double,
        var market_cap:Double,
        var market_cap_dominance:Double,
        var fully_diluted_market_cap:Double,
        var last_updated: Date)

    class USDT (
        var price:Double,
        var volume_24h:Double,
        var percent_change_1h:Double,
        var percent_change_24h:Double,
        var percent_change_7d:Double,
        var percent_change_30d:Double,
        var percent_change_60d:Double,
        var percent_change_90d:Double,
        var market_cap:Double,
        var market_cap_dominance:Double,
        var fully_diluted_market_cap:Double,
        var last_updated: Date)
}







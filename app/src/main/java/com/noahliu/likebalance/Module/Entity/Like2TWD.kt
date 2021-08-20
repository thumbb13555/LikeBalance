package com.noahliu.likebalance.Module.Entity

import java.util.*

data class Like2TWD(var status:Status, var data:Data) {
    class Status {
        var timestamp: Date? = null
        var error_code = 0
        var error_message: Any? = null
        var elapsed = 0
        var credit_count = 0
        var notice: Any? = null
    }

    class TWD {
        var price = 0.0
        var volume_24h = 0.0
        var percent_change_1h = 0.0
        var percent_change_24h = 0.0
        var percent_change_7d = 0.0
        var percent_change_30d = 0.0
        var percent_change_60d = 0.0
        var percent_change_90d = 0.0
        var market_cap = 0.0
        var market_cap_dominance = 0.0
        var fully_diluted_market_cap = 0.0
        var last_updated: Date? = null
    }

    class Quote {
        @JsonProperty("TWD")
        var tWD: TWD? = null
    }

    class _2909 {
        var id = 0
        var name: String? = null
        var symbol: String? = null
        var slug: String? = null
        var num_market_pairs = 0
        var date_added: Date? = null
        var tags: List<String>? = null
        var max_supply = 0
        var circulating_supply = 0.0
        var total_supply = 0.0
        var is_active = 0
        var platform: Any? = null
        var cmc_rank = 0
        var is_fiat = 0
        var last_updated: Date? = null
        var quote: Quote? = null
    }

    class Data {
        @JsonProperty("2909")
        var _2909: _2909? = null
    }
}

annotation class JsonProperty(val value: String)

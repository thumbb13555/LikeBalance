package com.noahliu.likebalance.Module.Entity

data class LikerAccount(
    var user:String,
    var displayName:String,
    var avatar:String,
    var cosmosWallet:String,
    var isSubscribedCivicLiker:Boolean,
    var civicLikerSince:Long
)

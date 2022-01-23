package com.noahliu.likebalance.Untils
import com.noahliu.likebalance.Module.OkHttpModule
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

fun main() {

    print(Locale.getDefault().country)

//    val array = ArrayList<String>()
//    array.add(API.GET_Like2TWD_PRICE)
//    array.add(API.GET_Like2USDT_PRICE)
//
//    val info = runBlocking {
//        return@runBlocking getDetailInfo(array)
//    }
//    info.forEach {
//        print(it)
//    }

}
private suspend fun getDetailInfo(arrayList: ArrayList<String>): List<String> {
    return coroutineScope {
        return@coroutineScope (0 until arrayList.size).map {
            async(Dispatchers.IO) {
                return@async OkHttpModule.sendGET(arrayList[it])[0]
            }
        }.awaitAll()
    }
}
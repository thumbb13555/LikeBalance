package com.noahliu.likebalance.Module.Service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import android.widget.RemoteViews
import com.google.gson.Gson
import com.noahliu.likebalance.Controller.BalanceProvider
import com.noahliu.likebalance.Controller.LikePriceProvider
import com.noahliu.likebalance.Controller.MainActivity
import com.noahliu.likebalance.Module.Entity.Like2TWD
import com.noahliu.likebalance.Module.Entity.Wallet
import com.noahliu.likebalance.Module.GetAsyncTask
import com.noahliu.likebalance.Module.OkHttpModule
import com.noahliu.likebalance.Module.SharedPreferences.MySharedPreferences
import com.noahliu.likebalance.R
import com.noahliu.likebalance.Untils.API
import kotlinx.coroutines.*
import java.lang.Runnable
import java.text.SimpleDateFormat
import java.util.*

class PriceService: Service(),Runnable {
    companion object{
        val TAG = PriceService::class.java.simpleName+"My"
        //        const val delay = 600000L
        const val delay = 5000L
        const val ClickEvent = "android.appwidget.action.UPDATE"
    }
    @SuppressLint("SimpleDateFormat")
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                1->{
                    update()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate(Service): ")
        handler.sendEmptyMessage(1)
        /**每1秒會再重複執行此task*/
        handler.postDelayed(this,  1)

    }


    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        Log.d(TAG, "onStart(Service): ")

        if (intent!!.action != null) {
            if (intent.action.equals(ClickEvent)) {
                Log.d(TAG, "onStart: (Click)")
                update()
            }
        }
        setButtonClick()
    }
    private fun setButtonClick(){
        val myIntent = Intent()
        myIntent.action = ClickEvent
        val thisWidget = ComponentName(this, BalanceProvider::class.java)
        val manager = AppWidgetManager.getInstance(this)
        val remoteViews = RemoteViews(packageName, R.layout.like_price)
        val pendingIntent = PendingIntent.getService(
            this, 0,
            myIntent, 0
        )
        remoteViews.setOnClickPendingIntent(R.id.imageButton_Price_Refresh, pendingIntent)
        manager.updateAppWidget(thisWidget, remoteViews)
    }

    fun update(){
//        val array = ArrayList<String>()
//        array.add(API.GET_Like2TWD_PRICE)
//        array.add(API.GET_Like2USDT_PRICE)
//        val info = runBlocking {
//            return@runBlocking getDetailInfo(array)
//        }
//        info.forEach {
//            try {
//                val gson = Gson().fromJson(it, Like2TWD::class.java)
//                Log.d(TAG, "update: ${gson.status.credit_count}")
//
//            }catch (e:Exception){
//                Log.d(TAG, "update: Boon")
//
//            }
//        }



        val time = sdf.format(Date())
        val remoteViews = RemoteViews(packageName, R.layout.like_price)
        remoteViews.setTextViewText(R.id.textView_LastTime,time)
        val manager = AppWidgetManager.getInstance(applicationContext)
        val componentName = ComponentName(applicationContext,LikePriceProvider::class.java)
        manager.updateAppWidget(componentName,remoteViews)


    }
    suspend fun getDetailInfo(arrayList: ArrayList<String>): List<String> {
        return coroutineScope {
            return@coroutineScope (0 until arrayList.size).map {
                async(Dispatchers.IO) {
                    return@async OkHttpModule.sendGETWithHeader(arrayList[it])[0]
                }
            }.awaitAll()
        }
    }




    override fun run() {
        handler.sendEmptyMessage(1)

        handler.postDelayed(this,  delay)
    }
}
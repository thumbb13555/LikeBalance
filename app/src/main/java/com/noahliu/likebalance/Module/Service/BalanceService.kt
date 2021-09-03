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
import com.noahliu.likebalance.Module.Entity.LikeQuote
import com.noahliu.likebalance.Module.Entity.Wallet
import com.noahliu.likebalance.Module.GetAsyncTask
import com.noahliu.likebalance.Module.OkHttpModule
import com.noahliu.likebalance.Module.SharedPreferences.MySharedPreferences
import com.noahliu.likebalance.R
import com.noahliu.likebalance.Untils.API
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class BalanceService : Service(),Runnable {
    companion object{
        val TAG = BalanceService::class.java.simpleName+"My"
        const val delay = 600000L//10分鐘
//        const val delay = 5000L
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
        handler.postDelayed(this,1000)

    }


    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        Log.d(TAG, "onStart(Service): ")

        if (intent!!.action != null) {
            if (intent.action.equals(ClickEvent)) {
                Log.d(TAG, "onStart: (Click)")
                val remoteViews = RemoteViews(packageName, R.layout.balance_provider)
                remoteViews.setTextViewText(R.id.textView_LastTime,getString(R.string.widget_update))
                val manager = AppWidgetManager.getInstance(applicationContext)
                val componentName = ComponentName(applicationContext,BalanceProvider::class.java)
                manager.updateAppWidget(componentName,remoteViews)
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
        val remoteViews = RemoteViews(packageName, R.layout.balance_provider)
        val pendingIntent = PendingIntent.getService(
            this, 0,
            myIntent, 0
        )
        remoteViews.setOnClickPendingIntent(R.id.imageButton_Refresh, pendingIntent)
        manager.updateAppWidget(thisWidget, remoteViews)
    }

    fun update(){
        val remoteViews = RemoteViews(packageName, R.layout.balance_provider)
        val time = sdf.format(Date())
        val likerAccount = MySharedPreferences.read(this)
        if (likerAccount == null){
            Log.d(TAG, "update: 尚未綁定任何帳號")
            return
        }
        val url = API.balanceRequest(likerAccount.cosmosWallet)
        val urlPrice = API.GET_LikePRICE
        CoroutineScope(Dispatchers.IO).launch {
            val result = OkHttpModule.sendGET(url)
            val price = OkHttpModule.sendGET(urlPrice)
            withContext(Dispatchers.Default){
                try {
                    val gson = Gson().fromJson(result[0], Wallet::class.java)
                    val priceGson = Gson().fromJson(price[0],LikeQuote::class.java)
                    val twd = String.format("%.1f",gson.balance.getChangeAmount().toDouble()*priceGson.data.TWD)
                    val amount = (gson.balance.getChangeAmount())+" LIKE\n"+"= $"+(twd)+"(TWD)"
                    Log.d(TAG, "Update: $amount, $time")
                    remoteViews.setTextViewText(R.id.textView_Balance,amount)
                    remoteViews.setTextViewText(R.id.textView_LikeID,"Liker: ${likerAccount.displayName}")
                    remoteViews.setTextViewText(R.id.textView_LastTime,time)

                }catch (e:Exception){
                    Log.d(TAG, "onHttpRespond: 無網路或出錯狀態")
                    remoteViews.setTextViewText(R.id.textView_LastTime,time+getString(R.string.service_no_internet))
                }

                val manager = AppWidgetManager.getInstance(applicationContext)
                val componentName = ComponentName(applicationContext,BalanceProvider::class.java)
                manager.updateAppWidget(componentName,remoteViews)
            }
        }

    }


    override fun run() {
        handler.sendEmptyMessage(1)

        handler.postDelayed(this,  delay)
    }
}
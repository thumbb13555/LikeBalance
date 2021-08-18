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
import com.noahliu.likebalance.Module.Entity.Wallet
import com.noahliu.likebalance.Module.GetAsyncTask
import com.noahliu.likebalance.Module.SharedPreferences.MySharedPreferences
import com.noahliu.likebalance.R
import com.noahliu.likebalance.Untils.API
import java.text.SimpleDateFormat
import java.util.*


class BalanceService() : Service(),Runnable {
    companion object{
        val TAG = BalanceService::class.java.simpleName+"My"
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
        val remoteViews = RemoteViews(packageName, R.layout.balance_provider)
        val pendingIntent = PendingIntent.getService(
            this, 0,
            myIntent, 0
        )
        remoteViews.setOnClickPendingIntent(R.id.imageButton_Refresh, pendingIntent)
        manager.updateAppWidget(thisWidget, remoteViews)
    }

    fun update(){
        val likerAccount = MySharedPreferences.read(this)
        if (likerAccount == null){
            Log.d(TAG, "update: 尚未綁定任何帳號")
            return
        }
        val task = GetAsyncTask(this, 0, false)
        val account = likerAccount.cosmosWallet
        val url = API.balanceRequest(account)

        task.execute(url)
        task.onHttpRespond = object : GetAsyncTask.OnHttpRespond {
            override fun onHttpRespond(result: ArrayList<String>, operationCode: Int) {
                try {
                    val gson = Gson().fromJson(result[0], Wallet::class.java)
                    val amount = (gson.result.value.coins[0].getChangeAmount())+" LIKE"
                    val time = sdf.format(Date())
                    Log.d(TAG, "Update: $amount, $time")
                    val remoteViews = RemoteViews(packageName, R.layout.balance_provider)
                    remoteViews.setTextViewText(R.id.textView_Balance,amount)
                    remoteViews.setTextViewText(R.id.textView_LastTime,time)
                    remoteViews.setTextViewText(R.id.textView_LikeID,"Liker: ${likerAccount.displayName}")
                    val manager = AppWidgetManager.getInstance(applicationContext)
                    val componentName = ComponentName(applicationContext,BalanceProvider::class.java)
                    manager.updateAppWidget(componentName,remoteViews)
                }catch (e:Exception){
                    Log.d(TAG, "onHttpRespond: 無網路或出錯狀態")
                }
            }
            override fun onProgressRespond(value: Int) {}
        }
    }

    @SuppressLint("WrongConstant")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, START_STICKY, startId)
    }


    override fun run() {
        handler.sendEmptyMessage(1)

        handler.postDelayed(this,  delay)
    }
}
package com.noahliu.likebalance.Controller

import android.app.ActivityManager
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import com.noahliu.likebalance.Module.Service.BalanceService
import com.noahliu.likebalance.Module.Service.PriceService
import com.noahliu.likebalance.R

/**
 * Implementation of App Widget functionality.
 */
class LikePriceProvider : AppWidgetProvider() {
    val TAG = PriceService.TAG

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d(TAG, "onEnabled(Price): ")
        val intent = Intent(context, PriceService::class.java)
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            context.startForegroundService(intent)
        }
        context.startService(intent)

    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled(Price): ")
        context!!.stopService(Intent(context, PriceService::class.java))

    }


    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d(TAG, "onReceive: ${intent!!.action}")
        val hasService = isServiceRun(context!!)

        Log.d(TAG, "onReceive是否有Price Service?: $hasService")
        if (intent.action.equals("android.appwidget.action.APPWIDGET_UPDATE")) {
            startMyService(hasService, context)
        }else if(intent.action.equals("android.appwidget.action.APPWIDGET_UPDATE_OPTIONS")){
            try {
                startMyService(hasService, context)
            }catch (e:Exception){
                Log.d(TAG, "onReceive: 出錯: $e")
            }
        }
    }
    private fun startMyService(hasService: Boolean, context: Context) {
        if (!hasService) {
            Log.d(TAG, "onReceive(Price): 刷新service")
            val intent = Intent(context, PriceService::class.java)
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                context.startForegroundService(intent)
            }
            context.startService(intent)

        }
    }

    private fun isServiceRun(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = manager.getRunningServices(Int.MAX_VALUE)
        for (info in list) {
            if (PriceService::class.java.name == info.service.className)return true
        }
        return false
    }
}


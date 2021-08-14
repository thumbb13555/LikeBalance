package com.noahliu.likebalance.Controller

import android.app.ActivityManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.content.ContextCompat.getSystemService
import com.noahliu.likebalance.Module.BalanceService
import com.noahliu.likebalance.R


/**
 * Implementation of App Widget functionality.
 */
class BalanceProvider : AppWidgetProvider() {
    val TAG = BalanceService.TAG


    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d(TAG, "onEnabled: ")
        startMyService(isMyServiceRunning(context), context)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled: ")
        context!!.stopService(Intent(context, BalanceService::class.java))
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d(TAG, "onReceive: ${intent!!.action}")
        val hasService = isMyServiceRunning(context!!)
        Log.d(TAG, "onReceive是否有Service?: $hasService")
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
            Log.d(TAG, "onReceive: 刷新service")
            val intent = Intent(context, BalanceService::class.java)
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                context.startForegroundService(intent)
            }
                context.startService(intent)

        }
    }

    private fun isMyServiceRunning(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            return manager!!.getRunningServices(Int.MAX_VALUE).size > 0
        }else{
            return false
        }


    }
}

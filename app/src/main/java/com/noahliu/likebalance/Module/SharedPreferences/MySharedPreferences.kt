package com.noahliu.likebalance.Module.SharedPreferences

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.noahliu.likebalance.Controller.MainActivity
import com.noahliu.likebalance.Module.Entity.LikerAccount

object MySharedPreferences {
    val TAG = MainActivity.TAG
    const val SHARED_PREFERENCE = "noahliuPreferenceShared"
    const val ACCOUNT_INFO = "ACCOUNT"

    fun write(context: Context,likerAccount: LikerAccount):Boolean{
        val share = context.getSharedPreferences(SHARED_PREFERENCE,Context.MODE_PRIVATE)
        val editor = share.edit()
        editor.putString(ACCOUNT_INFO,Gson().toJson(likerAccount))
        Log.d(TAG, "write: ${Gson().toJson(likerAccount)}")
        return editor.commit()
    }
    fun read(context: Context): LikerAccount? {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(ACCOUNT_INFO, "")
        Log.d(TAG, "read: $json")
        if (json.isNullOrBlank()) return null
        return Gson().fromJson(json, LikerAccount::class.java)
    }


}
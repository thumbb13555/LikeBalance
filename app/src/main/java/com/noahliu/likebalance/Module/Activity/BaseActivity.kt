package com.noahliu.likebalance.Module.Activity

import android.appwidget.AppWidgetProvider
import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.noahliu.likebalance.Module.GetAsyncTask
import com.noahliu.likebalance.Module.PostAsyncTask
import com.noahliu.likebalance.R
import com.noahliu.likebalance.Untils.API
import okhttp3.FormBody

abstract class BaseActivity : API() {
    companion object{
        const val LOGIN_INFO = "ACCOUNT"
    }


    fun sendGET(
        url: String,
        apiType: Int,
        needDialog: Boolean,
        listener: GetAsyncTask.OnHttpRespond
    ) {
        val task = GetAsyncTask(this, apiType, needDialog)
        task.execute(url)
        task.onHttpRespond = listener
    }

    fun sendPost(
        url: String,
        formBody: FormBody,
        apiType: Int,
        needDialog: Boolean,
        listener: PostAsyncTask.OnHttpRespond
    ) {
        val task = PostAsyncTask(this, apiType, url, needDialog)
        task.execute(formBody)
        task.onHttpRespond = listener
    }

    fun showToast(msg:String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    fun showSnack(msg: String,view: View){
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show()
    }


}
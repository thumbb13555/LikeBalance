package com.noahliu.likebalance.Module

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask

class GetAsyncTask(context: Context, val operationCode: Int, val needDialog:Boolean) : AsyncTask<String, Int, ArrayList<String>>() {
    val TAG = GetAsyncTask::class.java.simpleName
     val dialog:ProgressDialog = ProgressDialog(context)
    lateinit var onHttpRespond:OnHttpRespond


    override fun onPreExecute() {
        super.onPreExecute()
        if (needDialog) {
            dialog.setMessage("Please Wait")
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    override fun doInBackground(vararg url: String?): ArrayList<String> {
        return OkHttpModule.sendGET(url[0]!!)
    }

    override fun onProgressUpdate(vararg values: Int?) {
        values[0]?.let { onHttpRespond.onProgressRespond(it) }
        super.onProgressUpdate(*values)
    }

    override fun onPostExecute(result: ArrayList<String>?) {
        super.onPostExecute(result)
        dialog.dismiss()
        onHttpRespond.onHttpRespond(result!!,operationCode)

    }
    interface OnHttpRespond{
        fun onHttpRespond(result:ArrayList<String>,operationCode:Int)
        fun onProgressRespond(value:Int)
    }
}
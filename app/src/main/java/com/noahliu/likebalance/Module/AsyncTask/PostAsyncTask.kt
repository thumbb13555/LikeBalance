package com.noahliu.likebalance.Module

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import okhttp3.FormBody

class PostAsyncTask(
    context: Context,
    val operationCode: Int,
    val url: String,
    val needDialog: Boolean
) : AsyncTask<FormBody, Int, ArrayList<String>>() {
    val TAG = PostAsyncTask::class.java.simpleName
    val dialog: ProgressDialog = ProgressDialog(context)
    lateinit var onHttpRespond: OnHttpRespond
    override fun onPreExecute() {
        super.onPreExecute()
        if (needDialog){
            dialog.setMessage("Please Wait")
            dialog.show()
        }
    }

    override fun onProgressUpdate(vararg values: Int?) {
        values[0]?.let { onHttpRespond.onProgressRespond(it) }
        super.onProgressUpdate(*values)
    }

    override fun onPostExecute(result: ArrayList<String>?) {
        super.onPostExecute(result)
        dialog.dismiss()
        onHttpRespond.onHttpRespond(result!!, operationCode)

    }

    interface OnHttpRespond {
        fun onHttpRespond(result: ArrayList<String>, operationCode: Int)
        fun onProgressRespond(value: Int)
    }

    override fun doInBackground(vararg formBody: FormBody): ArrayList<String> {
        return OkHttpModule.sendPOST(formBody[0], url)
    }
}
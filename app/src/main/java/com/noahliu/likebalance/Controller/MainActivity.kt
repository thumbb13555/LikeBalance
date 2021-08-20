package com.noahliu.likebalance.Controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.noahliu.likebalance.Module.Activity.BaseActivity
import com.noahliu.likebalance.Module.Entity.LikerAccount
import com.noahliu.likebalance.Module.GetAsyncTask
import com.noahliu.likebalance.Module.OkHttpModule
import com.noahliu.likebalance.Module.SharedPreferences.MySharedPreferences
import com.noahliu.likebalance.R
import com.noahliu.likebalance.Untils.API
import kotlinx.coroutines.*


class MainActivity : BaseActivity() ,GetAsyncTask.OnHttpRespond{
    companion object{
        val TAG = MainActivity::class.java.simpleName+"My"
    }
    lateinit var igHeadshot:ImageView
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val likerAccount = MySharedPreferences.read(this)

        if (likerAccount != null){
            igHeadshot = findViewById(R.id.imageView_Headshot)
            val intent = Intent()
            intent.action = "android.appwidget.action.APPWIDGET_UPDATE"
            sendBroadcast(intent)
            Glide.with(this).load(getDrawable(R.drawable.blank_head)).circleCrop().into(igHeadshot)
            Log.d(TAG, "onCreate(Activity)")
            updateUI(likerAccount)
        }else{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


    //Bind button clicked.
    fun logout(view: View){
        if (MySharedPreferences.clear(this)){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            showToast("unknown error")
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onHttpRespond(result: ArrayList<String>, operationCode: Int) {
        val res = result[0]
        if (res == "Not Found"){
            showToast(getString(R.string.main_word_not_found))
            return
        }
        val gson = Gson().fromJson(res,LikerAccount::class.java)
        updateUI(gson)
        MySharedPreferences.write(this,gson)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(gson: LikerAccount) {
        Glide.with(this).load(gson.avatar)
            .circleCrop()
            .placeholder(R.drawable.blank_head)
            .error(R.drawable.blank_head).into(igHeadshot)
        val tvTitle = findViewById<TextView>(R.id.textView_Hello)
        val tvWallet = findViewById<TextView>(R.id.textView_WalletRespond)
        val edInput = findViewById<EditText>(R.id.editTextText_Account)
        edInput.setText(gson.user)
        tvTitle.text = getString(R.string.main_word_hello) + " ${gson.displayName}"
        tvWallet.text = getString(R.string.main_word_bind_success)
    }

    override fun onProgressRespond(value: Int) {}
}
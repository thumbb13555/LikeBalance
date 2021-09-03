package com.noahliu.likebalance.Controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.noahliu.likebalance.Module.Activity.BaseActivity
import com.noahliu.likebalance.Module.Entity.LikeQuote
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
    lateinit var btBind:Button
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val likerAccount = MySharedPreferences.read(this)
        igHeadshot = findViewById(R.id.imageView_Headshot)
        btBind = findViewById(R.id.button_Bind)
        Glide.with(this).load(getDrawable(R.drawable.blank_head)).circleCrop().into(igHeadshot)
        val intent = Intent()
        intent.action = "android.appwidget.action.APPWIDGET_UPDATE"
        sendBroadcast(intent)
        if (likerAccount != null){
            Log.d(TAG, "onCreate(Activity)")
            updateUI(likerAccount)
        }else{
            updateUI()
        }
    }

    //Bind button clicked.
    fun logout(view: View){
       if (MySharedPreferences.read(this) != null){
           MySharedPreferences.clear(this)
           updateUI()
       }else{
           val edInput = findViewById<EditText>(R.id.editTextText_Account)
           val account = edInput.text.toString().trim()
           if (account.isEmpty()){
               showToast(getString(R.string.main_word_account_input_hint))
               return
           }
           sendGET(accountRequest(account),0,true,this)
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
        btBind.text = getString(R.string.main_word_unbind)
        tvWallet.text = getString(R.string.main_word_bind_success)
        edInput.isEnabled = false
    }
    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        Glide.with(this).load(R.drawable.blank_head)
            .circleCrop()
            .placeholder(R.drawable.blank_head)
            .error(R.drawable.blank_head).into(igHeadshot)
        val tvTitle = findViewById<TextView>(R.id.textView_Hello)
        val tvWallet = findViewById<TextView>(R.id.textView_WalletRespond)
        val edInput = findViewById<EditText>(R.id.editTextText_Account)
        edInput.setText("")
        tvTitle.text = getString(R.string.main_word_hello)
        btBind.text = getString(R.string.main_word_bind)
        tvWallet.text = getString(R.string.main_word_invite_account)
        edInput.isEnabled = true
    }


    override fun onProgressRespond(value: Int) {}
}
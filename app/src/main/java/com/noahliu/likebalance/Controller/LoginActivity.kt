package com.noahliu.likebalance.Controller

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.google.gson.Gson
import com.noahliu.graphqldemo.LoginMutation
import com.noahliu.likebalance.Module.Activity.BaseActivity
import com.noahliu.likebalance.Module.Apollo.Apollo
import com.noahliu.likebalance.Module.Apollo.AuthorizationInterceptor
import com.noahliu.likebalance.Module.EditText.PasswordEditText
import com.noahliu.likebalance.Module.Entity.LikerAccount
import com.noahliu.likebalance.Module.GetAsyncTask
import com.noahliu.likebalance.Module.SharedPreferences.MySharedPreferences
import com.noahliu.likebalance.R
import okhttp3.OkHttpClient

class LoginActivity :  BaseActivity() ,GetAsyncTask.OnHttpRespond{
    companion object{
        val TAG = LoginActivity::class.java.simpleName+"My"
    }

    val devToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjU3Mjc0IiwiaWF0IjoxNjI5MTkxMjQ2LCJleHAiOjE2MzY5NjcyNDZ9.IjG1BXvzs8K-oMWAkyv9JhFBIu-vcp6qpL41jCpdzv0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    fun signButtonClick(view: View){
        val edAccount = findViewById<EditText>(R.id.editText_Account)
        val edPassword =findViewById<PasswordEditText>(R.id.editText_Password)
        val account = edAccount.text.toString().trim()
        val password = edPassword.text.toString().trim()
        if (account.isBlank() || password.isBlank()){
            showToast(getString(R.string.login_account_or_psw_is_empty))
            return
        }
        val token = Apollo.sendLogin(account,password)
        if (token == ERROR_CODE){
            showToast(getString(R.string.main_word_not_found))
            return
        }
        val likerID = Apollo.getViewer(devToken)
        Log.d(TAG, "onCreate: $likerID")
        sendGET(accountRequest(likerID),0,true,this)
    }


    @SuppressLint("SetTextI18n")
    override fun onHttpRespond(result: ArrayList<String>, operationCode: Int) {
        val res = result[0]
        if (res == "Not Found"){
            showToast(getString(R.string.main_word_not_found))
            return
        }
        try {
            val gson = Gson().fromJson(res, LikerAccount::class.java)
            showToast(getString(R.string.main_word_bind_success))
            MySharedPreferences.write(this,gson)
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }catch (e:Exception){
            showToast("Unknown error")
        }
    }

    override fun onProgressRespond(value: Int) {}


}
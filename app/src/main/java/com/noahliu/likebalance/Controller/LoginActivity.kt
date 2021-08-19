package com.noahliu.likebalance.Controller

import android.annotation.SuppressLint
import android.app.ProgressDialog
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
    lateinit var waitDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        waitDialog = ProgressDialog(this)
    }
    fun signButtonClick(view: View){
        waitDialog.show()
        Thread{
            val edAccount = findViewById<EditText>(R.id.editText_Account)
            val edPassword =findViewById<PasswordEditText>(R.id.editText_Password)
            val account = edAccount.text.toString().trim()
            val password = edPassword.text.toString().trim()
            if (account.isBlank() || password.isBlank()){
                runOnUiThread {
                    showToast(getString(R.string.login_account_or_psw_is_empty))
                }
                return@Thread
            }
            val token = Apollo.sendLogin(account,password)
            if (token == ERROR_CODE){
                runOnUiThread {
                    showToast(getString(R.string.main_word_not_found))
                    waitDialog.dismiss()
                }
                return@Thread
            }
            val likerID = Apollo.getViewer(token)
            Log.d(TAG, "onCreate: $likerID")
            runOnUiThread {
                sendGET(accountRequest(likerID),0,false,this)
            }
        }.start()

    }

    @SuppressLint("SetTextI18n")
    override fun onHttpRespond(result: ArrayList<String>, operationCode: Int) {
        val res = result[0]
        if (res == "Not Found"){
            showToast(getString(R.string.main_word_not_found))
            waitDialog.dismiss()
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
        waitDialog.dismiss()
    }
    override fun onProgressRespond(value: Int) {}
}
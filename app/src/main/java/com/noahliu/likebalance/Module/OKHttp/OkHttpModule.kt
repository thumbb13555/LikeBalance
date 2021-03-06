package com.noahliu.likebalance.Module

import android.os.SystemClock
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Query
import com.noahliu.likebalance.Untils.API
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

object OkHttpModule {
    fun sendGET(url: String): ArrayList<String> {
        val arrayList = ArrayList<String>()
        val client = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .connectTimeout(10000, TimeUnit.SECONDS)
            .readTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(10000, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                arrayList.add("UnknownHostException")
            }

            override fun onResponse(call: Call, response: Response) {
                arrayList.add(response.body!!.string())
            }
        })//respond
        while (arrayList.isEmpty()) {
            SystemClock.sleep(1)
            if (arrayList.isNotEmpty()) break
        }
        return try {
            arrayList
        } catch (e: Exception) {
            arrayList
        }
    }
    fun sendGETWithHeader(url: String): ArrayList<String> {
        val arrayList = ArrayList<String>()
        val client = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .connectTimeout(10000, TimeUnit.SECONDS)
            .readTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(10000, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(url)
            .addHeader("X-CMC_PRO_API_KEY",API.KEY)
            .addHeader("Accepts","application/json")
            .build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                arrayList.add("UnknownHostException")
            }

            override fun onResponse(call: Call, response: Response) {
                arrayList.add(response.body!!.string())
            }
        })//respond
        while (arrayList.isEmpty()) {
            SystemClock.sleep(1)
            if (arrayList.isNotEmpty()) break
        }
        return try {
            arrayList
        } catch (e: Exception) {
            arrayList
        }
    }

    /**??????????????????POST??????*/
    fun sendPOST(formBody: FormBody, url: String): ArrayList<String> {
        val arrayList = ArrayList<String>()
        val client = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                arrayList.add("UnknownHostException")
            }

            override fun onResponse(call: Call, response: Response) {
                arrayList.add(response.body!!.string())
            }
        })//respond
        while (arrayList.isEmpty()) {
            SystemClock.sleep(1)
            if (arrayList.isNotEmpty()) break
        }
        return try {
            arrayList
        } catch (e: Exception) {
            arrayList
        }
    }//End


}
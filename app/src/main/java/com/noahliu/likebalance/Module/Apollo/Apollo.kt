package com.noahliu.likebalance.Module.Apollo
import android.os.SystemClock
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.noahliu.graphqldemo.GetLikerIDQuery
import com.noahliu.graphqldemo.LoginMutation
import com.noahliu.likebalance.Untils.API
import okhttp3.Callback
import okhttp3.OkHttpClient


object Apollo {
    fun sendLogin(email:String, password:String):String{
        val apolloClient = ApolloClient.builder()
            .serverUrl(API.BASE_MATTERS)
            .okHttpClient(
                OkHttpClient().newBuilder()
                .addInterceptor(AuthorizationInterceptor("")).build()).build()
        var res = ""
        apolloClient.mutate(LoginMutation(email, password))
            .enqueue(object : ApolloCall.Callback<LoginMutation.Data>() {
                override fun onResponse(response: Response<LoginMutation.Data>) {
                    res = try {
                        response.data!!.userLogin.token!!
                    }catch (e:Exception){
                        API.ERROR_CODE
                    }

                }
                override fun onFailure(e: ApolloException) {
                    res = API.ERROR_CODE
                }
            })
        while (res.isBlank()){
            SystemClock.sleep(1)
            if (res.isNotEmpty()) break
        }
        return try {
            res
        }catch (e:Exception) {
            API.ERROR_CODE
        }
    }
    fun getViewer(token:String):String{
        val apolloClient = ApolloClient.builder()
            .serverUrl(API.BASE_MATTERS)
            .okHttpClient(
                OkHttpClient().newBuilder()
                    .addInterceptor(AuthorizationInterceptor(token)).build()).build()
        var res = ""
        apolloClient.query(GetLikerIDQuery())
            .enqueue(object : ApolloCall.Callback<GetLikerIDQuery.Data>() {
                override fun onResponse(response: Response<GetLikerIDQuery.Data>) {
                    res = try {
                        response.data!!.viewer!!.likerId!!
                    }catch (e:Exception){
                        API.ERROR_CODE
                    }
                }
                override fun onFailure(e: ApolloException) {
                    res = API.ERROR_CODE
                }
            })
        while (res.isBlank()){
            SystemClock.sleep(1)
            if (res.isNotEmpty()) break
        }
        return try {
            res
        }catch (e:Exception) {
            API.ERROR_CODE
        }
    }

}



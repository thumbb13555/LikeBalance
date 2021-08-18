package com.noahliu.likebalance.Module.Apollo

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(val token:String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("x-access-token",token)
            .build()
        return chain.proceed(request)
    }

}
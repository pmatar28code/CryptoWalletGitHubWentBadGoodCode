package com.example.cryptowallet

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenAuthorizationInterceptor(
    private val authorizationRepository: AccessTokenProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().signedRequest()
        return chain.proceed(newRequest)
    }
    private fun Request.signedRequest(): Request {
        val accessToken = authorizationRepository.token()?.access_token?:""
        Log.e("ACCESS TOKEN INTERCEPTOR", accessToken)
        return newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }
}
package com.example.cryptowallet

import android.util.Log
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenRefreshAuthenticator(
    private val authorizationRepository: AccessTokenProvider
) : Authenticator {

    var FreshAccessToken:AccessToken ?= null

    private val Response.retryCount: Int
        get() {
            var currentResponse = priorResponse
            var result = 0
            while (currentResponse != null) {
                result++
                currentResponse = currentResponse.priorResponse
            }
            return result
        }

    override fun authenticate(route: Route?, response: Response): Request? = when {
        response.retryCount > 2 -> null
        else -> response.createSignedRequest()
    }

    private fun Response.createSignedRequest(): Request? = try {
        //authorizationRepository.refreshToken {
           // FreshAccessToken = it
       // }
        Log.e("AUTHENTICATOR FRESH ACCESTOKEN:","$FreshAccessToken")
        request.signWithToken(FreshAccessToken!!)
    } catch (error: Throwable) {
        Log.e("error", "Failed to re-sign request")
        null
    }

    fun Request.signWithToken(accessToken: AccessToken) =
        newBuilder()
            .header("Authorization", "Bearer ${accessToken.access_token}")
            .build()

}
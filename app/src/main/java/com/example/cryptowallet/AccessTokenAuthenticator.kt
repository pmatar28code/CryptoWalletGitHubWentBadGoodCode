package com.example.cryptowallet

import android.util.Log
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AccessTokenAuthenticator(
    private val tokenProvider: AccessTokenProvider
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // We need to have a token in order to refresh it.
        val token = tokenProvider.token() ?: return null

        synchronized(this) {
            val newToken = tokenProvider.token()
            Log.e("NEW TOKEN AUTHENTICATOR","$newToken")

            // Check if the request made was previously made as an authenticated request.
            if (response.request.header("Authorization") != null) {

                // If the token has changed since the request was made, use the new token.
                if (newToken != token) {
                    Log.e("Testing Authenticator1","Testing1")

                    return response.request
                        .newBuilder()
                        .removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer $newToken")
                        .build()
                }

                val updatedToken = tokenProvider.refreshToken() ?: return null
                Log.e("Testing Authenticator2","Testing2")

                // Retry the request with the new token.
                return response.request
                    .newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $updatedToken")
                    .build()

            }
        }
        return null
    }
}
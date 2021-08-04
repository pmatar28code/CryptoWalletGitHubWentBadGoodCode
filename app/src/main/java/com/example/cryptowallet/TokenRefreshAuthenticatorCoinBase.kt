package com.example.cryptowallet

import android.util.Log
import okhttp3.*

class TokenRefreshAuthenticatorCoinBase(
    private val tokenProvider: AccessTokenProvider
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // We need to have a token in order to refresh it.
        val token = tokenProvider.token() ?: return null

        synchronized(this) {
            val newToken = tokenProvider.token()

            // Check if the request made was previously made as an authenticated request.
            if (response.request.header("Authorization") != null) {

                // If the token has changed since the request was made, use the new token.
                if (newToken != token) {
                    return response.request
                        .newBuilder()
                        .removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer ${newToken?.access_token}")
                        .build()
                }

                var updatedToken:AccessToken ?= null
                    tokenProvider.refreshToken {
                        updatedToken = it
                    }
                Log.e("NEW UPDATED TOKEN ON AUTHENTICATOR CONBASE","$updatedToken")

                // Retry the request with the new token.
                return response.request
                    .newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer ${updatedToken?.access_token}")
                    .build()
            }
        }
        return null
    }
}
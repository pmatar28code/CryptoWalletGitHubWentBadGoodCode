package com.example.cryptowallet

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RefreshNetwork {
    const val MY_CLIENT_ID = "e4faf6ec45843a2f1e8a42c6242f3d8e82ce5603d3ee9c86c85be29a6361104f"
    const val CLIENT_SECRET = "84e2a6a63dc56f5d5be617f77cf71c0c4069a7e3a49cb9e2ce7e5d2bddea007f"
    //private val logger = HttpLoggingInterceptor()
    //   .setLevel(HttpLoggingInterceptor.Level.BODY )
    private val accessTokenProvider = AccessTokenProviderImp()
    private val accessTokenInterceptor = TokenAuthorizationInterceptor(accessTokenProvider)
    val client = OkHttpClient()
        //.addNetworkInterceptor(accessTokenInterceptor)
        //.authenticator(AccessTokenAuthenticator(accessTokenProvider))
        //.build()

        val refreshTokenApi:RefreshTokenApi
            get(){
            return Retrofit.Builder()
                .baseUrl("https://api.coinbase.com/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(RefreshTokenApi::class.java)
        }

    private class RefreshCallBack(
        private val onSuccess:(AccessToken) -> Unit): Callback<AccessToken> {
        override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
            Log.e("ON Response RefreshToken:"," ${response.body()?.access_token}")
            val newRefreshedToken = AccessToken(
                access_token = response.body()?.access_token?:"",
                        expires_in = response.body()?.expires_in?:0,
                        refresh_token = response.body()?.refresh_token?:"",
                        scope = response.body()?.scope?:"",
                        token_type = response.body()?.token_type?:""
            )
            Log.e("RESPONDED WITH:","RefresehedToken: ${newRefreshedToken.access_token}, ${response.isSuccessful}")
            onSuccess(newRefreshedToken)
        }

        override fun onFailure(call: Call<AccessToken>, t: Throwable) {
            Log.e("On Failure Address:","$t")
        }
    }

    fun refreshToken (onSuccess: (AccessToken) -> Unit){
        var refreshToken = AccessTokenProviderImp().token()?.refresh_token?:""
        Log.e("REFRESH NETWORK REFRESh TOKEN FROM Actual TOKEN:","$refreshToken")

        refreshTokenApi.refreshToken("refresh_token", MY_CLIENT_ID, CLIENT_SECRET,refreshToken).enqueue(RefreshCallBack(onSuccess)) //getUser(token).enqueue(AddressCallBack(onSuccess))


    }

}
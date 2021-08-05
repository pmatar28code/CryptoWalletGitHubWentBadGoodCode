package com.example.cryptowallet

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object UserNetwork {
    //private val logger = HttpLoggingInterceptor()
     //   .setLevel(HttpLoggingInterceptor.Level.BODY )
    private val accessTokenProvider = AccessTokenProviderImp()
    private val accessTokenInterceptor = TokenAuthorizationInterceptor(accessTokenProvider)
    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(accessTokenInterceptor)
        .authenticator(TokenRefreshAuthenticatorCoinBase(accessTokenProvider))
        .build()
    private val coinBaseClientApiCalls:CoinBaseClientApiCalls
        get(){
            return Retrofit.Builder()
                .baseUrl("https://api.coinbase.com/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(CoinBaseClientApiCalls::class.java)
        }

    private class UserCallBack(
        private val onSuccess:(UserData.Data) -> Unit): Callback<UserData> {
        override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
            Log.e("ON Response User:"," ${response.body()?.data?.name} ok? ${response.isSuccessful}")
            val newClient = UserData.Data(
                name = response.body()?.data?.name?:"",
                avatarUrl = response.body()?.data?.avatarUrl?:"",
                id = response.body()?.data?.id?:"",
                profileBio = response.body()?.data?.profileBio?:"",
                profileLocation = response.body()?.data?.profileLocation?:"",
                profileUrl = response.body()?.data?.profileUrl ?:"",
                resource = response.body()?.data?.resource?:"",
                resourcePath = response.body()?.data?.resourcePath?:"",
                username = response.body()?.data?.username?:""
            )
            Log.e("RESPONDED WITH:","Client: ${newClient.name},${newClient.id} ${response.isSuccessful}")
            onSuccess(newClient)
        }
        override fun onFailure(call: Call<UserData>, t: Throwable) {
            Log.e("On Failure Address:","$t")
        }
    }
    fun getUser (onSuccess: (UserData.Data) -> Unit){
        val token = accessTokenProvider.token()?.access_token ?:""
        Log.e("ON ACTUAL USER NETWORk CALL TOKEN:", token)

        coinBaseClientApiCalls.getUser("Bearer $token").enqueue(UserCallBack(onSuccess)) //getUser(token).enqueue(AddressCallBack(onSuccess))
    }
}
package com.example.cryptowallet

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ShowAddressNetwork {
    private val accessTokenProvider = AccessTokenProviderImp()
    val client = OkHttpClient.Builder()
        //.addNetworkInterceptor(TokenAuthorizationInterceptor(accessTokenProvider))
        .authenticator(TokenRefreshAuthenticatorCoinBase(accessTokenProvider))
        .build()
    val showAddressApi: ShowAddressApi
        get() {
            return Retrofit.Builder()
                .baseUrl("https://api.coinbase.com/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(ShowAddressApi::class.java)
        }

    private class AddressCallBack(
        private val onSuccess: (ShowAddresses.Data) -> Unit
    ) : Callback<NAddress> {
        override fun onResponse(call: Call<NAddress>, response: Response<NAddress>) {
            Log.e("ON Response Address:", " ${response.body()?.data?.address}")
            val addresses = ShowAddresses.Data(
              address = response.body()?.data?.address?:"",
              createdAt = response.body()?.data?.createdAt?:"",
              id = response.body()?.data?.id?:"",
              name = response.body()?.data?.name?:"",
              network = response.body()?.data?.network?:"",
              resource = response.body()?.data?.resource?:"",
              resourcePath = response.body()?.data?.resourcePath?:"",
              updatedAt = response.body()?.data?.updatedAt?:"",
            )
            Log.e(
                "RESPONDED WITH:",
                "Address: ${addresses.address},${addresses.name} ${response.isSuccessful}"
            )
            onSuccess(addresses)
        }

        override fun onFailure(call: Call<NAddress>, t: Throwable) {
            Log.e("On Failure Address:", "$t")
        }
    }

    fun getAddresses(onSuccess: (ShowAddresses.Data) -> Unit) {
        var token = AccessTokenProviderImp().token()?.access_token ?: ""
        Log.e("On Actual ADDRESS NETWORK TOKEN:", "$token")
        showAddressApi.getAddress("Bearer $token").enqueue(AddressCallBack(onSuccess))
    }
}
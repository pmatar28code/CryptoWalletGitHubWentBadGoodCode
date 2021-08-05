package com.example.cryptowallet

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ListAccountsNetwork {
    private val accessTokenProvider = AccessTokenProviderImp()
    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(TokenAuthorizationInterceptor(accessTokenProvider))
        .authenticator(TokenRefreshAuthenticatorCoinBase(accessTokenProvider))
        .build()
    private val listAccountsApi: ListAccountsApi
        get() {
            return Retrofit.Builder()
                .baseUrl("https://api.coinbase.com/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(ListAccountsApi::class.java)
        }

    private class AccountsCallBack(
        private val onSuccess: (List<ListAccounts.Data>) -> Unit
    ) : Callback<ListAccounts> {
        override fun onResponse(call: Call<ListAccounts>, response: Response<ListAccounts>) {
            val listOfAccounts = mutableListOf<ListAccounts.Data>()
            for(item in response.body()?.data!!){
                listOfAccounts.add(item!!)
            }
            onSuccess(listOfAccounts.toList())

        }
        override fun onFailure(call: Call<ListAccounts>, t: Throwable) {
            Log.e("On Failure LIST ACCOUNTS NETWork:", "This is T : $t")
        }
    }

    fun getAccounts(onSuccess: (List<ListAccounts.Data>) -> Unit) {
        var token = AccessTokenProviderImp().token()?.access_token ?: ""
        Log.e("On Actual ACCOUNTS NETWORK TOKEN:", token)
        listAccountsApi.getAccounts("Bearer $token").enqueue(AccountsCallBack(onSuccess))
    }
}
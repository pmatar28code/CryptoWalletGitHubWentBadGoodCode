package com.example.cryptowallet

import android.util.Log
import com.example.cryptowallet.ShowAddressNetwork.showAddressApi
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ListAccountsNetwork {
    private val accessTokenProvider = AccessTokenProviderImp()
    val client = OkHttpClient.Builder()
        .addNetworkInterceptor(TokenAuthorizationInterceptor(accessTokenProvider))
        .authenticator(TokenRefreshAuthenticator(accessTokenProvider))
        .build()
    val listAccountsApi: ListAccountsApi
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
            //Log.e("ON Response LIST ACCOUNTS :", " ${response.body()?.data}")
            /*
            val accounts = ListAccounts.Data(
                balance= response.body()?.data?.get(1)?.balance,
                createdAt = response.body()?.data?.get(1)?.createdAt,
                currency = response.body()?.data?.get(1)?.currency,
                id = response.body()?.data?.get(1)?.id,
                name = response.body()?.data?.get(1)?.name,
                primary = response.body()?.data?.get(1)?.primary,
                ready = response.body()?.data?.get(1)?.ready,
                resource = response.body()?.data?.get(1)?.resource,
                resourcePath = response.body()?.data?.get(1)?.resourcePath,
                type = response.body()?.data?.get(1)?.type,
                updatedAt = response.body()?.data?.get(1)?.updatedAt
            )
            Log.e(
                "RESPONDED LIST ACCOUNTS WITH:",
                "accounts: ${accounts.id},${accounts.name} ${response.isSuccessful}"
            )
            onSuccess(accounts)
            */
            var listOfAccounts = mutableListOf<ListAccounts.Data>()
            //response.body()?.data?.map { it to listOfAccounts!! }
            for(item in response.body()?.data!!){
                listOfAccounts.add(item!!)
            }
            onSuccess(listOfAccounts.toList())

        }

        override fun onFailure(call: Call<ListAccounts>, t: Throwable) {
            Log.e("On Failure LIST ACOUNTS NETWork:", "This is T : $t")
        }
    }

    fun getAccounts(onSuccess: (List<ListAccounts.Data>) -> Unit) {
        var token = AccessTokenProviderImp().token()?.access_token ?: ""
        Log.e("On Actual ACCOUNTS NETWORK TOKEN:", "$token")
        listAccountsApi.getAccounts("Bearer $token").enqueue(AccountsCallBack(onSuccess))
    }
}
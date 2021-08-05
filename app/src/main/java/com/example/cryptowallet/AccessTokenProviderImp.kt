package com.example.cryptowallet

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AccessTokenProviderImp :AccessTokenProvider {
    var token: AccessTokenDCLass?=null
    var newAccessToken:AccessTokenDCLass?=null

    override fun token(): AccessTokenDCLass? {
        runBlocking {
            val job: Job = launch(IO) {
                val database = MainActivity.ROOM_DATABASE.AccessTokenDao()
                val listOfTokens = database.getAllTokens()
                token = listOfTokens[0]
            }
        }
        /*
        if (newAccessToken == null){
            token?.access_token = "1"
        Log.e("TOKEN ACCESS TOKEN PROV Forced to 1", "$token")
        return token
    }else {
            Log.e("TOKEN ACCESS TOKEN PROV NOOOTT FORCED", "$token")
            return token
        }
        */
        Log.e("RETURNED TOKEN FUN TOKEN IMP:","$token")
        return token
    }

    override fun refreshToken(refreshCallback: (Boolean) -> Unit) {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://api.coinbase.com/")
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = retrofitBuilder.build()
        val refreshClient = retrofit.create(RefreshTokenApi::class.java)
        val refreshTokenCall = AccessTokenProviderImp().token()?.refresh_token?.let {
            refreshClient.refreshToken(
                "refresh_token",
                MainActivity.MY_CLIENT_ID,
                MainActivity.CLIENT_SECRET,
                it
            )
        }
        refreshTokenCall?.enqueue(object: Callback<AccessToken>{
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                Log.e("GOOD RESPONSE IMP:", "TOKEN: ${response.body()?.access_token}")
                newAccessToken = AccessTokenDCLass(
                    access_token = response.body()?.access_token ?: "",
                    expires_in = response.body()?.expires_in ?: 0,
                    refresh_token = response.body()?.refresh_token ?: "",
                    scope = response.body()?.scope ?: "",
                    token_type = response.body()?.token_type ?: ""
                )
                if (newAccessToken!!.access_token != "" && newAccessToken != null){
                    runBlocking {
                        val job: Job = launch(IO) {
                            val database = MainActivity.ROOM_DATABASE.AccessTokenDao()
                            database.deleteAllTokens()
                            Log.e("NEW ACCESS TOKen ADDED TO DATABASE FROM IMP", "$newAccessToken")
                            database.addToken(newAccessToken!!)
                            refreshCallback(true)
                            joinAll()
                        }
                    }
                }else{
                    refreshCallback(false)
                }
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                Log.e("ON FAILURE ReFReSH IMP:","$t")
                refreshCallback(false)
            }
        })
    }

    private suspend fun getTokenDatabase(tokenCallBack:(AccessTokenDCLass)-> Unit) {
        val database = MainActivity.ROOM_DATABASE
        val listTokens = database.AccessTokenDao().getAllTokens()
        tokenCallBack(listTokens[0])
    }

    private suspend fun deleteActualToken() {
        var actualToken:AccessTokenDCLass ?=null
            getTokenDatabase {
                actualToken = it
            }
            Log.e("Actual token for delete", "$actualToken")
        val database = MainActivity.ROOM_DATABASE
            database.AccessTokenDao().removeToken(actualToken?.key)
    }

    private suspend fun addNewToken(newToken:AccessTokenDCLass){
        val database = MainActivity.ROOM_DATABASE

        database.AccessTokenDao().addToken(newToken)
        Log.e("NEW TOKEN ADDED IMP","$newToken")
    }

    private suspend fun deleteAllTokens(){
        val database = MainActivity.ROOM_DATABASE.AccessTokenDao()
        database.deleteAllTokens()
    }
}
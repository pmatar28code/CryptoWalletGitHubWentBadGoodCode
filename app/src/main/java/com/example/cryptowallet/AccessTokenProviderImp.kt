package com.example.cryptowallet

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class AccessTokenProviderImp :AccessTokenProvider {

    var token: AccessTokenDCLass?=null

    override fun token(): AccessTokenDCLass? {
        runBlocking {
            val job:Job = launch(IO){
                getTokenDatabase {
                    token = it
                }
            }
        }
        //CoroutineScope(IO).launch {
           // getTokenDatabase {
             //   token = it
           // }
        //}
        Log.e("TOKEN ACCESS TOKEN PROV", "$token")
        return token
    }

    override fun refreshToken(refreshCallBack: (AccessToken) -> Unit) {
        RefreshNetwork.refreshToken {
            var newAccessToken = AccessTokenDCLass(
                access_token = it.access_token,
                expires_in = it.expires_in,
                refresh_token = it.refresh_token,
                scope = it.scope,
                token_type = it.token_type
            )
            runBlocking {
                val job: Job = launch(IO){
                    if(newAccessToken.access_token !=""){
                        deleteActualToken()
                        addNewToken(newAccessToken)
                    }
                    joinAll()
                }
            }
            //CoroutineScope(IO).launch {
             //   if(newAccessToken.access_token !=""){
                //    deleteActualToken()
                //    addNewToken(newAccessToken)
                //}
            //}
            Log.e("NEWLY REFRESHED TOKEN IMP","$newAccessToken")
            refreshCallBack(it)
        }
    }

    private suspend fun getTokenDatabase(tokenCallBack:(AccessTokenDCLass)-> Unit) {
        var database = MainActivity.ROOM_DATABASE
        var listTokens = database.AccessTokenDao().getAllTokens()
        tokenCallBack(listTokens[0])
    }

    private suspend fun deleteActualToken() {
        var actualToken:AccessTokenDCLass ?=null
            getTokenDatabase {
                actualToken = it
            }
            Log.e("Actual token for delete", "$actualToken")
        var database = MainActivity.ROOM_DATABASE
            database.AccessTokenDao().removeToken(actualToken?.key)
    }

    private suspend fun addNewToken(newToken:AccessTokenDCLass){
        var database = MainActivity.ROOM_DATABASE

        database.AccessTokenDao().addToken(newToken)
        Log.e("NEW TOKEN ADDED IMP","$newToken")
    }
}
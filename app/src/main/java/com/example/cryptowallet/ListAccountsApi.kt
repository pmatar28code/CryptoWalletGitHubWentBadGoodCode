package com.example.cryptowallet

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ListAccountsApi {
    @GET("v2/accounts/")
    fun getAccounts(@Header("Authorization") token:String
    ):Call<ListAccounts>
}
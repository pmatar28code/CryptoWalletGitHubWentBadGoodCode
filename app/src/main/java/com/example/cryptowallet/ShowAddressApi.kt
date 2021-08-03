package com.example.cryptowallet

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ShowAddressApi {

    @GET("v2/accounts/68c41609-6b0f-5209-a655-e9a81ddd91d2/addresses")
    fun getAddress(@Header("Authorization") token:String
        //@Field("grant_type")grant_type:String,
        //@Field("bearer")bearer:String
    ): Call<NAddress>
}
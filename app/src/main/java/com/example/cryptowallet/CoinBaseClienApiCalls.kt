package com.example.cryptowallet

import retrofit2.Call
import retrofit2.http.*

interface CoinBaseClienApiCalls {
    //@Headers("Accept: application/json")
   // @POST("v2/user")
    //@FormUrlEncoded
    @GET("v2/user")
    fun getUser(@Header("Authorization") token:String
        //@Field("grant_type")grant_type:String,
        //@Field("bearer")bearer:String
    ):Call<UserData>
}
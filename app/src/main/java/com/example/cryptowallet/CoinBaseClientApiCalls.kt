package com.example.cryptowallet

import retrofit2.Call
import retrofit2.http.*

interface CoinBaseClientApiCalls {
    @GET("v2/user/")
    fun getUser(@Header("Authorization") token:String
    ):Call<UserData>
}
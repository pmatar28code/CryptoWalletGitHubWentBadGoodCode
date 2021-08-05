package com.example.cryptowallet

import retrofit2.Call
import retrofit2.http.*

interface RefreshTokenApi {
    @Headers("Accept: application/json")
    @POST("oauth/token")
    @FormUrlEncoded
    fun refreshToken(
        @Field("grant_type") refresh_token:String,
        @Field("client_id") YOUR_CLIENT_ID:String,
        @Field("client_secret")YOUR_CLIENT_SECRET:String,
        @Field("refresh_token")REFRESH_TOKEN:String
    ): Call<AccessToken>
}
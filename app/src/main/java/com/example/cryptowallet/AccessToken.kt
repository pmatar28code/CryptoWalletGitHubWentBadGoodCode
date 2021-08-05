package com.example.cryptowallet

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessToken (
    @Json(name= "access_token")
    val access_token:String,

    @Json(name= "token_type")
    val token_type :String,

    @Json(name = "expires_in")
    val expires_in :Int,

    @Json(name= "refresh_token")
    val refresh_token :String,

    @Json(name ="scope")
    val scope :String
)




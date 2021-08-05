package com.example.cryptowallet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AccessToken (

    @SerializedName("access_token")
    @Expose
    val access_token:String,

    @SerializedName("token_type")
    @Expose
    val token_type :String,

    @SerializedName("expires_in")
    @Expose
    val expires_in :Int,

    @SerializedName("refresh_token")
    @Expose
    val refresh_token :String,

    @SerializedName("scope")
    @Expose
    val scope :String
)




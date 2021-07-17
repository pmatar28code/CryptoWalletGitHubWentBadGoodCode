package com.example.cryptowallet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserData(

    @SerializedName("access_token")
    @Expose
    val access_token:String,
    @SerializedName("id")
    @Expose
    val id:String,
    @SerializedName("name")
    val name:String,
    @SerializedName("username")
    @Expose
    val username:String,
    @SerializedName("profile_location")
    @Expose
    val profile_location:String?, //says null
    @SerializedName("profile_bio")
    val profile_bio: String?,
    @SerializedName("profile_url")
    @Expose
    val profile_url:String,
    @SerializedName("avatar_url")
    @Expose
    val avatar_url:String,
    @SerializedName("resource")
    @Expose
    val resource:String,
    @SerializedName("resource_path")
    @Expose
    val resource_path:String

)

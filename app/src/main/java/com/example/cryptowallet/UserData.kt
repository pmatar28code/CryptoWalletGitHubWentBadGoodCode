package com.example.cryptowallet

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("data")
    val data: Data?
) {
    data class Data(
        @SerializedName("avatar_url")
        val avatarUrl: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("profile_bio")
        val profileBio: Any?,
        @SerializedName("profile_location")
        val profileLocation: Any?,
        @SerializedName("profile_url")
        val profileUrl: String?,
        @SerializedName("resource")
        val resource: String?,
        @SerializedName("resource_path")
        val resourcePath: String?,
        @SerializedName("username")
        val username: String?
    )
}
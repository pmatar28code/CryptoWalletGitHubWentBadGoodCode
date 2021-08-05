package com.example.cryptowallet

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserData(
    @Json(name = "data") val data: Data?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "avatarUrl")
        val avatarUrl: String?,
        @Json(name ="id")
        val id: String?,
        @Json(name = "name")
        val name: String?,
        @Json(name = "profile_bio")
        val profileBio: Any?,
        @Json(name = "profile_location")
        val profileLocation: Any?,
        @Json(name = "profile_url")
        val profileUrl: String?,
        @Json(name = "resource")
        val resource: String?,
        @Json(name = "resource_path")
        val resourcePath: String?,
        @Json(name = "username")
        val username: String?
    )
}
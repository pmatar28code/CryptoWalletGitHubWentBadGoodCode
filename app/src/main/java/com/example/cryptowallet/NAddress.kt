package com.example.cryptowallet

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
    data class NAddress(
        @Json(name = "data")
        val data: Data?
    ) {
        @JsonClass(generateAdapter = true)
        data class Data(
            @Json(name = "id")
            val id: String?,
            @Json(name = "address")
            val address: String?,
            @Json(name = "name")
            val name: String?,
            @Json(name = "created_at")
            val createdAt: String?,
            @Json(name = "updated_at")
            val updatedAt: String?,
            @Json(name = "network")
            val network: String?,
            @Json(name = "resource")
            val resource: String?,
            @Json(name = "resource_path")
            val resourcePath: String?
        )
    }

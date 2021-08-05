package com.example.cryptowallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListAccounts(
    @Json(name = "data")
    val data: List<Data?>?,
    @Json(name = "pagination")
    val pagination: Pagination?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "balance")
        val balance: Balance?,
        @Json(name = "created_at")
        val createdAt: String?,
        @Json(name = "currency")
        val currency: Balance?,
        @Json(name = "id")
        val id: String?,
        @Json(name = "name")
        val name: String?,
        @Json(name = "primary")
        val primary: Boolean?,
        @Json(name = "ready")
        val ready: Boolean?,
        @Json(name = "resource")
        val resource: String?,
        @Json(name = "resource_path")
        val resourcePath: String?,
        @Json(name = "type")
        val type: String?,
        @Json(name = "updated_at")
        val updatedAt: String?
    ) {
        @JsonClass(generateAdapter = true)
        data class Balance(
            @Json(name = "amount")
            val amount: String?,
            @Json(name = "currency")
            val currency: String?
        )
    }
    @JsonClass(generateAdapter = true)
    data class Pagination(
        @Json(name = "ending_before")
        val endingBefore: Any?,
        @Json(name = "limit")
        val limit: Int?,
        @Json(name = "next_uri")
        val nextUri: Any?,
        @Json(name = "order")
        val order: String?,
        @Json(name = "previous_uri")
        val previousUri: Any?,
        @Json(name = "starting_after")
        val startingAfter: Any?
    )
}
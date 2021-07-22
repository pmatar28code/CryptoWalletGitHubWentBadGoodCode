package com.example.cryptowallet

import retrofit2.Call
import retrofit2.http.POST

interface AddressApi {
    companion object{
        val userId =Repository.userId
        const val postValue = "v2/accounts/\$userId/addresses"
    }

    @POST(postValue)
    fun getAddress():Call<NAddress>
}
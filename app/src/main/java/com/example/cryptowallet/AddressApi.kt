package com.example.cryptowallet

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


    interface AddressApi {

        companion object{
            val myClientId = "e4faf6ec45843a2f1e8a42c6242f3d8e82ce5603d3ee9c86c85be29a6361104f"
            val userId =Repository.userId
            var accountId = Repository.accountId
            var clientId = "e4faf6ec45843a2f1e8a42c6242f3d8e82ce5603d3ee9c86c85be29a6361104f"
            const val postValue = "v2/accounts/0b125b64-619e-5cf8-a719-963534b52bca/addresses"
        }

       // @Headers("Accept: application/json")
        @POST(postValue)
        //@FormUrlEncoded
        fun getAddress(
            @Header("Authorization") token:String): Call<NAddress>
        //fun getAddress(
        //@Field("Content-Type") application:String,
        // @Field("Authorization") token:String
        //):Call<NAddress>
    }

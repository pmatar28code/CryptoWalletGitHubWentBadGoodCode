package com.example.cryptowallet

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object AddressNetwork {
    val userId = Repository.userId
    val client = OkHttpClient()
    val addressApi:AddressApi
    get(){
        return Retrofit.Builder()
            .baseUrl("https://api.coinbase.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AddressApi::class.java)
    }

    private class AddressCallBack(
        private val onSuccess:(NAddress.Data) -> Unit):Callback<NAddress> {
        override fun onResponse(call: Call<NAddress>, response: Response<NAddress>) {
            Log.e("ON Response Address:"," ${response.body()?.data?.address}")
            val newNAddress = NAddress.Data(
                address = response.body()?.data?.address,
                createdAt = response.body()?.data?.address,
                id = response.body()?.data?.id,
                name = response.body()?.data?.name,
                network = response.body()?.data?.network,
                resource = response.body()?.data?.resource,
                resourcePath = response.body()?.data?.resourcePath,
                updatedAt = response.body()?.data?.updatedAt
            )
            Log.e("RESPONDED WITH:","Address: ${newNAddress.address},${newNAddress.name}")
            onSuccess(newNAddress)
        }

        override fun onFailure(call: Call<NAddress>, t: Throwable) {
            Log.e("On Failure Address:","$t")
        }
    }

    fun getAddresses (onSuccess: (NAddress.Data) -> Unit){
        var token = " Bearer ${Repository.lastToken}"
        addressApi.getAddress(Repository.lastToken).enqueue(AddressCallBack(onSuccess))
    }


}
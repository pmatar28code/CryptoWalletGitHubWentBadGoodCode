package com.example.cryptowallet

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.cryptowallet.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    companion object {
        const val MY_CLIENT_ID = "e4faf6ec45843a2f1e8a42c6242f3d8e82ce5603d3ee9c86c85be29a6361104f"
        const val CLIENT_SECRET = "84e2a6a63dc56f5d5be617f77cf71c0c4069a7e3a49cb9e2ce7e5d2bddea007f"
        const val MY_REDIRECT_URI = "cryptowallet://callback"
    }
    var accessToken:AccessTokenDCLass?=null
    var database:CoinBaseDatabase ?= null
    //var tokenList:List<AccessTokenDCLass> ?=null
    var token:String ?= ""
    var testingCodeList:List<JustCode>?=null
    var testingTokenList:List<AccessTokenDCLass>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        val binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)

        database = CoinBaseDatabase.getInstance(applicationContext)

        CoroutineScope(IO).launch {
            testingCodeList = getCode()
            testingTokenList = getAllTokens()

            if (testingCodeList!!.isEmpty()) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.coinbase.com/oauth/authorize?client_id=e4faf6ec45843a2f1e8a42c6242f3d8e82ce5603d3ee9c86c85be29a6361104f&redirect_uri=cryptowallet%3A%2F%2Fcallback&response_type=code&scope=wallet%3Aaddresses%3Acreate") //&scope=wallet%3Auser%3Aread
                )
                startActivity(intent)
                Log.e("FIRST Run", "getting the code")
            } else {
                Log.e("WHATS NEXT", "DO API Requests WITH TOKEN AVAILABLE CODE:${testingCodeList?.get(0)}, Token:${testingTokenList?.get(0)}")

                val retrofitBuilder = Retrofit.Builder()
                    .baseUrl("https://api.coinbase.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                val retrofit = retrofitBuilder.build()
                val coinBaseClientForApi = retrofit.create(CoinBaseClienApiCalls::class.java)
                val accessToken = testingTokenList?.get(0)?.access_token!!
                Repository.lastToken = accessToken
                Log.e("ACCESS TOKEN LOG","this: $accessToken")
                val accessCall = coinBaseClientForApi.getUser(
                    " Bearer $accessToken"
                )


                //from here
/*
                val retrofitBuilderAddress = Retrofit.Builder()
                    .baseUrl("https://api.coinbase.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                val retrofitAddress = retrofitBuilderAddress.build()
                val coinBaseAddressApi = retrofitAddress.create(AddressApi::class.java)
                val addressCall = coinBaseAddressApi.getAddress(" Bearer $accessToken")
                addressCall.enqueue(object: Callback<NAddress>{
                    override fun onResponse(
                        call: Call<NAddress>,
                        response: Response<NAddress>
                    ) {
                        Log.e(" Good REsp ADDRESS PM","${response.body()?.data?.address} - ${response.isSuccessful}")
                    }

                    override fun onFailure(call: Call<NAddress>, t: Throwable) {
                        Log.e("address failure conexion","$t")
                    }

                })

 */
                //to here

                accessCall.enqueue(object: Callback<UserData>{
                    override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                        Log.e("Testing Api Call good response"," response: ${response.body()?.data?.name}")
                        Repository.userId = response.body()?.data?.id!!
                        Log.e("USER ID FIRST","${Repository.userId}")
                        //"68c41609-6b0f-5209-a655-e9a81ddd91d2"
                        val refreshToken = testingTokenList?.get(0)?.refresh_token!!
                        Log.e("THiS IS THE REFRESH TOKEN TO GET NEW","$refreshToken")
                        val retrofitBuilderRefresh = Retrofit.Builder()
                            .baseUrl("https://api.coinbase.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                        val retrofitRefresh = retrofitBuilderRefresh.build()
                        val refreshApi = retrofitRefresh.create(RefreshApi::class.java)
                        val refreshCall = refreshApi.refreshToken(
                            "refresh_token",
                            MY_CLIENT_ID,
                            CLIENT_SECRET,
                            refreshToken
                            )

                        refreshCall?.enqueue(object: Callback<AccessToken>{
                            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>
                            ){
                                Log.e("RESPOND REFRESH OK","Refresehd token: ${response.body()?.access_token} ok? ${response.isSuccessful}")
                                val refreshedAccessToken = response.body()?.access_token
                                Log.e("THIS REFRESH TOKEN:","$refreshedAccessToken")
                                Repository.lastToken = refreshedAccessToken!!
                                Log.e("LAST TOKEN REPO","${Repository.lastToken}")

                                AddressNetwork.getAddresses {
                                    Repository.nAddressFromResponse = it
                                    Log.e("REPOSITORY ADRESS:",
                                        Repository.nAddressFromResponse?.address?:"no address"
                                    )
                                }

                                //Repository.lastToken = refreshedAccessToken!!
/*
                                val retrofitBuilderAddress = Retrofit.Builder()
                                    .baseUrl("https://api.coinbase.com/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                val retrofitAddress = retrofitBuilderAddress.build()
                                val coinBaseAddressApi = retrofitAddress.create(AddressApi::class.java)
                                val addressCall = coinBaseAddressApi.getAddress(" Bearer $refreshedAccessToken")
                                addressCall.enqueue(object: Callback<NAddress>{
                                    override fun onResponse(call: Call<NAddress>, response: Response<NAddress>
                                    ) {
                                        Log.e(" Good REsp ADDRESS PM","${response.body()?.data?.address} - ${response.isSuccessful}")
                                    }

                                    override fun onFailure(call: Call<NAddress>, t: Throwable) {
                                        Log.e("address failure conexion","$t")
                                    }

                                })
                                GG*/

                            }

                            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                            }

                        })
/* good
                        val retrofitBuilderAddress = Retrofit.Builder()
                            .baseUrl("https://api.coinbase.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                        val retrofitAddress = retrofitBuilderAddress.build()
                        val coinBaseAddressApi = retrofitAddress.create(AddressApi::class.java)
                        val addressCall = coinBaseAddressApi.getAddress(" Bearer $accessToken")
                        addressCall.enqueue(object: Callback<NAddress>{
                            override fun onResponse(
                                call: Call<NAddress>,
                                response: Response<NAddress>
                            ) {
                                Log.e(" Good REsp ADDRESS PM","${response.body()?.data?.address} - ${response.isSuccessful}")
                            }

                            override fun onFailure(call: Call<NAddress>, t: Throwable) {
                                Log.e("address failure conexion","$t")
                            }

                        })
*/

                        //AddressNetwork.getAddresses {
                        //    Repository.nAddressFromResponse = it
                        //    Log.e("REPOSITORY ADRESS:",
                        //        Repository.nAddressFromResponse?.address?:"no address"
                        //    )
                        //}
                    }

                    override fun onFailure(call: Call<UserData>, t: Throwable) {
                        Log.e("Failed","conexion failed $t")
                    }
                })

            }
        }
    }

    override fun onResume() {
        super.onResume()
        val uri = intent.data
        if(uri != null){
            val code = uri.getQueryParameter("code")!!
            CoroutineScope(IO).launch {
                addCode(code)
                var testCodeList2 = getCode()
                Log.e("ADDING CODE","ADDED CODE TO DATABASE $testCodeList2")
            }
            //cryptowallet://callback?code=e260bd24659d7ee7c88acf1550839ded1a90b159d4cb37d24fd208df4a4222fb
            val retrofitBuilder = Retrofit.Builder()
                .baseUrl("https://api.coinbase.com/")
                .addConverterFactory(GsonConverterFactory.create())
            val retrofit = retrofitBuilder.build()
            val coinBaseClient = retrofit.create(CoinBaseClient::class.java)
            val accessTokenCall = coinBaseClient.getToken(
                "authorization_code",
                code,
                MY_CLIENT_ID,
                CLIENT_SECRET,
                MY_REDIRECT_URI
            )
            accessTokenCall.enqueue(object: Callback<AccessToken> {
                override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                    Toast.makeText(this@MainActivity,"good response: ${response.body()?.access_token}",Toast.LENGTH_SHORT).show()
                    accessToken= AccessTokenDCLass(
                        access_token =  response.body()?.access_token!!,
                        token_type =  response.body()?.token_type!!,
                        expires_in = response.body()?.expires_in!!,
                        refresh_token = response.body()?.refresh_token!!,
                        scope =  response.body()?.scope!!
                    )
                    if(accessToken != null) {
                        addToken(accessToken!!)
                        Log.e("ADDED TOKEN TO DATABASE","ACCESS TOKEN ADDED TO DATABASE $accessToken")
                        val intent =Intent(this@MainActivity, MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        Log.e("TOKEN IS NULL","ACCESS TOKEN IS NULL $accessToken")
                    }

                }
                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                    Toast.makeText(this@MainActivity,"bad response",Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun addToken(token:AccessTokenDCLass){
        AsyncTask.execute {
            database?.AccessTokenDao()?.addToken(token)
        }
    }

    private suspend fun getAllTokens():List<AccessTokenDCLass>{
        var tokenList = database?.AccessTokenDao()?.getAllTokens()!!
        if(tokenList.isEmpty()){
            return emptyList()
        }else{
            return tokenList
        }
    }

    private fun authorizationRequest(callback: (String) -> Unit){
        if (intent.data == null) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.coinbase.com/oauth/authorize?client_id=e4faf6ec45843a2f1e8a42c6242f3d8e82ce5603d3ee9c86c85be29a6361104f&redirect_uri=cryptowallet%3A%2F%2Fcallback&response_type=code&scope=wallet%3Auser%3Aread")
            )
            startActivity(intent)
            var code = intent.data?.getQueryParameter("code")
            callback(code!!)
        }
    }

    suspend fun getCode():List<JustCode>{
        var codeList = database?.JustCodeDao()?.getAllCodes()
        return codeList!!
    }

    suspend fun addCode(code:String){
        var justCode = JustCode(code = code)
        database?.JustCodeDao()?.addCode(justCode)
    }
}

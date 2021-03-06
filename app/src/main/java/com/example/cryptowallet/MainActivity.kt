package com.example.cryptowallet

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptowallet.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    companion object {
        const val MY_CLIENT_ID = "e4faf6ec45843a2f1e8a42c6242f3d8e82ce5603d3ee9c86c85be29a6361104f"
        const val CLIENT_SECRET = "84e2a6a63dc56f5d5be617f77cf71c0c4069a7e3a49cb9e2ce7e5d2bddea007f"
        const val MY_REDIRECT_URI = "cryptowallet://callback"
        lateinit var ROOM_DATABASE:CoinBaseDatabase
    }
    var accessToken:AccessTokenDCLass?=null
    var database:CoinBaseDatabase ?= null
    var token:String ?= ""
    var testingCodeList:List<JustCode>?=null
    var testingTokenList:List<AccessTokenDCLass>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        val binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)

        database = CoinBaseDatabase.getInstance(applicationContext)
        ROOM_DATABASE = database as CoinBaseDatabase
        runBlocking {
            val job:Job = launch(IO) {
                testingCodeList = database?.JustCodeDao()?.getAllCodes()
                testingTokenList = database?.AccessTokenDao()?.getAllTokens()
                joinAll()
            }
        }


        if (testingCodeList!!.isEmpty()) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.coinbase.com/oauth/authorize?client_id=e4faf6ec45843a2f1e8a42c6242f3d8e82ce5603d3ee9c86c85be29a6361104f&redirect_uri=cryptowallet%3A%2F%2Fcallback&response_type=code&scope=wallet%3Aaccounts%3Aread+wallet%3Aaddresses%3Acreate")
            )
            startActivity(intent)
            Log.e("FIRST Run", "getting the code")
        } else {
            Repository.accessToken = testingTokenList!![0]
            Log.e(
                "WHATS NEXT",
                "DO API Requests WITH TOKEN AVAILABLE CODE:${testingCodeList?.get(0)?.code}, Token:${
                    testingTokenList?.get(0)?.access_token
                }"
            )
            UserNetwork.getUser {
                runBlocking {
                    var job:Job = launch(IO) {
                        val token = database?.AccessTokenDao()?.getAllTokens()?.get(0)
                        Log.e("SHOWING USER", "${it.name}, id: ${it.id} WITH TOKEN = ${token?.access_token}")
                        Repository.userId = it.id.toString()
                    }
                }
            }
            ListAccountsNetwork.getAccounts {
                Repository.accountId = it[0].id?:""
                runBlocking {
                    var job:Job = launch(IO) {
                        var token = database?.AccessTokenDao()?.getAllTokens()?.get(0)
                        Log.e(
                            "LIST OF ACCOUNTS MAIN OJO: ",
                            "ID: ${it[0].id}, ${it[0].name}, ${it[0].balance}, ${it[0].currency} WITH TOKEN = ${token?.access_token}"
                        )
                    }
                }
            }
            AddressNetwork.getAddresses {
                runBlocking {
                    var job: Job = launch(IO) {
                        var token = database?.AccessTokenDao()?.getAllTokens()?.get(0)
                        Log.e(
                            "CREATE ADDRESS MAIN: ",
                            "${it.address} , ${it.name} , ${it.createdAt} , WITH TOKEN = ${token?.access_token}"
                        )
                    }
                }
            }

        //AccessTokenProviderImp().refreshToken {
        //  Log.e("USING IMP ON MAIN FOR REFRESH","${it.access_token}")
        // }

        // runBlocking {
        //  var job:Job = launch(IO){
        //    revokeToken()
        // joinAll()
        // }
        //}
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
                    var accessTokenRegular = AccessToken(
                        access_token =  response.body()?.access_token?:"",
                        token_type =  response.body()?.token_type?:"",
                        expires_in = response.body()?.expires_in?:0,
                        refresh_token = response.body()?.refresh_token?:"",
                        scope =  response.body()?.scope?:""
                    )
                    accessToken= AccessTokenDCLass(
                        access_token =  response.body()?.access_token?:"",
                        token_type =  response.body()?.token_type?:"",
                        expires_in = response.body()?.expires_in?:0,
                        refresh_token = response.body()?.refresh_token?:"",
                        scope =  response.body()?.scope?:""
                    )
                    if(accessToken != null) {
                        //CoroutineScope(IO).launch{
                          //  deleteActualToken()
                        //}
                        addToken(accessToken!!)
                        Repository.accessToken = accessToken
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

    private suspend fun deleteActualToken() {
        var actualToken:AccessTokenDCLass ?=null
        getTokenDatabase {
            actualToken = it
        }
        Log.e("Actual token for delete", "$actualToken")
        var database = MainActivity.ROOM_DATABASE
        database.AccessTokenDao().removeToken(actualToken?.key)
    }

    private suspend fun getTokenDatabase(tokenCallBack:(AccessTokenDCLass)-> Unit) {
        var database = MainActivity.ROOM_DATABASE
        var listTokens = database.AccessTokenDao().getAllTokens()
        tokenCallBack(listTokens[0])
    }

    private suspend fun populateCodeList(codeListCallBack:(List<JustCode>)->Unit){
        val codeListFormDatabase = database?.JustCodeDao()
        codeListFormDatabase?.getAllCodes()?.let { codeListCallBack(it) }

    }

    private suspend fun populateTokenList(tokenListCallBack:(List<AccessTokenDCLass>)->Unit){
        val tokenListFormDatabase = database?.AccessTokenDao()
        tokenListFormDatabase?.getAllTokens()?.let { tokenListCallBack(it) }
    }

    private fun revokeToken(){
        val databaseRevoke =  database?.AccessTokenDao()?.getAllTokens()?.get(0)?.refresh_token!!
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://api.coinbase.com/")
            .addConverterFactory(MoshiConverterFactory.create())
        val retrofit = retrofitBuilder.build()
        val revokeTokenClient = retrofit.create(RevokeTokenApi::class.java)
        val revokeCall = revokeTokenClient.revokeToken(
            databaseRevoke,
            "Bearer $databaseRevoke "
        )
        revokeCall.enqueue(object:Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.e("IS IT REVOKED MAIN ON RESPONSE","${response.body()}")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ON FAILURE REVOKE","$t")
            }

        })

    }
}

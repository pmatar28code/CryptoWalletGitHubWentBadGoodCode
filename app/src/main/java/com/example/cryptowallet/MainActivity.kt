package com.example.cryptowallet

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri

import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.AuthorizationServiceConfiguration.RetrieveConfigurationCallback
import net.openid.appauth.ResponseTypeValues

import net.openid.appauth.AuthorizationRequest
import android.content.Intent

import android.app.PendingIntent
import android.os.PersistableBundle
import android.widget.Button

import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationException

import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService.TokenResponseCallback
import net.openid.appauth.AuthState
import net.openid.appauth.AuthState.AuthStateAction
import androidx.core.app.ActivityCompat.startActivityForResult
import net.openid.appauth.TokenResponse








class MainActivity : AppCompatActivity() {
    companion object{
        const val MY_CLIENT_ID = "e4faf6ec45843a2f1e8a42c6242f3d8e82ce5603d3ee9c86c85be29a6361104f"
        val MY_REDIRECT_URI = "com.example.cryptowallet://coinbase-oauth".toUri()
    }
    lateinit var serviceConfig:AuthorizationServiceConfiguration
    lateinit var authService:AuthorizationService
    lateinit var authState: AuthState
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse("https://www.coinbase.com/oauth/authorize"),  // authorization endpoint
            Uri.parse("http://www.coinbase.com/oauth/token")
        ) // token endpoint

/*
        AuthorizationServiceConfiguration.fetchFromIssuer(
            Uri.parse("https://www.coinbase.com/oauth/authorize?client_id=e4faf6ec45843a2f1e8a42c6242f3d8e82ce5603d3ee9c86c85be29a6361104f&redirect_uri=cryptoWallet%3A%2F%2Fcoinbase-oauth&response_type=code&scope=wallet%3Auser%3Aread"),
            RetrieveConfigurationCallback { serviceConfiguration, ex ->
                if (ex != null) {
                    Log.e("AUTH SERVICE FAILED", "failed to fetch configuration")
                    serviceConfig = serviceConfiguration!!
                    return@RetrieveConfigurationCallback
                }

                // use serviceConfiguration as needed

            })

 */

        //if(serviceConfig != null) {
            authState = AuthState(serviceConfig)
        //}

        findViewById<Button>(R.id.button).setOnClickListener {
            val authRequestBuilder = AuthorizationRequest.Builder(
                serviceConfig,  // the authorization service configuration
                MY_CLIENT_ID,  // the client ID, typically pre-registered and static
                ResponseTypeValues.CODE,  // the response_type value: we want a code
                MY_REDIRECT_URI
            ) // the redirect URI to which the auth response is sent

            val authRequest = authRequestBuilder
                .setScope("wallet:user:read wallet:accounts:read")
                .setLoginHint("jdoe@user.example.com")
                .build()

            authService = AuthorizationService(this)

            //authService.performAuthorizationRequest(
            //  authRequest,
            //  PendingIntent.getActivity(this, 0, Intent(this, Test::class.java), 0),
            //PendingIntent.getActivity(this, 0, Intent(this, Test::class.java), 0)
            // )
            doAuthorization(authRequest, 100)





        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val resp = AuthorizationResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)
        if (resp != null) {
            Log.e("Test","$resp")
            // authorization completed
        } else {
            Log.e("Test2","${ex?.cause}")
            // authorization failed, check ex for more details
        }
        // ...
    }
    private fun doAuthorization(authRequest:AuthorizationRequest,RC_AUTH:Int) {
        val authService = AuthorizationService(this)
        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        startActivityForResult(authIntent, RC_AUTH)

    }

}
package com.example.cryptowallet

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.cryptowallet.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    companion object {
        const val MY_CLIENT_ID = "e4faf6ec45843a2f1e8a42c6242f3d8e82ce5603d3ee9c86c85be29a6361104f"
        val MY_REDIRECT_URI = "cryptowallet://callback"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        val binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.coinbase.com/oauth/authorize?client_id=e4faf6ec45843a2f1e8a42c6242f3d8e82ce5603d3ee9c86c85be29a6361104f&redirect_uri=cryptowallet%3A%2F%2Fcallback&response_type=code&scope=wallet%3Auser%3Aread")
        )
        startActivity(intent)

    }

    override fun onResume() {
        super.onResume()
        val uri = intent.data
        if(uri != null){
            val code = uri.getQueryParameter("code")
            //cryptowallet://callback?code=e260bd24659d7ee7c88acf1550839ded1a90b159d4cb37d24fd208df4a4222fb
        }

    }

}


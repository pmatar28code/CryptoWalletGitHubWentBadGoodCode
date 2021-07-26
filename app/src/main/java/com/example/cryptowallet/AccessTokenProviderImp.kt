package com.example.cryptowallet

class AccessTokenProviderImp():AccessTokenProvider {
    var token = Repository.accessToken
    override fun token(): String? {
        return token
    }

    override fun refreshToken(): String? {
        return token
    }
}
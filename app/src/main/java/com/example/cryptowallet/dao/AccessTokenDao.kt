package com.example.cryptowallet.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cryptowallet.AccessToken

@Dao
interface AccessTokenDao {
    @Insert
    fun addToken(token: AccessToken)

    @Query("SELECT * FROM AccessToken")
    fun getAllTokens(): List<AccessToken>
}
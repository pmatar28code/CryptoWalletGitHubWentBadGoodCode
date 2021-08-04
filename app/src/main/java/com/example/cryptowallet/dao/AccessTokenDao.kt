package com.example.cryptowallet.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cryptowallet.AccessTokenDCLass

@Dao
interface AccessTokenDao {
    @Insert
    fun addToken(token: AccessTokenDCLass)

    @Query("SELECT * FROM AccessTokenDClass")
    fun getAllTokens(): List<AccessTokenDCLass>

    @Query("DELETE FROM AccessTokenDClass WHERE 'key' = :key")
    fun removeToken(key:Int?)

    @Query("DELETE FROM AccessTokenDClass")
    fun deleteAllTokens()

}
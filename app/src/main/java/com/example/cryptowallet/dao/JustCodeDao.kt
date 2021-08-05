package com.example.cryptowallet.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cryptowallet.JustCode

@Dao
interface JustCodeDao {
    @Insert
    fun addCode(code: JustCode)

    @Query("SELECT * FROM JustCode")
    fun getAllCodes(): List<JustCode>
}
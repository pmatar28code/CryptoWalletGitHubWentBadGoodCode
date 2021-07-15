package com.example.cryptowallet

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cryptowallet.dao.AccessTokenDao

@Database(
    entities=[AccessToken::class],
    version = 1
)
abstract class Database: RoomDatabase(){
    abstract fun AccessTokenDao(): AccessTokenDao
}

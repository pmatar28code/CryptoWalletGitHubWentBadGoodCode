package com.example.cryptowallet

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities=[AccessToken::class],
    version = 1
)

abstract class Database: RoomDatabase(){

    abstract fun AccessTokenDao(): AccessTokenDao
}

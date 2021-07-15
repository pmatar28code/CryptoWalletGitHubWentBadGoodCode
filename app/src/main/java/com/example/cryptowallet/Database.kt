package com.example.cryptowallet

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cryptowallet.dao.AccessTokenDao

@Database(
    entities=[AccessTokenDCLass::class],
    version = 1
)
abstract class Database: RoomDatabase(){
    abstract fun AccessTokenDao(): AccessTokenDao
}

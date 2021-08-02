package com.example.cryptowallet

import androidx.room.RoomDatabase

interface InterfaceMain {
    fun getDatabase(database:RoomDatabase):RoomDatabase
}
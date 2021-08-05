package com.example.cryptowallet

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cryptowallet.dao.AccessTokenDao
import com.example.cryptowallet.dao.JustCodeDao

@Database(
    entities=[AccessTokenDCLass::class,JustCode::class],
    version = 2
)
abstract class CoinBaseDatabase: RoomDatabase(){
    abstract fun AccessTokenDao(): AccessTokenDao
    abstract fun JustCodeDao(): JustCodeDao

    companion object {
        private const val DATABASE_NAME = "COINBASE DATABASE"
        @Volatile
        private var INSTANCE: CoinBaseDatabase? = null

        fun getInstance(context: Context): CoinBaseDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CoinBaseDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

package com.example.cryptowallet

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AccessTokenDClass")
data class AccessTokenDCLass (
    @PrimaryKey(autoGenerate = true) val key:Int = 0,
    @ColumnInfo(name ="access_token")val access_token:String,
    @ColumnInfo(name ="token_type")val token_type :String,
    @ColumnInfo(name ="expires_in")val expires_in :Int,
    @ColumnInfo(name ="refresh_token")val refresh_token :String,
    @ColumnInfo(name ="scope")val scope :String
)

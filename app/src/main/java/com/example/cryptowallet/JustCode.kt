package com.example.cryptowallet

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "JustCode")
data class JustCode(
    @PrimaryKey(autoGenerate = true) var key:Int = 0,
    @ColumnInfo(name = "code") val code:String
)
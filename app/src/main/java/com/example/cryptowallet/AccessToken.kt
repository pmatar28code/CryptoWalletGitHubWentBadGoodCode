package com.example.cryptowallet

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "AccessToken")
data class AccessToken (

    @SerializedName("access_token")
    @Expose
    @PrimaryKey(autoGenerate = true) val access_token:String,

    @SerializedName("token_type")
    @Expose
    @PrimaryKey(autoGenerate = true) val token_type :String,

    @SerializedName("expires_in")
    @Expose
    @PrimaryKey(autoGenerate = true) val expires_in :Int,

    @SerializedName("refresh_token")
    @Expose
    @PrimaryKey(autoGenerate = true) val refresh_token :String,

    @SerializedName("scope")
    @Expose
    @PrimaryKey(autoGenerate = true) val scope :String
)




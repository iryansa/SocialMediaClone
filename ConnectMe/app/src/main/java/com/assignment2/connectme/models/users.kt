package com.assignment2.connectme.models

import com.google.gson.annotations.SerializedName

data class Users(
    val id: Int,
    val username: String,
    @SerializedName("profile_picture")  val profile_pic: String // Base64 string or URL depending on your API
)
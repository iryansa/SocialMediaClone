package com.assignment2.connectme.models
data class Users(
    val id: Int,
    val username: String,
    val profile_pic: String // Base64 string or URL depending on your API
)
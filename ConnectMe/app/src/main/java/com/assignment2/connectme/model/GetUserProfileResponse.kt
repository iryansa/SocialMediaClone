package com.assignment2.connectme.model

import com.assignment2.connectme.network.User

data class GetUserProfileResponse(
    val success: Boolean,
    val user: User?
)

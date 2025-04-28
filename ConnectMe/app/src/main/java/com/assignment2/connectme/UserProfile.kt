package com.assignment2.connectme

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.assignment2.connectme.model.GetUserProfileResponse
import com.assignment2.connectme.network.ApiService
import com.assignment2.connectme.network.RetrofitClient
import com.assignment2.connectme.session.SessionManager
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback

class UserProfile : AppCompatActivity() {
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(R.layout.activity_user_profile)

    val newUserId = intent.getIntExtra("newUserId", 1)
    val sessionManager = SessionManager(this)
    val userId = sessionManager.getUserId()
    val followButton = findViewById<TextView>(R.id.follow_button) // Ensure the correct ID is used

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        insets
    }

    // Use the userId as needed
    // For example, you can fetch the user profile data using the userId
    // use retrofit to fetch user data
    val apiService = RetrofitClient.instance
    apiService.getUserProfile(newUserId).enqueue(object : retrofit2.Callback<GetUserProfileResponse> {
        override fun onResponse(
            call: retrofit2.Call<GetUserProfileResponse>,
            response: retrofit2.Response<GetUserProfileResponse>
        ) {
            if (response.isSuccessful) {
                val userProfile = response.body()?.user
                // Update UI with user profile data
                // For example, set the username and profile picture
                val usernameTextView = findViewById<TextView>(R.id.username)
                val profileImageView = findViewById<CircleImageView>(R.id.profile_picture)
                val profilePic = userProfile?.profile_picture
                val username = userProfile?.username
                usernameTextView.text = username
                if (!profilePic.isNullOrEmpty()) {
                    val imageBytes = android.util.Base64.decode(profilePic, android.util.Base64.DEFAULT)
                    val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    profileImageView.setImageBitmap(bitmap)
                } else {
                    profileImageView.setImageResource(R.drawable.profile_man)
                }
                // Set other user profile data as needed
                // For example, set the name, bio, etc.
                val nameTextView = findViewById<TextView>(R.id.name)
                val bioTextView = findViewById<TextView>(R.id.bio)
                nameTextView.text = userProfile?.name
                bioTextView.text = userProfile?.bio



            } else {
                // Handle error response
                val errorMessage = response.errorBody()?.string()
            }
        }





        override fun onFailure(call: retrofit2.Call<GetUserProfileResponse>, t: Throwable) {
            // Handle failure
            val errorMessage = t.message

        }
    })
    apiService.getFollowStatus(userId, newUserId).enqueue(object : Callback<ApiService.FollowStatusResponse> {
        override fun onResponse(call: Call<ApiService.FollowStatusResponse>, response: retrofit2.Response<ApiService.FollowStatusResponse>) {
            if (response.isSuccessful) {
                val status = response.body()?.status
                when (status) {
                    "accepted" -> followButton.text = "Unfollow"
                    "pending" -> followButton.text = "Requested"
                    "none" -> followButton.text = "Follow"
                }
            } else {
                // Log error response for debugging
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("UserProfile", "Error in getFollowStatus: $errorBody")
            }
        }

        override fun onFailure(call: Call<ApiService.FollowStatusResponse>, t: Throwable) {
            // Log failure for debugging
            android.util.Log.e("UserProfile", "Failed to get follow status", t)
            Toast.makeText(this@UserProfile, "Failed to get follow status", Toast.LENGTH_SHORT).show()
        }
    })

    followButton.setOnClickListener {
        val buttonValue = followButton.text.toString() // get fresh value every time when clicked

        if (buttonValue == "Follow") {
            apiService.followUser(userId, newUserId)
                .enqueue(object : Callback<ApiService.FollowResponse> {
                    override fun onResponse(
                        call: Call<ApiService.FollowResponse>,
                        response: retrofit2.Response<ApiService.FollowResponse>
                    ) {
                        if (response.isSuccessful) {
                            followButton.text = "Requested" // because initially you send request, not immediately "unfollow"
                        }
                    }

                    override fun onFailure(call: Call<ApiService.FollowResponse>, t: Throwable) {
                        // Handle failure (show toast maybe)
                    }
                })
        }
        else if (buttonValue == "Unfollow") {
            followButton.isEnabled = false
            followButton.text = "Unfollowing..."

            apiService.unfollowUser(userId, newUserId)
                .enqueue(object : Callback<ApiService.UnfollowResponse> {
                    override fun onResponse(
                        call: Call<ApiService.UnfollowResponse>,
                        response: retrofit2.Response<ApiService.UnfollowResponse>
                    ) {
                        if (response.isSuccessful) {
                            followButton.text = "Follow"
                        } else {
                            followButton.text = "Unfollow"
                        }
                        followButton.isEnabled = true
                    }

                    override fun onFailure(call: Call<ApiService.UnfollowResponse>, t: Throwable) {
                        followButton.text = "Unfollow"
                        followButton.isEnabled = true
                    }
                })
        }

    }
}
}
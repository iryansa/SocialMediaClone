package com.assignment2.connectme.network

import com.assignment2.connectme.model.GetUserProfileResponse
import com.assignment2.connectme.models.Users
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

// --- Login Request and Response ---
data class LoginRequest(val identifier: String, val password: String)

data class LoginResponse(
    val status: String,
    val message: String,
    val user: User?
)

data class User(
    val id: Int,
    val username: String,
    val name: String,
    val email: String,
    val profile_picture: String,
    val bio: String
)

data class SignupRequest(
    val username: String,
    val name: String,
    val email: String,
    val password: String
)

data class SignupResponse(
    val status: String,
    val message: String
)

// --- API Service ---
interface ApiService {


    @FormUrlEncoded
    @POST("signup.php")
    fun signup(
        @Field("username") username: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignupResponse>

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("identifier") identifier: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("update_profile.php")
    fun updateProfile(
        @Part("userId") userId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("username") username: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part profile_picture: MultipartBody.Part? // nullable if not uploading new picture
    ): Call<SignupResponse>  // You can reuse SignupResponse

    @FormUrlEncoded
    @POST("get_user_profile.php")
    fun getUserProfile(
        @Field("userId") userId: Int
    ): Call<GetUserProfileResponse>

    @FormUrlEncoded
    @POST("search_users.php")
    fun searchUsers(
        @Field("query") searchText: String
    ): Call<List<Users>>


    @FormUrlEncoded
    @POST("get_follow_status.php")
    fun getFollowStatus(
        @Field("follower_id") followerId: Int,
        @Field("following_id") followingId: Int
    ): Call<FollowStatusResponse>

    data class FollowStatusResponse(
        val status: String
    )

    @FormUrlEncoded
    @POST("follow.php")
    fun followUser(
        @Field("follower_id") followerId: Int,
        @Field("following_id") followingId: Int
    ): Call<FollowResponse>

    data class FollowResponse(
        val status: String,
        val message: String
    )
}

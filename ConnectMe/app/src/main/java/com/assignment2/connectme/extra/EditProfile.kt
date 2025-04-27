package com.assignment2.connectme.extra

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.assignment2.connectme.R
import com.assignment2.connectme.model.GetUserProfileResponse
import com.assignment2.connectme.network.RetrofitClient
import com.assignment2.connectme.session.SessionManager
import com.assignment2.connectme.network.SignupResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfile : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editUsername: EditText
    private lateinit var editBio: EditText
    private lateinit var profileImageView: ImageView
    private var selectedImageUri: Uri? = null

    private val GALLERY_REQUEST_CODE = 1001
    private val CAMERA_REQUEST_CODE = 1002
    private val CAMERA_PERMISSION_CODE = 1003

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)
        val sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editName = findViewById(R.id.edit_name)
        editUsername = findViewById(R.id.edit_username)
        editBio = findViewById(R.id.edit_bio)
        profileImageView = findViewById(R.id.edit_pfp)

        val apiService = RetrofitClient.instance
        apiService.getUserProfile(userId).enqueue(object : Callback<GetUserProfileResponse> {
            override fun onResponse(call: Call<GetUserProfileResponse>, response: Response<GetUserProfileResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()?.user


                    if (!user?.profile_picture.isNullOrEmpty()) {
                        val imageBytes = android.util.Base64.decode(user?.profile_picture, android.util.Base64.DEFAULT)
                        val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        profileImageView.setImageBitmap(bitmap)
                    } else {
                        profileImageView.setImageResource(R.drawable.profile_man)
                    }
                } else {
                    Toast.makeText(this@EditProfile, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetUserProfileResponse>, t: Throwable) {
                Toast.makeText(this@EditProfile, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
        findViewById<View>(R.id.cancel_button).setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.save_button).setOnClickListener {
            saveProfile()
        }

        profileImageView.setOnClickListener {
            openImagePicker()
        }
    }

    private fun openImagePicker() {
        val options = arrayOf("Camera", "Gallery")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Pick Image From")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    selectedImageUri = data?.data
                    profileImageView.setImageURI(selectedImageUri)
                }
                CAMERA_REQUEST_CODE -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    val uri = bitmap?.let { getImageUriFromBitmap(it) }
                    selectedImageUri = uri
                    profileImageView.setImageURI(uri)
                }
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "ProfilePic", null)
        return Uri.parse(path)
    }

    private fun saveProfile() {
        val name = editName.text.toString().trim()
        val username = editUsername.text.toString().trim()
        val bio = editBio.text.toString().trim()

        if (name.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Name and Username are required", Toast.LENGTH_SHORT).show()
            return
        }

        val sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId()

        val nameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
        val usernameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), username)
        val bioBody = RequestBody.create("text/plain".toMediaTypeOrNull(), bio)
        val userIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userId.toString())

        var profilePicturePart: MultipartBody.Part? = null

        if (selectedImageUri != null) {
            val inputStream = contentResolver.openInputStream(selectedImageUri!!)
            val bytes = inputStream?.readBytes()
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), bytes!!)
            profilePicturePart = MultipartBody.Part.createFormData("profile_picture", "profile.jpg", requestFile)
        }

        val apiService = RetrofitClient.instance
        apiService.updateProfile(userIdBody, nameBody, usernameBody, bioBody, profilePicturePart)
            .enqueue(object : Callback<SignupResponse> {
                override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Toast.makeText(this@EditProfile, result?.message, Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@EditProfile, "Update failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    Toast.makeText(this@EditProfile, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}

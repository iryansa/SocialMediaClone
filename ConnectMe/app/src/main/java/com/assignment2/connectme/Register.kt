package com.assignment2.connectme

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.assignment2.connectme.network.ApiService
import com.assignment2.connectme.network.SignupRequest
import com.assignment2.connectme.network.SignupResponse
import com.assignment2.connectme.network.RetrofitClient
import com.assignment2.connectme.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val registerBtn = findViewById<Button>(R.id.registerbtn)
        val nameField = findViewById<EditText>(R.id.r_name)
        val usernameField = findViewById<EditText>(R.id.r_uname)
        val emailField = findViewById<EditText>(R.id.r_email)
        val passwordField = findViewById<EditText>(R.id.r_pass)

        registerBtn.setOnClickListener {
            val name = nameField.text.toString().trim()
            val username = usernameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@Register, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
// Don't create SignupRequest object anymore
// val signupRequest = SignupRequest(username, name, email, password)

// Make the network call to your signup API
            val apiService = RetrofitClient.instance

            apiService.signup(username, name, email, password)
                .enqueue(object : Callback<SignupResponse> {
                    override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                        if (response.isSuccessful) {
                            val signupResponse = response.body()

                            if (signupResponse?.status == "success") {
                                // Show success toast
                                Toast.makeText(this@Register, signupResponse.message, Toast.LENGTH_SHORT).show()

                                // Navigate to Home Activity
                                val intent = Intent(this@Register, MainActivity::class.java)
                                startActivity(intent)
                                finish() // (optional) finish Register Activity
                            } else {
                                // Show error message from server
                                Toast.makeText(this@Register, signupResponse?.message ?: "Signup failed", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@Register, "Signup failed. Try again.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                        Toast.makeText(this@Register, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
//            // Create Signup request object
//            val signupRequest = SignupRequest(username, name, email, password)
//
//            // Make the network call to your signup API
//            val apiService = RetrofitClient.instance
//            apiService.signup(signupRequest).enqueue(object : Callback<SignupResponse> {
//                override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
//                    if (response.isSuccessful) {
//                        val signupResponse = response.body()
//
//                        if (signupResponse?.status == "success") {
//                            // Show success toast
//                            Toast.makeText(this@Register, signupResponse.message, Toast.LENGTH_SHORT).show()
//
//
//                            // Navigate to Home Activity
//                            val intent = Intent(this@Register, MainActivity::class.java)
//                            startActivity(intent)
//                            finish()
//                        } else {
//                            // Handle registration failure
//                            Toast.makeText(this@Register, signupResponse?.message ?: "Registration failed", Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
//                        // Handle server error
//                        Toast.makeText(this@Register, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
//                    // Handle network failure
//                    Toast.makeText(this@Register, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
//                }
//            })
//        }

        // Login link
        val LoginTextView = findViewById<TextView>(R.id.loginLink)
        val fullText = "Already have an account? Login"
        val spannableString = SpannableString(fullText)

        val boldSpan = StyleSpan(Typeface.BOLD)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@Register, MainActivity::class.java)
                startActivity(intent)
            }
        }

        val startIndex = fullText.indexOf("Login")
        val endIndex = startIndex + "Login".length

        spannableString.setSpan(boldSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        LoginTextView.text = spannableString
        LoginTextView.movementMethod = LinkMovementMethod.getInstance()
    }
}

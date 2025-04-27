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
import androidx.appcompat.app.AppCompatActivity
import com.assignment2.connectme.network.ApiService
import com.assignment2.connectme.network.LoginRequest
import com.assignment2.connectme.network.LoginResponse
import com.assignment2.connectme.network.RetrofitClient
import com.assignment2.connectme.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginBtn = findViewById<Button>(R.id.loginButton)
        val usernameField = findViewById<EditText>(R.id.l_uname)
        val passwordField = findViewById<EditText>(R.id.l_pass)

        loginBtn.setOnClickListener {
            val identifier = usernameField.text.toString()  // Could be email or username
            val password = passwordField.text.toString()

            if (identifier.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@MainActivity, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
// Call login API
                val apiService = RetrofitClient.instance

                apiService.login(identifier, password).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            if (loginResponse != null && loginResponse.status == "success") {
                                // Store the user session (userId and email)
                                val user = loginResponse.user
                                user?.let {
                                    val sessionManager = SessionManager(this@MainActivity)
                                    sessionManager.saveUserSession(it.id, it.email)

                                    // Navigate to Home Activity
                                    val intent = Intent(this@MainActivity, Home::class.java)
                                    startActivity(intent)
                                    finish()  // Close the login activity
                                }
                            } else {
                                Toast.makeText(this@MainActivity, loginResponse?.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Failed to login. Try again.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        val registerTextView = findViewById<TextView>(R.id.RegisterLinkText)
        val fullText = "Don't have an account? Register"
        val spannableString = SpannableString(fullText)

        // Make "Register" bold
        val boldSpan = StyleSpan(Typeface.BOLD)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Navigate to Register Activity
                val intent = Intent(this@MainActivity, Register::class.java)
                startActivity(intent)
            }
        }

        val startIndex = fullText.indexOf("Register")
        val endIndex = startIndex + "Register".length

        spannableString.setSpan(boldSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        registerTextView.text = spannableString
        registerTextView.movementMethod = LinkMovementMethod.getInstance() // Make it clickable
    }
}

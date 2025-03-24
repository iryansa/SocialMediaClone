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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginBtn = findViewById<TextView>(R.id.loginButton)
        val usernameField = findViewById<TextView>(R.id.l_uname)
        val passwordField = findViewById<TextView>(R.id.l_pass)
        loginBtn.setOnClickListener {
            if (usernameField.text.toString().isEmpty() || passwordField.text.toString().isEmpty()) {
                Toast.makeText(this@MainActivity, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                // Navigate to Home Activity
                val intent = Intent(this@MainActivity, Home::class.java)
                startActivity(intent)
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
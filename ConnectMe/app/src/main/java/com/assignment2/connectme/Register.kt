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
import com.assignment2.connectme.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {
    val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
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
        val phoneField = findViewById<EditText>(R.id.r_phone)
        val emailField = findViewById<EditText>(R.id.r_email)
        val passwordField = findViewById<EditText>(R.id.r_pass)

        registerBtn.setOnClickListener {
            if (nameField.text.toString().isEmpty() ||
                usernameField.text.toString().isEmpty() ||
                phoneField.text.toString().isEmpty() ||
                emailField.text.toString().isEmpty() ||
                passwordField.text.toString().isEmpty()) {

                Toast.makeText(this@Register, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                // Navigate to MainActivity
                Toast.makeText(this@Register, "Registration Successful!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@Register, Home::class.java)
                startActivity(intent)
            }
        }


        val LoginTextView = findViewById<TextView>(R.id.loginLink)
        val fullText = "Already have an account? Login"
        val spannableString = SpannableString(fullText)

        // Make "Register" bold
        val boldSpan = StyleSpan(Typeface.BOLD)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Navigate to Register Activity
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
package com.assignment2.connectme

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoadingScreen : AppCompatActivity() {
    private lateinit var tvLoading: TextView
    private val loadingText = "Loading..."
    private var index = 0
    private val delay: Long = 300 // Delay per character

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loading_screen)

        tvLoading = findViewById(R.id.tvLoading)

        // Start animation
        animateText()

        // Check authentication after animation starts
        Handler(Looper.getMainLooper()).postDelayed({
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                startActivity(Intent(this, Home::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }, 2000) // Adjust delay as needed
    }

    private fun animateText() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (index <= loadingText.length) {
                    tvLoading.text = loadingText.substring(0, index)
                    index++
                    handler.postDelayed(this, delay)
                }
            }
        }, delay)
    }
}

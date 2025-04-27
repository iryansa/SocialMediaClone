package com.assignment2.connectme

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.assignment2.connectme.databinding.ActivityHomeBinding
import com.assignment2.connectme.session.SessionManager
import com.google.firebase.auth.FirebaseAuth

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        navView.setupWithNavController(navController)
        navView.findViewById<View>(R.id.add).setOnClickListener {
                    // 👇 Launch UploadPosts Activity manually
                    val intent = Intent(this, UploadPosts::class.java)
                    startActivity(intent)
                    false // important to return false, so that the bottom nav selection does not change
                }

        // Add long click listener for the "Profile" button
        navView.findViewById<View>(R.id.profile).setOnLongClickListener {
            val sessionManager = SessionManager(this)
            sessionManager.logout()

            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            true
        }
    }
}
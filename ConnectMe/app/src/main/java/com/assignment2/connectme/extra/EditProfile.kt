package com.assignment2.connectme.extra

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.assignment2.connectme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<View>(R.id.cancel_button).setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.save_button).setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                val userId = it.uid
                val database = FirebaseDatabase.getInstance().reference.child("users").child(userId)

                val name = findViewById<EditText>(R.id.edit_name).text.toString()
                val bio = findViewById<EditText>(R.id.edit_bio).text.toString()

                database.child("name").setValue(name).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Optionally show a success message
                    } else {
                        // Optionally show an error message
                    }
                }

                database.child("bio").setValue(bio).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Optionally show a success message
                    } else {
                        // Optionally show an error message
                    }
                }
            }
        }
    }
}

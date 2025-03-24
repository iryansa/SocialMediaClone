package com.assignment2.connectme.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.assignment2.connectme.R
import com.assignment2.connectme.chat  // Import Chat Activity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find ImageView with ID chatbtn
        val chatButton: ImageView = view.findViewById(R.id.chatbtn)

        // Set click listener to open Chat.kt
        chatButton.setOnClickListener {
            val intent = Intent(requireContext(), chat::class.java)
            startActivity(intent)
        }
    }
}

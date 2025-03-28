package com.assignment2.connectme.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment2.connectme.R
import com.assignment2.connectme.adapters.PhotoAdapter
import com.assignment2.connectme.databinding.FragmentProfileBinding
import com.assignment2.connectme.extra.EditProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //update the pfp, name_text, bio_text by the one from the link in the firebase database
// Get the current user from Firebase Authentication
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val userId = it.uid
            // Update the profile picture, name, and bio from the Firebase database
            val database = FirebaseDatabase.getInstance().reference.child("users").child(userId)

            database.child("profileImageUrl").get().addOnSuccessListener { snapshot ->
                val profilePictureUrl = snapshot.value.toString()
                // Load the profile picture using an image loading library like Picasso
                Picasso.get().load(profilePictureUrl).into(binding.pfp)
            }

            database.child("name").get().addOnSuccessListener { snapshot ->
                val name = snapshot.value.toString()
                binding.nameText.text = name
            }

            database.child("bio").get().addOnSuccessListener { snapshot ->
                val bio = snapshot.value.toString()
                binding.bioText.text = bio
            }
        }
        // on click edit_pencil open edit profile
        binding.editPencil.setOnClickListener {
            val intent = Intent(activity, EditProfile::class.java)
            startActivity(intent)
        }


        // List of 7 drawable photos
        val photos = listOf(
            R.drawable.bird,
            R.drawable.building,
            R.drawable.caars,
            R.drawable.island,
            R.drawable.mountain,
            R.drawable.space,
            R.drawable.sunset
        )

        // Find RecyclerView and set up GridLayoutManager
        val recyclerView = view.findViewById<RecyclerView>(R.id.posts_view)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = PhotoAdapter(photos)
    }



    companion object {

    }
}
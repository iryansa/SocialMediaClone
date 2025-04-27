package com.assignment2.connectme.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment2.connectme.R
import com.assignment2.connectme.adapters.PhotoAdapter
import com.assignment2.connectme.databinding.FragmentProfileBinding
import com.assignment2.connectme.extra.EditProfile
import com.assignment2.connectme.model.GetUserProfileResponse
import com.assignment2.connectme.network.RetrofitClient
import com.assignment2.connectme.session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        val apiService = RetrofitClient.instance
        apiService.getUserProfile(userId).enqueue(object : Callback<GetUserProfileResponse> {
            override fun onResponse(call: Call<GetUserProfileResponse>, response: Response<GetUserProfileResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()?.user

                    binding.nameText.text = user?.name ?: ""
                    binding.bioText.text = user?.bio ?: ""

                    if (!user?.profile_picture.isNullOrEmpty()) {
                        val imageBytes = android.util.Base64.decode(user?.profile_picture, android.util.Base64.DEFAULT)
                        val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        binding.pfp.setImageBitmap(bitmap)
                    } else {
                        binding.pfp.setImageResource(R.drawable.profile_man)
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetUserProfileResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

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
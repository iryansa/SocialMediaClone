package com.assignment2.connectme.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment2.connectme.R
import com.assignment2.connectme.adapters.PhotoAdapter
import com.assignment2.connectme.databinding.FragmentProfileBinding

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
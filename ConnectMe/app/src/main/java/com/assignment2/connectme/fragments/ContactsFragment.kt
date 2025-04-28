package com.assignment2.connectme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment2.connectme.R
import com.assignment2.connectme.adapters.ContactsAdapter
import com.assignment2.connectme.model.FollowRequest
import com.assignment2.connectme.network.RetrofitClient
import com.assignment2.connectme.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewContacts)
        recyclerView.layoutManager = LinearLayoutManager(context)
        contactsAdapter = ContactsAdapter(emptyList())
        recyclerView.adapter = contactsAdapter

        loadFollowRequests()

        return view
    }

    private fun loadFollowRequests() {
        val sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        RetrofitClient.instance.getPendingRequests(userId)
            .enqueue(object : Callback<List<FollowRequest>> {
                override fun onResponse(call: Call<List<FollowRequest>>, response: Response<List<FollowRequest>>) {
                    if (response.isSuccessful) {
                        contactsAdapter.updateData(response.body() ?: emptyList())
                    }
                }

                override fun onFailure(call: Call<List<FollowRequest>>, t: Throwable) {
                    // Handle error
                }
            })
    }
}

package com.assignment2.connectme.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment2.connectme.R
import com.assignment2.connectme.adapters.UserAdapter
import com.assignment2.connectme.models.Users
import com.assignment2.connectme.network.RetrofitClient
import com.assignment2.connectme.network.User
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var searchEditText: TextInputEditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchEditText = view.findViewById(R.id.search_text)
        recyclerView = view.findViewById(R.id.recyclerViewSearch)

        recyclerView.layoutManager = LinearLayoutManager(context)
        userAdapter = UserAdapter(emptyList())
        recyclerView.adapter = userAdapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.length >= 0) {
                        searchUsers(it.toString())
                    }
                }
            }
        })

        return view
    }

    private fun searchUsers(query: String) {
        val apiService = RetrofitClient.instance
        val call: Call<List<Users>> = apiService.searchUsers(query)
        Log.d(call.request().toString(), "Search query: $query")
        call.enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                if (response.isSuccessful) {
                    Log.d("SearchFragment", "Response: ${response.body()}")
                    userAdapter.updateData(response.body() ?: emptyList())
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                // Handle failure
                Log.e("SearchFragment", "API call failed: ${t.message}")
            }
        })
    }
}

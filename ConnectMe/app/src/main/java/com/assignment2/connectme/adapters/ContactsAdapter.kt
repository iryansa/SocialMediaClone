package com.assignment2.connectme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assignment2.connectme.R
import com.assignment2.connectme.model.FollowRequest
import com.assignment2.connectme.network.ApiService
import com.assignment2.connectme.network.RetrofitClient
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactsAdapter(private var followRequests: List<FollowRequest>) :
    RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usernameTextView: TextView = view.findViewById(R.id.usernameTextView)
        val profileImageView: ImageView = view.findViewById(R.id.profileImageView)
        val acceptButton: Button = view.findViewById(R.id.acceptButton)
        val rejectButton: Button = view.findViewById(R.id.rejectButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_follow_request, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val request = followRequests[position]

        holder.usernameTextView.text = request.username

if (!request.profile_picture.isNullOrEmpty()) {
    try {
        val imageBytes = android.util.Base64.decode(request.profile_picture, android.util.Base64.DEFAULT)
        val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.profileImageView.setImageBitmap(bitmap)
    } catch (e: IllegalArgumentException) {
        // If decoding fails, fallback to placeholder
        Picasso.get().load(request.profile_picture).placeholder(R.drawable.profile_man).into(holder.profileImageView)
    }
} else {
    holder.profileImageView.setImageResource(R.drawable.profile_man)
}
        holder.acceptButton.setOnClickListener {
            // Handle accept logic
            //use acceptFollowRequest API with request id
            // start the code from next line
            RetrofitClient.instance.acceptFollowRequest(request.id)
                .enqueue(object : Callback<ApiService.FollowActionResponse> {
                    override fun onResponse(
                        call: Call<ApiService.FollowActionResponse>,
                        response: Response<ApiService.FollowActionResponse>
                    ) {
                        if (response.isSuccessful) {
                            val actionResponse = response.body()
                            if (actionResponse != null) {
                                // Handle successful acceptance
                                // For example, remove the request from the list and notify the adapter
                                followRequests = followRequests.filter { it.id != request.id }
                                notifyDataSetChanged()
                            }
                        } else {
                            // Handle unsuccessful response
                        }
                    }
                    override fun onFailure(call: Call<ApiService.FollowActionResponse>, t: Throwable) {
                        // Handle failure
                    }

                })
        }

        holder.rejectButton.setOnClickListener {
            // Handle reject logic
            //use rejectFollowRequest API with request id
            // start the code from next line

            RetrofitClient.instance.rejectFollowRequest(request.id)
                .enqueue(object : Callback<ApiService.FollowActionResponse> {
                    override fun onResponse(
                        call: Call<ApiService.FollowActionResponse>,
                        response: Response<ApiService.FollowActionResponse>
                    ) {
                        if (response.isSuccessful) {
                            val actionResponse = response.body()
                            if (actionResponse != null) {
                                // Handle successful rejection
                                // For example, remove the request from the list and notify the adapter
                                followRequests = followRequests.filter { it.id != request.id }
                                notifyDataSetChanged()
                            }
                        } else {
                            // Handle unsuccessful response
                        }
                    }

                    override fun onFailure(call: Call<ApiService.FollowActionResponse>, t: Throwable) {
                        // Handle failure
                    }

                })
        }
    }

    override fun getItemCount(): Int = followRequests.size

    fun updateData(newData: List<FollowRequest>) {
        followRequests = newData
        notifyDataSetChanged()
    }
}

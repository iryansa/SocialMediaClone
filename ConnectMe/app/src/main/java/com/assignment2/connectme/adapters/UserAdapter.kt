package com.assignment2.connectme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assignment2.connectme.R
import com.assignment2.connectme.models.Users
import com.squareup.picasso.Picasso

class UserAdapter(private var users: List<Users>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        val username: TextView = itemView.findViewById(R.id.usernameText)
        val addButton: Button = itemView.findViewById(R.id.addFriendButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.username.text = user.username

        // Assuming profile_pic is a URL or base64 decoded
        Picasso.get().load(user.profile_pic).placeholder(R.drawable.profile_man).into(holder.profileImage)

        holder.addButton.setOnClickListener {
            // Handle Add Friend Button click here
        }
    }

    override fun getItemCount(): Int = users.size

    fun updateData(newUsers: List<Users>) {
        users = newUsers
        notifyDataSetChanged()
    }
}

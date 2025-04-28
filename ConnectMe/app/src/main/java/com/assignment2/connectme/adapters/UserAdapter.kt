package com.assignment2.connectme.adapters

import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assignment2.connectme.R
import com.assignment2.connectme.UserProfile
import com.assignment2.connectme.models.Users
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private var users: List<Users>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: CircleImageView = itemView.findViewById(R.id.profileImage)
        val username: TextView = itemView.findViewById(R.id.usernameText)
//        val addButton: Button = itemView.findViewById(R.id.addFriendButton)
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
if (!user.profile_pic.isNullOrEmpty()) {
    try {
        val imageBytes = android.util.Base64.decode(user.profile_pic, android.util.Base64.DEFAULT)
        val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.profileImage.setImageBitmap(bitmap)
    } catch (e: IllegalArgumentException) {
        // If decoding fails, fallback to placeholder
        Picasso.get().load(user.profile_pic).placeholder(R.drawable.profile_man).into(holder.profileImage)
    }
} else {
    holder.profileImage.setImageResource(R.drawable.profile_man)
}
//        holder.addButton.setOnClickListener {
//            // Handle Add Friend Button click here
//        }
        //clicking on the user will open the profile of the user
        holder.itemView.setOnClickListener {
            // Handle user click here
            // For example, navigate to the user's profile
            val context = holder.itemView.context
            val intent = Intent(context, UserProfile::class.java)
            intent.putExtra("newUserId", user.id) // Pass the user ID to the profile activity
            context.startActivity(intent)


        }
    }

    override fun getItemCount(): Int = users.size

    fun updateData(newUsers: List<Users>) {
        users = newUsers
        notifyDataSetChanged()
    }
}

package com.assignment2.connectme.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val PREF_NAME = "user_session"
    private val KEY_IS_LOGGED_IN = "is_logged_in"
    private val KEY_USER_ID = "user_id"
    private val KEY_EMAIL = "email"

    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    // Save user session after login
    fun saveUserSession(userId: Int, email: String) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putInt(KEY_USER_ID, userId)
        editor.putString(KEY_EMAIL, email)
        editor.apply()
    }

    // Check if user is logged in
    fun isLoggedIn(): Boolean {
        return sharedPref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Get user details (just ID and email)
    fun getUserDetails(): Map<String, String?> {
        val user = HashMap<String, String?>()
        user["user_id"] = sharedPref.getInt(KEY_USER_ID, -1).toString()
        user["email"] = sharedPref.getString(KEY_EMAIL, null)
        return user
    }

    // Get user ID
    fun getUserId(): Int {
        return sharedPref.getInt(KEY_USER_ID, -1)
    }

    // Logout (clear session)
    fun logout() {
        editor.clear()
        editor.apply()
    }
}

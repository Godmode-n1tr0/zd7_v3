package com.bignerdranch.android.pract7.utils

import android.content.Context

class PrefsManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun saveUser(login: String, email: String, role: UserRole) {
        prefs.edit()
            .putString("login", login)
            .putString("email", email)
            .putString("role", role.name)
            .apply()
    }

    fun getRole(): UserRole =
        UserRole.valueOf(
            prefs.getString("role", UserRole.CLIENT.name)!!
        )

    fun isLoggedIn(): Boolean =
        prefs.contains("email")

    fun clear() {
        prefs.edit().clear().apply()
    }
}

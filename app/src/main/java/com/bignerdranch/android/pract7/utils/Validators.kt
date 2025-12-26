package com.bignerdranch.android.pract7.utils

import android.util.Patterns

fun isValidEmail(email: String): Boolean =
    Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun isValidPassword(password: String): Boolean =
    password.length >= 6 && password.any { it.isDigit() }

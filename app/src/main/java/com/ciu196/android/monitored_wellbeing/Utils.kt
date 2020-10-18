package com.ciu196.android.monitored_wellbeing

import User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


object Utils {
    fun writeNewUser(userId: String, name: String, points: Int?) {
        val database = Firebase.database.getReference()
        val user = User(name, points)
        database.child("users").child(userId).setValue(user)
    }
}
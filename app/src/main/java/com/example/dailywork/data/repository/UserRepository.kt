// src/data/repository/UserRepository.kt
package com.example.dailywork.data.repository

import com.example.dailywork.data.database.FirebaseService
import com.example.dailywork.data.model.User
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class UserRepository @Inject constructor(
    private var firebaseService: FirebaseService
) {

    // Function to register a new user using email and password


    suspend fun registerUser(
        email: String,
        password: String,
        userData: User
    ):Result<FirebaseUser?>{
        return firebaseService.registerUser(email, password, userData)
    }

    // Function to log in an existing user
    suspend fun loginUser(
        email: String,
        password: String
    ): Result<FirebaseUser?> {
        return firebaseService.loginUser(email, password)
    }

    // Function to log out the current user
    fun logoutUser() {
        firebaseService.logoutUser()
    }

    // Function to get the current logged-in user
    fun getCurrentUser(): FirebaseUser? {
        return firebaseService.getCurrentUser()
    }

    // Function to retrieve user data from Firestore
    suspend fun getUserData(userId: String): Result<User> {
        return firebaseService.getUser(userId)
    }

    // Function to update user profile in Firestore
    suspend fun updateUserDetails(user: User): Result<Void?> {
        return firebaseService.updateUserProfile(user.userId, user)
    }
}

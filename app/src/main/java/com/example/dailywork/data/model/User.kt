// src/data/model/User.kt
package com.example.dailywork.data.model

import com.google.firebase.firestore.DocumentId


data class User(
    @DocumentId var userId: String = "",  // Firebase-generated unique user ID
    val username: String = "",            // User's name
    val email: String = "",               // User's email address
    val password: String = "",            // User's password
    val profilePictureUrl: String = "",   // URL for the user's profile picture
    val goals: List<String> = emptyList(), // List of goal IDs associated with the user
    val milestones: List<String> = emptyList(), // List of milestone IDs associated with the user
    val createdAt: Long = System.currentTimeMillis(), // Timestamp for when the user was created
    val lastLoginAt: Long = System.currentTimeMillis(), // Timestamp of the user's last login
    val settings: UserSettings = UserSettings() // User's specific settings/preferences
)

data class UserSettings(
    val notificationEnabled: Boolean = true,  // If the user has enabled notifications
    val theme: AppTheme = AppTheme.LIGHT,     // App theme, could be light or dark
    val language: String = "en"               // Preferred language of the user
)

enum class AppTheme {
    LIGHT, DARK  // User theme preference
}

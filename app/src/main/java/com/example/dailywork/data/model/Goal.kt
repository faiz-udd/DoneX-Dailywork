package com.example.dailywork.data.model

import com.google.firebase.firestore.DocumentId

data class Goal(
    @DocumentId val goalId: String = "",         // Unique ID for the goal
    val userId: String = "",                     // User ID of the person who created the goal
    val title: String = "",                      // Title of the goal (e.g., "Learn Kotlin")
    val description: String = "",                // Description of the goal (e.g., "Master Kotlin in 6 months")
    var milestones: List<String> = emptyList(),  // List of milestone IDs associated with this goal
    val createdAt: Long = System.currentTimeMillis(),  // Timestamp for when the goal was created
    val dueDate: Long? = null,                   // Optional due date for the goal
    val progress: Int = 0                        // Percentage of completion (0-100%)
)

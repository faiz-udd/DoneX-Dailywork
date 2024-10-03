package com.example.dailywork.data.model

import com.google.firebase.firestore.DocumentId

data class Task(
    @DocumentId val taskId: String = "",         // Unique ID for the task
    val milestoneId: String = "",                // The ID of the milestone to which this task belongs
    val title: String = "",                      // Title of the task (e.g., "Watch Kotlin tutorial")
    val description: String = "",                // Description of the task (e.g., "Complete Kotlin basic tutorials on YouTube")
    val time: String = "",                       // Optional time field (e.g., "10:00 AM - 11:00 AM")
    val dueDate: Long? = null,                   // Optional due date for the task
    val completed: Boolean = false,              // Flag to check if the task is completed
    val createdAt: Long = System.currentTimeMillis()  // Timestamp for when the task was created
) {

}

//package com.example.dailywork.data.model
//
//import com.google.firebase.firestore.DocumentId
//
//data class Milestone(
//    @DocumentId val milestoneId: String = "",    // Unique ID for the milestone
//    val goalId: String = "",                     // The ID of the goal to which this milestone belongs
//    val title: String = "",                      // Title of the milestone (e.g., "Complete first module")
//    val description: String = "",                // Description of the milestone (e.g., "Finish learning about Kotlin basics")
//    val dueDate: Long? = null,                   // Optional due date for the milestone
//    val completed: Boolean = false,              // Flag to check if the milestone is completed
//    val createdAt: Long = System.currentTimeMillis(),  // Timestamp for when the milestone was created
//    var tasks: List<Any> = emptyList()        // List of task IDs associated with this milestone
//)

//Newly Added Code
package com.example.dailywork.data.model

import com.google.firebase.Timestamp

data class Milestone(
    val milestoneId: String = "",          // Firestore Document ID
    val title: String = "",                // Milestone title
    val description: String = "",           // Milestone description
    val dueDate: Long = 0L,                 // Due date in Long format (epoch timestamp)
    val goalId: String = "",                // Goal ID to which this milestone belongs
    val isCompleted: Boolean = false,       // Status of the milestone
    val createdAt: Timestamp = Timestamp.now()  // Time when the milestone was created
)

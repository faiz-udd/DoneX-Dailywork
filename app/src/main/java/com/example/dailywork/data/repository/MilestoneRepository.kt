//package com.example.dailywork.data.repository
//
//import com.example.dailywork.data.model.Milestone
//import com.google.firebase.firestore.FirebaseFirestore
//
//class MilestoneRepository {
//
//    private val db = FirebaseFirestore.getInstance()
//
//    // Function to add a new milestone to Firestore under a specific goal
//    fun addMilestone(
//        userId: String,
//        goalId: String,
//        milestone: Milestone,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        val milestoneRef = db.collection("users")
//            .document(userId)
//            .collection("goals")
//            .document(goalId)
//            .collection("milestones")
//            .document()
//
//        // Set the milestoneId to the document ID generated by Firestore
//        val milestoneWithId = milestone.copy(milestoneId = milestoneRef.id)
//
//        milestoneRef.set(milestoneWithId)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { exception -> onFailure(exception) }
//    }
//
//    // Function to retrieve all milestones for a specific goal
//    fun getMilestones(
//        userId: String,
//        goalId: String,
//        onSuccess: (List<Milestone>) -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        db.collection("users")
//            .document(userId)
//            .collection("goals")
//            .document(goalId)
//            .collection("milestones")
//            .get()
//            .addOnSuccessListener { result ->
//                val milestones = result.documents.mapNotNull { document ->
//                    document.toObject(Milestone::class.java)?.copy(milestoneId = document.id)
//                }
//                onSuccess(milestones)
//            }
//            .addOnFailureListener { exception -> onFailure(exception) }
//    }
//
//    // Function to update an existing milestone in Firestore
//    fun updateMilestone(
//        userId: String,
//        goalId: String,
//        milestoneId: String,
//        updatedMilestone: Milestone,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        db.collection("users")
//            .document(userId)
//            .collection("goals")
//            .document(goalId)
//            .collection("milestones")
//            .document(milestoneId)
//            .set(updatedMilestone)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { exception -> onFailure(exception) }
//    }
//
//    // Function to delete a milestone from Firestore
//    fun deleteMilestone(
//        userId: String,
//        goalId: String,
//        milestoneId: String,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        db.collection("users")
//            .document(userId)
//            .collection("goals")
//            .document(goalId)
//            .collection("milestones")
//            .document(milestoneId)
//            .delete()
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { exception -> onFailure(exception) }
//    }
//
//    fun getMilestonesForGoal(
//        goalId: String,
//        onSuccess: (List<Milestone>) -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        val milestonesRef = db.collection("users")
//            .document("userId") // You need to provide the correct user ID here
//            .collection("goals")
//            .document(goalId)
//            .collection("milestones")
//
//        milestonesRef.get()
//            .addOnSuccessListener { snapshot ->
//                val milestones = snapshot.documents.mapNotNull { document ->
//                    document.toObject(Milestone::class.java)?.copy(milestoneId = document.id)
//                }
//                onSuccess(milestones)
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//            }
//    }
//
//
//}


//Newly Added Code
package com.example.dailywork.data.repository

import com.example.dailywork.data.model.Milestone
import com.google.firebase.firestore.FirebaseFirestore

class MilestoneRepository {

    private val db = FirebaseFirestore.getInstance()

    // Function to add a new milestone to Firestore under a specific goal
    fun addMilestone(
        userId: String,
        goalId: String,
        milestone: Milestone,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val milestoneRef = db.collection("users")
            .document(userId)
            .collection("goals")
            .document(goalId)
            .collection("milestones")
            .document()

        // Set the milestoneId to the document ID generated by Firestore
        val milestoneWithId = milestone.copy(milestoneId = milestoneRef.id)

        milestoneRef.set(milestoneWithId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // Function to retrieve all milestones for a specific goal
    fun getMilestones(
        userId: String,
        goalId: String,
        onSuccess: (List<Milestone>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("users")
            .document(userId)
            .collection("goals")
            .document(goalId)
            .collection("milestones")
            .get()
            .addOnSuccessListener { result ->
                val milestones = result.documents.mapNotNull { document ->
                    document.toObject(Milestone::class.java)?.copy(milestoneId = document.id)
                }
                onSuccess(milestones)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
}

//package com.example.dailywork.data.repository
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import com.example.dailywork.data.model.Task
//import com.google.firebase.firestore.FirebaseFirestore
//import java.text.SimpleDateFormat
//import java.time.LocalDate
//import java.util.Date
//import java.util.Locale
//
//class TaskRepository(
//) {
//
//    private val db = FirebaseFirestore.getInstance()
//
//    // Function to add a new task under a specific milestone
//    fun addTask(
//        userId: String,
//        goalId: String,
//        milestoneId: String,
//        task: Task,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        val taskRef = db.collection("users")
//            .document(userId)
//            .collection("goals")
//            .document(goalId)
//            .collection("milestones")
//            .document(milestoneId)
//            .collection("tasks")
//            .document()
//
//        val taskWithId = task.copy(taskId = taskRef.id)
//
//        taskRef.set(taskWithId)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { exception -> onFailure(exception) }
//    }
//
//    // Function to retrieve all tasks for a specific milestone
//    fun getTasks(
//        userId: String,
//        goalId: String,
//        milestoneId: String,
//        onSuccess: (List<Task>) -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        db.collection("users")
//            .document(userId)
//            .collection("goals")
//            .document(goalId)
//            .collection("milestones")
//            .document(milestoneId)
//            .collection("tasks")
//            .get()
//            .addOnSuccessListener { result ->
//                val tasks = result.documents.mapNotNull { document ->
//                    document.toObject(Task::class.java)?.copy(taskId = document.id)
//                }
//                onSuccess(tasks)
//            }
//            .addOnFailureListener { exception -> onFailure(exception) }
//    }
//
//    // Function to update an existing task under a specific milestone
//    fun updateTask(
//        userId: String,
//        goalId: String,
//        milestoneId: String,
//        taskId: String,
//        updatedTask: Task,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        db.collection("users")
//            .document(userId)
//            .collection("goals")
//            .document(goalId)
//            .collection("milestones")
//            .document(milestoneId)
//            .collection("tasks")
//            .document(taskId)
//            .set(updatedTask)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { exception -> onFailure(exception) }
//    }
//
//    // Function to delete a task from Firestore
//    fun deleteTask(
//        userId: String,
//        goalId: String,
//        milestoneId: String,
//        taskId: String,
//        onSuccess: () -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        db.collection("users")
//            .document(userId)
//            .collection("goals")
//            .document(goalId)
//            .collection("milestones")
//            .document(milestoneId)
//            .collection("tasks")
//            .document(taskId)
//            .delete()
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { exception -> onFailure(exception) }
//    }
//
//    // Retrieve tasks for today by comparing task dueDate with today's date
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getTasksForToday(
//        userId: String,
//        goalId: String,
//        milestoneId: String,
//        onSuccess: (List<Task>) -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        // Get today's date in "yyyy-MM-dd" format
//        val today = LocalDate.now().toString()
//
//        getTasks(
//            userId = userId,
//            goalId = goalId,
//            milestoneId = milestoneId,
//            onSuccess = { tasks ->
//                // Format the dueDate from Long? to "yyyy-MM-dd" and filter tasks for today
//                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//
//                val tasksForToday = tasks.filter { task ->
//                    task.dueDate?.let {
//                        val taskDate = dateFormat.format(Date(it)) // Convert Long to "yyyy-MM-dd"
//                        taskDate == today // Compare formatted date with today's date
//                    } ?: false
//                }
//
//                onSuccess(tasksForToday)
//            },
//            onFailure = onFailure
//        )
//    }
//
//    // Retrieve tasks for a specific date
//    fun getTasksForDate(
//        userId: String,
//        goalId: String,
//        milestoneId: String,
//        date: String, // Expected in "yyyy-MM-dd" format
//        onSuccess: (List<Task>) -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        getTasks(
//            userId = userId,
//            goalId = goalId,
//            milestoneId = milestoneId,
//            onSuccess = { tasks ->
//                // Assuming 'dueDate' is a Long? representing a timestamp
//                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//
//                // Filter tasks for the given date
//                val tasksForDate = tasks.filter { task ->
//                    task.dueDate?.let {
//                        val taskDate = dateFormat.format(Date(it)) // Convert Long to "yyyy-MM-dd"
//                        taskDate == date // Compare formatted date with the input date
//                    } ?: false
//                }
//
//                onSuccess(tasksForDate)
//            },
//            onFailure = onFailure
//        )
//    }
//
//    // Retrieve tasks for a specific milestone
//    fun getTasksForMilestone(
//        userId: String,
//        goalId: String,
//        milestoneId: String,
//        onSuccess: (List<Task>) -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        getTasks(
//            userId = userId,
//            goalId = goalId,
//            milestoneId = milestoneId,
//            onSuccess = { tasks ->
//                // Return all tasks for the milestone
//                onSuccess(tasks)
//            },
//            onFailure = onFailure
//        )
//    }
//}



//Newly Added Code
package com.example.dailywork.data.repository

import com.example.dailywork.data.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class TaskRepository {

    private val db = FirebaseFirestore.getInstance()

    // Function to add a new task under a specific milestone
    fun addTask(
        userId: String,
        goalId: String,
        milestoneId: String,
        task: Task,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val taskRef = db.collection("users")
            .document(userId)
            .collection("goals")
            .document(goalId)
            .collection("milestones")
            .document(milestoneId)
            .collection("tasks")
            .document()

        val taskWithId = task.copy(taskId = taskRef.id)

        taskRef.set(taskWithId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // Function to retrieve all tasks for a specific milestone
    fun getTasks(
        userId: String,
        goalId: String,
        milestoneId: String,
        onSuccess: (List<Task>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("users")
            .document(userId)
            .collection("goals")
            .document(goalId)
            .collection("milestones")
            .document(milestoneId)
            .collection("tasks")
            .get()
            .addOnSuccessListener { result ->
                val tasks = result.documents.mapNotNull { document ->
                    document.toObject(Task::class.java)?.copy(taskId = document.id)
                }
                onSuccess(tasks)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // Function to update an existing task under a specific milestone
    fun updateTask(
        userId: String,
        goalId: String,
        milestoneId: String,
        taskId: String,
        updatedTask: Task,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("users")
            .document(userId)
            .collection("goals")
            .document(goalId)
            .collection("milestones")
            .document(milestoneId)
            .collection("tasks")
            .document(taskId)
            .set(updatedTask)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // Function to delete a task from Firestore
    fun deleteTask(
        userId: String,
        goalId: String,
        milestoneId: String,
        taskId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("users")
            .document(userId)
            .collection("goals")
            .document(goalId)
            .collection("milestones")
            .document(milestoneId)
            .collection("tasks")
            .document(taskId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // Retrieve tasks for today by comparing task dueDate with today's date
    fun getTasksForToday(
        userId: String,
        goalId: String,
        milestoneId: String,
        onSuccess: (List<Task>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        getTasks(
            userId = userId,
            goalId = goalId,
            milestoneId = milestoneId,
            onSuccess = { tasks ->
                val tasksForToday = tasks.filter { task ->
                    task.dueDate?.let {
                        val taskDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
                        taskDate == today
                    } ?: false
                }
                onSuccess(tasksForToday)
            },
            onFailure = onFailure
        )
    }

    // Retrieve tasks for a specific date
    fun getTasksForDate(
        userId: String,
        goalId: String,
        milestoneId: String,
        date: String, // Expected in "yyyy-MM-dd" format
        onSuccess: (List<Task>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getTasks(
            userId = userId,
            goalId = goalId,
            milestoneId = milestoneId,
            onSuccess = { tasks ->
                val tasksForDate = tasks.filter { task ->
                    task.dueDate?.let {
                        val taskDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
                        taskDate == date
                    } ?: false
                }
                onSuccess(tasksForDate)
            },
            onFailure = onFailure
        )
    }

    // Retrieve tasks for a specific milestone
    fun getTasksForMilestone(
        userId: String,
        goalId: String,
        milestoneId: String,
        onSuccess: (List<Task>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getTasks(
            userId = userId,
            goalId = goalId,
            milestoneId = milestoneId,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}

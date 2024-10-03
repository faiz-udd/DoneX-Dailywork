//package com.example.dailywork.viewmodels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.dailywork.data.model.Goal
//import com.example.dailywork.data.model.Milestone
//import com.example.dailywork.data.model.Task
//import com.example.dailywork.data.repository.GoalRepository
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//class GoalViewModel(private val goalRepository: GoalRepository) : ViewModel() {
//    private val db = FirebaseFirestore.getInstance()
//
//    // Holds the list of goals
//    private val _goals = MutableStateFlow<List<Goal>>(emptyList())
//    val goals: StateFlow<List<Goal>> get() = _goals
//
//    // Holds the current goal, milestones, and tasks that are being set up
//    private val _currentGoal = MutableStateFlow(Goal())
//    val currentGoal: StateFlow<Goal> get() = _currentGoal
//
//    private val _milestones = MutableStateFlow<List<Milestone>>(emptyList())
//    val milestones: StateFlow<List<Milestone>> get() = _milestones
//
//    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
//    val tasks: StateFlow<List<Task>> get() = _tasks
//
//    // Error message for showing errors
//    private val _errorMessage = MutableStateFlow<String?>(null)
//    val errorMessage: StateFlow<String?> get() = _errorMessage
//
//    // Loading state for displaying progress
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> get() = _isLoading
//
//    // Add a new goal along with milestones and tasks
//    fun addGoalWithMilestonesAndTasks(
//        userId: String,
//        goalTitle: String,
//        description: String,
//        dueDate: Long,
//        milestoneTitles: List<String>,
//        tasksForMilestones: Map<String, List<String>> // Mapping milestone to list of tasks
//    ) {
//        if (goalTitle.isEmpty()) {
//            _errorMessage.value = "Goal title cannot be empty."
//            return
//        }
//
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//
//                // Create a new Goal object
//                val goal = Goal(
//                    title = goalTitle,
//                    description = description,
//                    dueDate = dueDate
//                )
//
//                // Prepare milestones and tasks
//                val milestonesList = milestoneTitles.map { title ->
//                    Milestone(title = title, tasks = tasksForMilestones[title]?.map { Task(it) } ?: emptyList())
//                }
//
//                // Add the goal with milestones and tasks to the repository
//                goalRepository.addGoalWithMilestonesAndTasks(
//                    userId = userId,
//                    goal = goal,
//                    milestones = milestonesList,
//                    onSuccess = {
//                        fetchGoals(userId = userId) // Fetch the updated goals after successful addition
//                        _isLoading.value = false
//                    },
//                    onFailure = { exception:Exception ->
//                        _errorMessage.value = exception.message
//                        _isLoading.value = false
//                    }
//                )
//
//                // Clear the current goal, milestones, and tasks
//                _currentGoal.value = Goal()
//                _milestones.value = emptyList()
//                _tasks.value = emptyList()
//
//            } catch (e: Exception) {
//                _errorMessage.value = e.message
//                _isLoading.value = false
//            }
//        }
//    }
//
//    // Fetch goals from the repository
//    fun fetchGoals(userId: String) {
//        _isLoading.value = true  // Start loading
//
//        // Call repository method with success and failure callbacks
//        goalRepository.getAllGoals(
//            userId = userId,
//            onSuccess = { goals ->
//                _goals.value = goals  // Update the StateFlow with the fetched goals
//                _isLoading.value = false  // Stop loading
//            },
//            onFailure = { exception ->
//                _errorMessage.value = exception.message  // Set the error message
//                _isLoading.value = false  // Stop loading
//            }
//        )
//    }
//
//
//    // Update the goal title in the current goal
//    fun updateGoalTitle(title: String) {
//        _currentGoal.value = _currentGoal.value.copy(title = title)
//    }
//
//    // Update the description in the current goal
//    fun updateGoalDescription(description: String) {
//        _currentGoal.value = _currentGoal.value.copy(description = description)
//    }
//
//    // Update the deadline in the current goal
//    fun updateGoalDeadline(dueDate: Long) {
//        _currentGoal.value = _currentGoal.value.copy(dueDate = dueDate)
//    }
//
//    // Add a milestone to the current setup
//    fun addMilestone(title: String, milestone: String) {
//        val updatedMilestones = _milestones.value.toMutableList()
//        updatedMilestones.add(Milestone(title = title))
//        _milestones.value = updatedMilestones
//    }
//
//    // Add a task to a specific milestone
//    fun addTaskToMilestone(milestoneTitle: String, taskTitle: String) {
//        val updatedMilestones = _milestones.value.map { milestone ->
//            if (milestone.title == milestoneTitle) {
//                milestone.copy(tasks = milestone.tasks + Task(taskTitle))
//            } else {
//                milestone
//            }
//        }
//        _milestones.value = updatedMilestones
//    }
//
//    // Clear error message
//    fun clearErrorMessage() {
//        _errorMessage.value = null
//    }
//
//    fun addTask(
//        userId: String,
//        task: Task,  // Assuming `Task` is a data model for a task
//        onSuccess: () -> Unit,      // Added success callback
//        onFailure: (Exception) -> Unit  // Added failure callback
//    ) {
//        val taskRef = db.collection("users").document(userId).collection("tasks").document()
//
//        // Set the taskId to the document ID generated by Firestore
//        val taskWithId = task.copy(taskId = taskRef.id)
//
//        taskRef.set(taskWithId)
//            .addOnSuccessListener {
//                onSuccess()   // Call the provided onSuccess function
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)  // Call the provided onFailure function with the exception
//            }
//    }
//
//
//    fun addGoal(
//        userId: String,
//        goalTitle: String,
//        description: String,
//        dueDate: Long,
//        onSuccess: (Any?) -> Unit,      // Added success callback
//        onFailure: (Exception) -> Unit  // Added failure callback
//    ) {
//        val goalRef = db.collection("users").document(userId).collection("goals").document()
//
//        // Create a Goal object with the provided details
//        val newGoal = Goal(
//            goalId = goalRef.id,  // Set the goalId to the document ID generated by Firestore
//            title = goalTitle,
//            description = description,
//            dueDate = dueDate
//        )
//
//        goalRef.set(newGoal)
//            .addOnSuccessListener {
//                //onSuccess()   // Call the provided onSuccess function
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)  // Call the provided onFailure function with the exception
//            }
//    }
//
//}



//Newly Added Code
package com.example.dailywork.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailywork.data.model.Goal
import com.example.dailywork.data.repository.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GoalViewModel(private val goalRepository: GoalRepository) : ViewModel() {

    private val _goalTitle = MutableStateFlow("")
    val goalTitle: StateFlow<String> get() = _goalTitle

    private val _goalDescription = MutableStateFlow("")
    val goalDescription: StateFlow<String> get() = _goalDescription

    private val _goalDueDate = MutableStateFlow<Long?>(null)
    val goalDueDate: StateFlow<Long?> get() = _goalDueDate

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // Update the goal title
    fun updateGoalTitle(title: String) {
        _goalTitle.value = title
    }

    // Update the goal description
    fun updateGoalDescription(description: String) {
        _goalDescription.value = description
    }

    // Update the goal deadline
    fun updateGoalDueDate(dueDate: Long) {
        _goalDueDate.value = dueDate
    }

    // Save the goal to Firestore
    fun saveGoal(userId: String) {
        if (_goalTitle.value.isEmpty() || _goalDueDate.value == null) {
            _errorMessage.value = "Goal title and due date cannot be empty."
            return
        }

        val newGoal = Goal(
            title = _goalTitle.value,
            description = _goalDescription.value,
            dueDate = _goalDueDate.value!!
        )

        viewModelScope.launch {
            _isLoading.value = true
            goalRepository.addGoal(
                userId = userId,
                goal = newGoal,
                onSuccess = {
                    _isLoading.value = false
                    _errorMessage.value = null
                },
                onFailure = { exception ->
                    _isLoading.value = false
                    _errorMessage.value = exception.message
                }
            )
        }
    }

    fun getGoalsForUser(userId: String, onSuccess: Any, onFailure: Any) {

    }
}


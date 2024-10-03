//package com.example.dailywork.viewmodels
//
//import android.widget.Toast
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.dailywork.data.model.Task
//import com.example.dailywork.data.repository.TaskRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context
//
//class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {
//
//    // Holds the list of tasks for a specific milestone
//    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
//    val tasks: StateFlow<List<Task>> get() = _tasks
//
//    // Holds the current task being set up or edited
//    private val _currentTask = MutableStateFlow(Task())
//    val currentTask: StateFlow<Task> get() = _currentTask
//
//    // Error message to handle error states
//    private val _errorMessage = MutableStateFlow<String?>(null)
//    val errorMessage: StateFlow<String?> get() = _errorMessage
//
//    // Loading state for displaying progress
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> get() = _isLoading
//
//    // Add a new task to a specific milestone
//    fun addTask(userId:String, goalId:String, milestoneId: String, title: String, description: String, dueDate: String, onSuccess: () -> Unit,
//                onFailure: (Exception) -> Unit) {
//        if (title.isEmpty()) {
//            _errorMessage.value = "Task title cannot be empty."
//            return
//        }
//
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//
//                // Create a new Task object
//                val task = Task(
//                    title = title,
//                    description = description,
//                    dueDate = dueDate.toLong(),
//                    milestoneId = milestoneId
//                )
//
//                // Add the task to the repository
//                taskRepository.addTask(
//                    userId = userId,
//                    goalId = goalId,
//                    milestoneId = milestoneId,
//                    task = task,
//                    onSuccess = {
//                        isLoading = false
//                        Toast.makeText(context, "Task created successfully!", Toast.LENGTH_SHORT).show()
//                        onTaskCreated()  // Navigate to the next screen
//                        navController.popBackStack()
//                    },
//                    onFailure = { exception:Exception ->
//                        isLoading = false
//                        Toast.makeText(context, "Failed to create task: ${exception.message}", Toast.LENGTH_SHORT).show()
//                    }
//                )
//
//                // Fetch updated tasks for the milestone
//                getTasksForMilestone(milestoneId)
//
//                // Clear the current task after successful addition
//                _currentTask.value = Task()
//
//                _isLoading.value = false
//            } catch (e: Exception) {
//                _errorMessage.value = e.message
//                _isLoading.value = false
//            }
//        }
//    }
//
//    // Fetch tasks for a specific milestone
//    fun getTasksForMilestone(milestoneId: String) {
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//                //_tasks.value = taskRepository.getTasksForMilestone(milestoneId)
//                _isLoading.value = false
//            } catch (e: Exception) {
//                _errorMessage.value = e.message
//                _isLoading.value = false
//            }
//        }
//    }
//
//    // Update task title in the current task
//    fun updateTaskTitle(title: String) {
//        _currentTask.value = _currentTask.value.copy(title = title)
//    }
//
//    // Update task description in the current task
//    fun updateTaskDescription(description: String) {
//        _currentTask.value = _currentTask.value.copy(description = description)
//    }
//
//    // Update task due date in the current task
//    fun updateTaskDueDate(dueDate: String) {
//        _currentTask.value = _currentTask.value.copy(dueDate = dueDate.toLong())
//    }
//
//    // Clear error message
//    fun clearErrorMessage() {
//        _errorMessage.value = null
//    }
//
//    fun fetchTasksForMilestone(userId: String, goalId: String, milestoneId: String, onSuccess: (List<Task>) -> Unit, onFailure: (Exception) -> Unit) {
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//                taskRepository.getTasksForMilestone(
//                    userId = userId,
//                    goalId = goalId,
//                    milestoneId = milestoneId,
//                    onSuccess = { tasks ->
//                        _tasks.value = tasks
//                        _isLoading.value = false
//                    },
//                    onFailure = { exception ->
//                        _errorMessage.value = exception.message
//                        _isLoading.value = false
//                    }
//                )
//            } catch (e: Exception) {
//                _errorMessage.value = e.message
//                _isLoading.value = false
//            }
//        }
//    }
//
//}


//Newly Added Code
package com.example.dailywork.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailywork.data.model.Task
import com.example.dailywork.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    // Holds the list of tasks for a specific milestone
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> get() = _tasks

    // Holds the current task being set up or edited
    private val _currentTask = MutableStateFlow(Task())
    val currentTask: StateFlow<Task> get() = _currentTask

    // Error message to handle error states
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    // Loading state for displaying progress
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // Add a new task to a specific milestone
    fun addTask(userId: String, goalId: String, milestoneId: String, title: String, description: String, dueDate: Long?) {
        if (title.isEmpty()) {
            _errorMessage.value = "Task title cannot be empty."
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true

                // Create a new Task object
                val task = Task(
                    title = title,
                    description = description,
                    dueDate = dueDate,
                    milestoneId = milestoneId
                )

                // Add the task to the repository
                taskRepository.addTask(
                    userId = userId,
                    goalId = goalId,
                    milestoneId = milestoneId,
                    task = task,
                    onSuccess = {
                        _isLoading.value = false
                        fetchTasksForMilestone(userId, goalId, milestoneId, onSuccess = {}, onFailure = {}) // Refresh tasks after addition
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                        _isLoading.value = false
                    }
                )

                // Clear the current task after successful addition
                _currentTask.value = Task()

            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        }
    }

    // Fetch tasks for a specific milestone
    fun fetchTasksForMilestone(userId: String, goalId: String, milestoneId: String, onSuccess: (List<Task>) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                taskRepository.getTasksForMilestone(
                    userId = userId,
                    goalId = goalId,
                    milestoneId = milestoneId,
                    onSuccess = { tasks ->
                        _tasks.value = tasks
                        _isLoading.value = false
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                        _isLoading.value = false
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        }
    }

    // Update an existing task in Firestore
    fun updateTask(userId: String, goalId: String, milestoneId: String, task: Task) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                taskRepository.updateTask(
                    userId = userId,
                    goalId = goalId,
                    milestoneId = milestoneId,
                    taskId = task.taskId,
                    updatedTask = task,
                    onSuccess = {
                        _isLoading.value = false
                        fetchTasksForMilestone(userId, goalId, milestoneId,
                            onSuccess = {}, onFailure = {}) // Refresh tasks after update
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                        _isLoading.value = false
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        }
    }

    // Delete a task from Firestore
    fun deleteTask(userId: String, goalId: String, milestoneId: String, taskId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                taskRepository.deleteTask(
                    userId = userId,
                    goalId = goalId,
                    milestoneId = milestoneId,
                    taskId = taskId,
                    onSuccess = {
                        _isLoading.value = false
                        fetchTasksForMilestone(userId, goalId, milestoneId,
                            onSuccess = {}, onFailure = {}) // Refresh tasks after deletion
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                        _isLoading.value = false
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        }
    }

    // Fetch tasks for today
    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchTasksForToday(userId: String, goalId: String, milestoneId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                taskRepository.getTasksForToday(
                    userId = userId,
                    goalId = goalId,
                    milestoneId = milestoneId,
                    onSuccess = { tasks ->
                        _tasks.value = tasks
                        _isLoading.value = false
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                        _isLoading.value = false
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        }
    }

    // Fetch tasks for a specific date
    fun fetchTasksForDate(userId: String, goalId: String, milestoneId: String, date: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                taskRepository.getTasksForDate(
                    userId = userId,
                    goalId = goalId,
                    milestoneId = milestoneId,
                    date = date,
                    onSuccess = { tasks ->
                        _tasks.value = tasks
                        _isLoading.value = false
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                        _isLoading.value = false
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        }
    }

    // Update task fields in the current task
    fun updateTaskTitle(title: String) {
        _currentTask.value = _currentTask.value.copy(title = title)
    }

    fun updateTaskDescription(description: String) {
        _currentTask.value = _currentTask.value.copy(description = description)
    }

    fun updateTaskDueDate(dueDate: Long) {
        _currentTask.value = _currentTask.value.copy(dueDate = dueDate)
    }

    // Clear error message
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}




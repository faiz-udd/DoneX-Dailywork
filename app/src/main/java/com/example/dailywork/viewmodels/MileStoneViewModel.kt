//package com.example.dailywork.viewmodels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.dailywork.data.model.Milestone
//import com.example.dailywork.data.repository.MilestoneRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//class MilestoneViewModel(private val milestoneRepository: MilestoneRepository) : ViewModel() {
//
//    private val _milestones = MutableStateFlow<List<Milestone>>(emptyList())
//    val milestones: StateFlow<List<Milestone>> get() = _milestones
//
//    private val _currentMilestone = MutableStateFlow(Milestone())
//    val currentMilestone: StateFlow<Milestone> get() = _currentMilestone
//
//    private val _errorMessage = MutableStateFlow<String?>(null)
//    val errorMessage: StateFlow<String?> get() = _errorMessage
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> get() = _isLoading
//
//    fun addMilestone(userId: String, goalId: String, title: String, description: String, dueDateString: String) {
//        if (title.isEmpty()) {
//            _errorMessage.value = "Milestone title cannot be empty."
//            return
//        }
//
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//
//                // Convert dueDateString to Long if required by your Milestone model
//                val dueDate = convertDateToLong(dueDateString)
//
//                val milestone = Milestone(
//                    title = title,
//                    description = description,
//                    dueDate = dueDate, // Assuming this takes Long
//                    goalId = goalId
//                )
//
//                // Call the repository's addMilestone function with required parameters
//                milestoneRepository.addMilestone(
//                    userId = userId,
//                    goalId = goalId,
//                    milestone = milestone,
//                    onSuccess = {
//                        // Handle success (e.g., fetch updated milestones)
//                        fetchMilestones(goalId)
//                        _currentMilestone.value = Milestone() // Clear the current milestone
//                        _isLoading.value = false
//                    },
//                    onFailure = { exception ->
//                        _errorMessage.value = exception.message
//                        _isLoading.value = false
//                    }
//                )
//
//            } catch (e: Exception) {
//                _errorMessage.value = e.message
//                _isLoading.value = false
//            }
//        }
//    }
//
//
//    fun fetchMilestones(goalId: String) {
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//
//                milestoneRepository.getMilestonesForGoal(
//                    goalId = goalId,
//                    onSuccess = { milestones ->
//                        _milestones.value = milestones // Update your state with the fetched milestones
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
//    fun updateMilestoneTitle(title: String) {
//        _currentMilestone.value = _currentMilestone.value.copy(title = title)
//    }
//
//    fun updateMilestoneDescription(description: String) {
//        _currentMilestone.value = _currentMilestone.value.copy(description = description)
//    }
//
//    fun updateMilestoneDeadline(deadline: String) {
//        _currentMilestone.value = _currentMilestone.value.copy(dueDate = convertDateToLong(deadline)) // Convert here too
//    }
//
//    fun clearErrorMessage() {
//        _errorMessage.value = null
//    }
//
//     fun convertDateToLong(dateString: String): Long? {
//        // Implement your date parsing logic here to convert String to Long timestamp
//        return null // Replace with actual conversion logic
//    }
//
//    fun getMilestonesForGoal(userId: String, goalId: String, onSuccess: Any, onFailure: Any) {
//
//    }
//}



//Newly Added COde
package com.example.dailywork.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailywork.data.model.Milestone
import com.example.dailywork.data.repository.MilestoneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MilestoneViewModel(private val milestoneRepository: MilestoneRepository) : ViewModel() {

    private val _milestones = MutableStateFlow<List<Milestone>>(emptyList())
    val milestones: StateFlow<List<Milestone>> get() = _milestones

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun addMilestone(userId: String, goalId: String, title: String, description: String, dueDateString: String) {
        if (title.isEmpty()) {
            _errorMessage.value = "Milestone title cannot be empty."
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true

                // Convert dueDateString to Long if required by your Milestone model
                val dueDate = dueDateString.toLongOrNull() ?: 0L

                val milestone = Milestone(
                    title = title,
                    description = description,
                    dueDate = dueDate,
                    goalId = goalId
                )

                // Call the repository's addMilestone function with required parameters
                milestoneRepository.addMilestone(
                    userId = userId,
                    goalId = goalId,
                    milestone = milestone,
                    onSuccess = {
                        fetchMilestones(userId, goalId)
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

    fun fetchMilestones(userId: String, goalId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                milestoneRepository.getMilestones(
                    userId = userId,
                    goalId = goalId,
                    onSuccess = { milestones ->
                        _milestones.value = milestones
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

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun getMilestonesForGoal(userId: String, goalId: String, onSuccess: (List<Milestone>) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // Call the repository function to get the milestones for the specified userId and goalId
                milestoneRepository.getMilestones(
                    userId = userId,
                    goalId = goalId,
                    onSuccess = { milestones ->
                        _milestones.value = milestones
                        _isLoading.value = false
                        onSuccess(milestones) // Pass the list to onSuccess callback
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                        _isLoading.value = false
                        onFailure(exception) // Pass the exception to onFailure callback
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
                onFailure(e) // Catch any exceptions and pass them to the onFailure callback
            }
        }
    }

}


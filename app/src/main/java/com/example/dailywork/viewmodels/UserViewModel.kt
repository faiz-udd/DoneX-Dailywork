package com.example.dailywork.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailywork.data.model.User
import com.example.dailywork.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {


    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _loginErrorMessage = MutableStateFlow<String?>(null)
    val loginErrorMessage: StateFlow<String?> = _loginErrorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess.asStateFlow()

    private val _registrationErrorMessage = MutableStateFlow<String?>(null)
    val registrationErrorMessage: StateFlow<String?> = _registrationErrorMessage.asStateFlow()

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginErrorMessage.value = null // Clear previous error messages
            if (email.isBlank() || password.isBlank()) {
                _loginErrorMessage.value = "Email or password cannot be empty."
                _isLoading.value = false
                return@launch
            }
            userRepository.loginUser(email, password)
                .onSuccess { firebaseUser ->
                    firebaseUser?.let {
                        fetchUserDetails(it.uid)
                        _isLoggedIn.value = true
                    } ?: run {
                        _loginErrorMessage.value = "Login failed: User data is null."
                    }
                }
                .onFailure { exception ->
                    _loginErrorMessage.value = "Login Failed: ${exception.message}"
                }
            _isLoading.value = false
        }
    }

    fun registerUser(
        username: String,
        email: String,
        password: String,
        profilePictureUrl: String = ""
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _registrationErrorMessage.value = null // Clear previous error messages
            if (username.isBlank() || email.isBlank() || password.isBlank()) {
                _registrationErrorMessage.value = "All fields are required."
                _isLoading.value = false
                return@launch
            }
            val user = User(
                username = username,
                email = email,
                profilePictureUrl = profilePictureUrl
            )
            userRepository.registerUser(email, password, user)
                .onSuccess { firebaseUser ->
                    firebaseUser?.let {
                        _registrationSuccess.value = true
                        fetchUserDetails(it.uid)
                    } ?: run {
                        _registrationErrorMessage.value = "Registration failed: User data is null."
                    }
                }
                .onFailure { exception ->
                    _registrationSuccess.value = false
                    _registrationErrorMessage.value = "Registration Failed: ${exception.message}"
                }
            _isLoading.value = false
        }
    }

    private fun fetchUserDetails(userId: String) {
        viewModelScope.launch {
            userRepository.getUserData(userId)
                .onSuccess { user ->
                    _currentUser.value = user
                }
                .onFailure { exception ->
                    _loginErrorMessage.value = "Failed to fetch user details: ${exception.message}"
                }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                userRepository.logoutUser()
                _currentUser.value = null
                _isLoggedIn.value = false
            } catch (e: Exception) {
                _loginErrorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearLoginErrorMessage() {
        _loginErrorMessage.value = null
    }

    fun updateUserDetails(user: User) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                userRepository.updateUserDetails(user)
                _currentUser.value = user
            } catch (e: Exception) {
                _loginErrorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearRegistrationErrorMessage() {
        _registrationErrorMessage.value = null
    }

    fun resetRegistrationSuccess() {
        _registrationSuccess.value = false
    }

    fun getCurrentUser(): FirebaseUser? {
        return userRepository.getCurrentUser()
    }


    fun getUserName(userId: String, onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        // Access the Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Reference to the user document based on userId
        val userRef = db.collection("users").document(userId)

        // Attempt to retrieve the document
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Check if the document contains a "name" field and pass it to onSuccess
                    val name = document.getString("name")
                    if (name != null) {
                        onSuccess(name)
                    } else {
                        // If the "name" field is missing, trigger the onFailure callback
                        onFailure()
                    }
                } else {
                    // Document does not exist or is null, trigger onFailure
                    onFailure()
                }
            }
            .addOnFailureListener {
                // Failed to retrieve the document, trigger onFailure
                onFailure()
            }
    }

    fun getUserDetails(userId: String, onSuccess: Any, onFailure: () -> Unit) {
       return fetchUserDetails(userId)

    }

}
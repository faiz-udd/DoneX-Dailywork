package com.example.dailywork.data.database

import com.example.dailywork.data.model.User
import com.example.dailywork.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseService(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    //private val context = LocalContext.current
    // User Registration
//    suspend fun registerUser(email: String, password: String, user: User): Result<FirebaseUser?> {
//        return try {
//            val result = auth.createUserWithEmailAndPassword(email, password).await()
//            val firebaseUser = result.user
//            user.userId = result.user?.uid ?: throw Exception("User ID is null")
//            saveUserToFirestore(user) // Ensure user is saved correctly
//            Result.success(firebaseUser)
//            Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
//        } catch (e:Exception) {
//            Result.failure(e) // Ensure this captures any errors that occur
//        }
//    }
    sealed class RegistrationError : Throwable() {
        object UserIdNull : RegistrationError()
        data class FirebaseError(val exception: Exception) : RegistrationError()
        data class FirestoreError(val exception: Exception) : RegistrationError()
    }

    suspend fun registerUser(email: String, password: String, user: User): Result<FirebaseUser?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            val userId = result.user?.uid
            if (userId == null) {
                return Result.failure(RegistrationError.UserIdNull) // Now type-safe
            }
            user.userId = userId
            try {
                saveUserToFirestore(user)
            } catch (e: Exception) {
                return Result.failure(RegistrationError.FirestoreError(e)) // Now type-safe
            }
            Result.success(firebaseUser)
        } catch (e: Exception) {
            Result.failure(RegistrationError.FirebaseError(e)) // Now type-safe
        }
    }
    // User Login
    suspend fun loginUser(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            Result.success(firebaseUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Save User to Firestore
    private suspend fun saveUserToFirestore(user: User): Boolean {
        return try {
            firestore.collection("users").document(user.userId).set(user).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Get User by UID
    suspend fun getUser(uid: String): Result<User> {
        return try {
            val document = firestore.collection(Constants.USERS_COLLECTION)
                .document(uid)
                .get()
                .await()

            if (document.exists()) {
                val user = document.toObject(User::class.java)
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("User data is null"))
                }
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Logout User
    fun logoutUser() {
        auth.signOut()
    }

    // Get Current User
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Update User Profile in Firestore
    suspend fun updateUserProfile(userId: String, updatedUserData: User): Result<Void?> {
        return try {
            firestore.collection(Constants.USERS_COLLECTION)
                .document(userId)
                .set(updatedUserData)
                .await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

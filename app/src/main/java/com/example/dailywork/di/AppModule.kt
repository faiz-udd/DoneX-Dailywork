// src/di/AppModule.kt
package com.example.dailywork.di

import com.example.dailywork.data.database.FirebaseService
import com.example.dailywork.data.repository.TaskRepository
import com.example.dailywork.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseService(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): FirebaseService = FirebaseService(auth, firestore)

    @Provides
    @Singleton
    fun provideUserRepository(firebaseService: FirebaseService): UserRepository = UserRepository(firebaseService)

    @Provides
    @Singleton
    fun provideTaskRepository(firebaseService: FirebaseService): TaskRepository = TaskRepository()
}

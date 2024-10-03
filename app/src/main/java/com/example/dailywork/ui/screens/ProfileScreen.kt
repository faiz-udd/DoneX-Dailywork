// src/ui/screens/ProfileScreen.kt
package com.example.dailywork.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailywork.data.model.Goal
import com.example.dailywork.data.model.User
import com.example.dailywork.viewmodels.GoalViewModel
import com.example.dailywork.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    goalViewModel: GoalViewModel,
    userId: String
) {
    val coroutineScope = rememberCoroutineScope()

    // UI states for user's data
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var goals by remember { mutableStateOf<List<Goal>>(emptyList()) }
    var isEditing by remember { mutableStateOf(false) }

    // Load user data from ViewModel
    LaunchedEffect(userId) {
        userViewModel.getUserDetails(
            userId = userId,
            onSuccess = { user: User ->
                userName = user.username ?: ""
                userEmail = user.email ?: ""
            },
            onFailure = {
                // Handle error, show a message if necessary
            }
        )
        goalViewModel.getGoalsForUser(
            userId = userId,
            onSuccess = { fetchedGoals:List<Goal> ->
                goals = fetchedGoals
            },
            onFailure = {
                // Handle error, show a message if necessary
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isEditing = !isEditing
                if (!isEditing) {
                    // Save user details when editing is toggled off
                    coroutineScope.launch {
                        userViewModel.currentUser.value?.let { userViewModel.updateUserDetails(it) }
                    }
                }
            }) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = if (isEditing) "Save" else "Edit"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // User Info Section
                Text("User Profile", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(16.dp))

                // Editable Text Fields for Name and Email
                if (isEditing) {
                    EditableTextField(value = userName, onValueChange = { userName = it }, label = "Name")
                    EditableTextField(value = userEmail, onValueChange = { userEmail = it }, label = "Email")
                } else {
                    DisplayTextField(value = userName, label = "Name")
                    DisplayTextField(value = userEmail, label = "Email")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display Goals Section
                Text("Your Goals", style = MaterialTheme.typography.titleMedium)
                if (goals.isEmpty()) {
                    Text("No goals found.", textAlign = TextAlign.Center, color = Color.Gray)
                } else {
                    Column {
                        goals.forEach { goal ->
                            //GoalItem(goal = goal) check:TODO
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditableTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions.Default,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
                .padding(8.dp)
        )
    }
}

@Composable
fun DisplayTextField(value: String, label: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
                .padding(8.dp)
        )
    }
}

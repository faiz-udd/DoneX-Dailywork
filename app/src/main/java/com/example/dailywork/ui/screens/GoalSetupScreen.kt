//package com.example.dailywork.ui.screens
//
//import android.widget.Toast
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.unit.dp
//import com.example.dailywork.data.model.Task
//import com.example.dailywork.viewmodels.GoalViewModel
//import com.example.dailywork.viewmodels.UserViewModel
//import kotlinx.coroutines.launch
//import java.util.UUID
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun GoalSetupScreen(
//    goalViewModel: GoalViewModel,
//    userViewModel: UserViewModel,
//    userId: String,
//    onGoalSaved: () -> Unit
//) {
//    var goalTitle by remember { mutableStateOf("") }
//    var goalDescription by remember { mutableStateOf("") }
//    var milestoneTitle by remember { mutableStateOf("") }
//    var taskTitle by remember { mutableStateOf("") }
//    var milestones by remember { mutableStateOf(listOf<String>()) } // Immutable list for milestones
//    var tasks by remember { mutableStateOf(listOf<String>()) }      // Immutable list for tasks
//    var showSuccessMessage by remember { mutableStateOf(false) }
//
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Set Your Goal, Milestones, and Tasks") },
//                navigationIcon = {
//                    IconButton(onClick = { /* Handle back navigation */ }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            // Goal Title and Description
//            TextField(
//                value = goalTitle,
//                onValueChange = { goalTitle = it },
//                label = { Text("Goal Title") },
//                placeholder = { Text("Enter your goal title") },
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            TextField(
//                value = goalDescription,
//                onValueChange = { goalDescription = it },
//                label = { Text("Goal Description") },
//                placeholder = { Text("Describe your goal") },
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            // Milestone Input
//            TextField(
//                value = milestoneTitle,
//                onValueChange = { milestoneTitle = it },
//                label = { Text("Milestone Title") },
//                placeholder = { Text("Enter a milestone") },
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Button(
//                onClick = {
//                    if (milestoneTitle.isNotBlank()) {
//                        milestones = milestones + milestoneTitle
//                        milestoneTitle = ""
//                    } else {
//                        Toast.makeText(context, "Milestone title cannot be empty", Toast.LENGTH_SHORT).show()
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Add Milestone")
//            }
//
//            // Display Milestones
//            milestones.forEach { milestone ->
//                Text(text = milestone, modifier = Modifier.padding(8.dp))
//            }
//
//            // Task Input
//            TextField(
//                value = taskTitle,
//                onValueChange = { taskTitle = it },
//                label = { Text("Task Title") },
//                placeholder = { Text("Enter a task") },
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Button(
//                onClick = {
//                    if (taskTitle.isNotBlank()) {
//                        tasks = tasks + taskTitle
//                        taskTitle = ""
//                    } else {
//                        Toast.makeText(context, "Task title cannot be empty", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Add Task")
//            }
//
//            // Display Tasks
//            tasks.forEach { task ->
//                Text(text = task, modifier = Modifier.padding(8.dp))
//            }
//
//            // Save Button
//            Button(
//                onClick = {
//                    if (goalTitle.isNotBlank() && goalDescription.isNotBlank()) {
//                        val dueDate = System.currentTimeMillis()
//
//                        coroutineScope.launch {
//                            goalViewModel.addGoal(
//                                userId = userId,
//                                goalTitle = goalTitle,
//                                description = goalDescription,
//                                dueDate = dueDate,
//                                onSuccess = { goalId -> // Adjust to handle goal ID correctly
//                                    milestones.forEach { milestoneTitle ->
//                                        goalViewModel.addMilestone(
//                                            milestone = Milestone(
//                                                userId = userId,
//                                                goalId = goalId.toString(), // Use the created goal ID
//                                                title = milestoneTitle,
//                                                description = "", // Optional description
//                                                dueDate = dueDate,// Ensure correct type
//                                                createdAt = Calender.getInstance()
//                                       )
//                                    }
//
//                                    tasks.forEach { taskTitle ->
//                                        val task = Task(
//                                            taskId = UUID.randomUUID().toString(),
//                                            title = taskTitle,
//                                            completed = false
//                                        )
//                                        goalViewModel.addTask(
//                                            userId = userId,
//                                            task = task,
//                                            onSuccess = {
//                                                // Handle task success if needed
//                                            },
//                                            onFailure = { exception ->
//                                                Toast.makeText(
//                                                    context,
//                                                    "Failed to add task: ${exception.message}",
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
//                                            }
//                                        )
//                                    }
//
//                                    showSuccessMessage = true
//                                    onGoalSaved() // Trigger navigation callback
//                                    Toast.makeText(context, "Goal added successfully", Toast.LENGTH_SHORT).show()
//                                },
//                                onFailure = { exception ->
//                                    Toast.makeText(context, "Failed to add goal: ${exception.message}", Toast.LENGTH_SHORT).show()
//                                }
//                            )
//                        }
//                    } else {
//                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Save Goal, Milestones, and Tasks")
//            }
//        }
//
//    }
//}



//Newly Added Code
package com.example.dailywork.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailywork.data.model.Goal
import com.example.dailywork.viewmodels.GoalViewModel

@Composable
fun GoalSetupScreen(
    navController: NavHostController,
    goalViewModel: GoalViewModel,
    userId: String,
    onGoalSaved: (Goal) -> Unit
) {
    val goalTitle by goalViewModel.goalTitle.collectAsState()
    val goalDescription by goalViewModel.goalDescription.collectAsState()
    val goalDueDate by goalViewModel.goalDueDate.collectAsState()
    val isLoading by goalViewModel.isLoading.collectAsState()
    val errorMessage by goalViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = goalTitle,
            onValueChange = { goalViewModel.updateGoalTitle(it) },
            label = { Text("Goal Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = goalDescription,
            onValueChange = { goalViewModel.updateGoalDescription(it) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Update the due date with a fixed value for demonstration (you can use a DatePicker here)
                goalViewModel.updateGoalDueDate(System.currentTimeMillis())
                goalViewModel.saveGoal(userId)
                onGoalSaved
            },
            enabled = !isLoading
        ) {
            Text("Save Goal")
        }

        if (errorMessage != null) {
            Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)
        }

        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}

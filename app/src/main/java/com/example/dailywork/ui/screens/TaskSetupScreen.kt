//package com.example.dailywork.ui.screens
//
//import android.widget.Toast
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.Button
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
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
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import com.example.dailywork.data.model.Task
//import com.example.dailywork.data.repository.TaskRepository
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TaskSetupScreen(
//    navController: NavHostController,
//    taskRepository: TaskRepository,
//    milestoneId: String,
//    userId: String,
//    goalId:String,
//    onTaskCreated: () -> Unit
//) {
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//
//    // State variables for the task fields
//    var taskTitle by remember { mutableStateOf("") }
//    var taskDescription by remember { mutableStateOf("") }
//    var taskTime by remember { mutableStateOf("") }
//    var isLoading by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = "Create New Task") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Top
//        ) {
//            Text(
//                text = "Create Task for Milestone",
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//
//            // Task Title Input Field
//            OutlinedTextField(
//                value = taskTitle,
//                onValueChange = { taskTitle = it },
//                label = { Text("Task Title") },
//                singleLine = true,
//                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
//            )
//
//            // Task Description Input Field
//            OutlinedTextField(
//                value = taskDescription,
//                onValueChange = { taskDescription = it },
//                label = { Text("Task Description") },
//                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
//            )
//
//            // Task Time Input Field
//            OutlinedTextField(
//                value = taskTime,
//                onValueChange = { taskTime = it },
//                label = { Text("Task Time (e.g. 09:00 AM)") },
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Number,
//                    imeAction = ImeAction.Done
//                ),
//                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
//            )
//
//            // Submit Button
//            Button(
//                onClick = {
//                    if (taskTitle.isBlank() || taskTime.isBlank()) {
//                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
//                    } else {
//                        isLoading = true
//                        coroutineScope.launch {
//                            val newTask = Task(
//                                taskId = "",
//                                title = taskTitle,
//                                description = taskDescription,
//                                time = taskTime,
//                                milestoneId = milestoneId
//                            )
//                            taskRepository.addTask(
//                                userId = userId,
//                                goalId = goalId,
//                                milestoneId = milestoneId,
//                                task = newTask,
//                                onSuccess = {
//                                    isLoading = false
//                                    Toast.makeText(context, "Task created successfully!", Toast.LENGTH_SHORT).show()
//                                    onTaskCreated()  // Navigate to the next screen
//                                    navController.popBackStack()
//                                },
//                                onFailure = { exception ->
//                                    isLoading = false
//                                    Toast.makeText(context, "Failed to create task: ${exception.message}", Toast.LENGTH_SHORT).show()
//                                }
//                            )
//                        }
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 16.dp),
//                enabled = !isLoading
//            ) {
//                if (isLoading) {
//                    CircularProgressIndicator(
//                        color = MaterialTheme.colorScheme.onPrimary,
//                        modifier = Modifier.size(24.dp)
//                    )
//                } else {
//                    Text(text = "Create Task")
//                }
//            }
//        }
//    }
//}
//
//
////Newly Added Code


package com.example.dailywork.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailywork.data.model.Task
import com.example.dailywork.data.repository.TaskRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskSetupScreen(
    navController: NavHostController,
    taskRepository: TaskRepository,
    userId: String,
    goalId: String,
    milestoneId: String,
    onTaskCreated: () -> Unit
) {
    // Context and coroutine scope for UI and background operations
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // State variables for the task fields
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var taskTime by remember { mutableStateOf("") }
    var taskDueDate by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Scaffold layout with a top bar and content area
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Create New Task") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Heading Text
            Text(
                text = "Create Task for Milestone",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Task Title Input Field
            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                label = { Text("Task Title") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Task Description Input Field
            OutlinedTextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text("Task Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Task Time Input Field (e.g., 09:00 AM)
            OutlinedTextField(
                value = taskTime,
                onValueChange = { taskTime = it },
                label = { Text("Task Time (e.g., 09:00 AM)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Task Due Date Input Field (e.g., 2024-10-10)
            OutlinedTextField(
                value = taskDueDate,
                onValueChange = { taskDueDate = it },
                label = { Text("Due Date (e.g., 2024-10-10)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Submit Button
            Button(
                onClick = {
                    if (taskTitle.isBlank() || taskTime.isBlank() || taskDueDate.isBlank()) {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        isLoading = true
                        coroutineScope.launch {
                            val newTask = Task(
                                taskId = "",
                                milestoneId = milestoneId,
                                title = taskTitle,
                                description = taskDescription,
                                time = taskTime,
                                dueDate = taskDueDate.toLong()
                            )
                            taskRepository.addTask(
                                userId = userId,
                                goalId = goalId,
                                milestoneId = milestoneId,
                                task = newTask,
                                onSuccess = {
                                    isLoading = false
                                    Toast.makeText(context, "Task created successfully!", Toast.LENGTH_SHORT).show()
                                    onTaskCreated()  // Navigate to the next screen
                                    navController.popBackStack()
                                },
                                onFailure = { exception ->
                                    isLoading = false
                                    Toast.makeText(context, "Failed to create task: ${exception.message}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(text = "Create Task")
                }
            }
        }
    }
}


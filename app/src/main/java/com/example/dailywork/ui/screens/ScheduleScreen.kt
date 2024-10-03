//package com.example.dailywork.ui.screens
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Icon
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
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.example.dailywork.data.model.Task
//import com.example.dailywork.data.repository.TaskRepository
//import com.example.dailywork.ui.components.CalendarView
//import com.example.dailywork.ui.components.TaskListView
//import kotlinx.coroutines.launch
//import java.util.Calendar
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ScheduleScreen(
//    navController: NavHostController,
//    selectedDate: Calendar,
//    taskRepository: TaskRepository = TaskRepository(),
//    userId: String,
//    goalId: String,
//    milestoneId: String
//) {
//    var tasksForSelectedDate by remember { mutableStateOf<List<Task>>(emptyList()) }
//    var loading by remember { mutableStateOf(false) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//    val coroutineScope = rememberCoroutineScope()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = "Schedule for $selectedDate") }
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
//                // Navigate to add schedule screen
//                navController.navigate("add_schedule")
//            }) {
//                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
//            }
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
//                text = "Your Tasks for $selectedDate",
//                fontSize = 22.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(bottom = 24.dp)
//            )
//
//            // Calendar View Component to pick a date
//            CalendarView(
//                onDateSelected = { newDate ->
//                    // Handle new date selection and refresh tasks
//                    coroutineScope.launch {
//                        loading = true
//                        errorMessage = null // Reset the error message on new request
//                        taskRepository.getTasksForDate(
//                            userId = userId,  // Replace with actual userId
//                            goalId = goalId,  // Replace with actual goalId
//                            milestoneId = milestoneId,  // Replace with actual milestoneId
//                            date = newDate,  // The selected date from the CalendarView
//                            onSuccess = { fetchedTasks ->
//                                tasksForSelectedDate = fetchedTasks // Update tasks state
//                                loading = false
//                            },
//                            onFailure = { exception: Exception ->
//                                loading = false
//                                errorMessage = "Error fetching tasks: ${exception.message}"
//                            }
//                        )
//                    }
//                }
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Error message handling
//            if (errorMessage != null) {
//                Text(
//                    text = errorMessage ?: "Unknown error",
//                    color = Color.Red,
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
//            }
//
//            // Task List View to display tasks for the selected date
//            if (loading) {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//            } else if (tasksForSelectedDate.isNotEmpty()) {
//                TaskListView(
//                    tasks = tasksForSelectedDate,
//                    onTaskClick = { task ->
//                        // Navigate to task detail or modify task screen
//                        navController.navigate("task_detail/${task.taskId}")
//                    }
//                )
//            } else if (!loading && tasksForSelectedDate.isEmpty() && errorMessage == null) {
//                Text(
//                    text = "No tasks scheduled for $selectedDate",
//                    color = Color.Gray,
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
//            }
//        }
//    }
//}


//new Added Code
package com.example.dailywork.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dailywork.data.model.Task
import com.example.dailywork.data.repository.TaskRepository
import com.example.dailywork.ui.components.CalendarView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    navController: NavHostController,
    userId: String,
    goalId: String,
    milestoneId: String,
    taskRepository: TaskRepository
) {
    // State variables
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }  // Manage Calendar object directly
    var tasksForSelectedDate by remember { mutableStateOf<List<Task>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Formatting the date to a readable string format
    val formattedDate = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault()).format(selectedDate.time)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Schedule for $formattedDate") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("task_setups/$userId/$goalId/$milestoneId")
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
            }
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
            Text(
                text = "Your Tasks for $formattedDate",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            CalendarView(
                selectedDate = selectedDate,
                onDateSelected = { newDate ->
                    selectedDate = newDate
                    coroutineScope.launch {
                        loadTasksForSelectedDate(
                            userId, goalId, milestoneId, selectedDate,
                            taskRepository, onTasksLoaded = { tasksForSelectedDate = it },
                            onLoading = { loading = it },
                            onError = { errorMessage = it }
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error message handling
            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "Unknown error",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            // Display tasks for the selected date
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (tasksForSelectedDate.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    tasksForSelectedDate.forEach { task ->
                        TaskCard(task = task)
                    }
                }
            } else if (!loading && tasksForSelectedDate.isEmpty() && errorMessage == null) {
                Text(
                    text = "No tasks scheduled for $formattedDate",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

// Function to load tasks for the selected date
suspend fun loadTasksForSelectedDate(
    userId: String,
    goalId: String,
    milestoneId: String,
    selectedDate: Calendar,
    taskRepository: TaskRepository,
    onTasksLoaded: (List<Task>) -> Unit,
    onLoading: (Boolean) -> Unit,
    onError: (String?) -> Unit
) {
    onLoading(true)
    onError(null) // Reset any previous errors
    try {
        // Format date to match Firestore date format or as required
        val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
        taskRepository.getTasksForDate(
            userId = userId,
            goalId = goalId,
            milestoneId = milestoneId,
            date = dateString,
            onSuccess = { tasks ->
                onTasksLoaded(tasks)
                onLoading(false)
            },
            onFailure = { exception ->
                onError("Error fetching tasks: ${exception.message}")
                onLoading(false)
            }
        )
    } catch (e: Exception) {
        onError(e.message)
        onLoading(false)
    }
}

// Custom Task Card UI
@Composable
fun TaskCard(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = task.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Description: ${task.description}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Time: ${task.time}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Due Date: ${task.dueDate}", fontSize = 14.sp)
        }
    }
}

// src/ui/screens/HomeScreen.kt
package com.example.dailywork.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailywork.data.model.Goal
import com.example.dailywork.data.model.Milestone
import com.example.dailywork.data.model.Task
import com.example.dailywork.ui.components.BottomNavigationBar
import com.example.dailywork.viewmodels.GoalViewModel
import com.example.dailywork.viewmodels.MilestoneViewModel
import com.example.dailywork.viewmodels.TaskViewModel
import com.example.dailywork.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    goalViewModel: GoalViewModel,
    milestoneViewModel: MilestoneViewModel,
    taskViewModel: TaskViewModel,
    userId: String,
    userName:String
) {
    // State variables for UI elements
    var goals by remember { mutableStateOf<List<Goal>>(emptyList()) }
    var selectedGoalId by remember { mutableStateOf<String?>(null) }
    var milestones by remember { mutableStateOf<Map<String, List<Milestone>>>(emptyMap()) }
    var tasks by remember { mutableStateOf<Map<String, List<Task>>>(emptyMap()) }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // State for Drawer and User Data
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    // Fetch Goals for the user
    LaunchedEffect(userId) {
        goalViewModel.getGoalsForUser(
            userId = userId,
            onSuccess = { fetchedGoals:List<Goal> ->
                goals = fetchedGoals
                loading = false
            },
            onFailure = { exception:Exception ->
                errorMessage = exception.message
                loading = false
            }
        )
    }

    // Fetch Milestones when a Goal is selected
    LaunchedEffect(selectedGoalId) {
        selectedGoalId?.let { goalId ->
            milestoneViewModel.getMilestonesForGoal(
                userId = userId,
                goalId = goalId,
                onSuccess = { fetchedMilestones:List<Milestone> ->
                    milestones = milestones.toMutableMap().apply {
                        put(goalId, fetchedMilestones)
                    }
                },
                onFailure = { exception:Exception ->
                    errorMessage = exception.message
                }
            )
        }
    }

    // Fetch Tasks for each Milestone when a Goal and its Milestones are expanded
    LaunchedEffect(milestones) {
        milestones.keys.forEach { goalId ->
            milestones[goalId]?.forEach { milestone ->
                taskViewModel.fetchTasksForMilestone(
                    userId = userId,
                    goalId = goalId,
                    milestoneId = milestone.milestoneId,
                    onSuccess = { fetchedTasks:List<Task> ->
                        tasks = tasks.toMutableMap().apply {
                            put(milestone.milestoneId, fetchedTasks)
                        }
                    },
                    onFailure = { exception:Exception->
                        errorMessage = exception.message
                    }
                )
            }
        }
    }

    // UI Layout
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                userViewModel = userViewModel,
                scope = scope,
                drawerState = drawerState,
                modifier = Modifier.width(240.dp)
            )
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Daily Works") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Handle Notification Bell Click */ }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                        }
                    }
                )
            },
            bottomBar = { BottomNavigationBar(navController) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("task_setup") },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add New Item")
                }
            } ,
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // Display Greeting with Username
                Text(
                    text = "Good Morning, $userName!",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                // Calendar Section
                CalendarView()

                // Hierarchical Goals, Milestones, and Tasks Section
                if (loading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "An error occurred",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    LazyColumn {
                        items(goals) { goal ->
                            GoalCard(
                                goal = goal,
                                isSelected = goal.goalId == selectedGoalId,
                                onClick = {
                                    selectedGoalId = if (goal.goalId == selectedGoalId) null else goal.goalId
                                }
                            )

                            if (goal.goalId == selectedGoalId) {
                                milestones[goal.goalId]?.let { milestoneList ->
                                    this@LazyColumn.items(milestoneList) { milestone ->
                                        MilestoneCard(
                                            milestone = milestone,
                                            tasks = tasks[milestone.milestoneId] ?: emptyList(),
                                            onAddTaskClick = {
                                                navController.navigate("task_setup?goalId=${goal.goalId}&milestoneId=${milestone.milestoneId}")
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Weekly Tasks Button
                Button(
                    onClick = { navController.navigate("all_tasks") },
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text("View Weekly Tasks")
                }
            }
        }
    }
}

// Additional Composable for Drawer, GoalCard, MilestoneCard, etc.
//Caldendar View
@Composable
fun CalendarView() {
    val today = Calendar.getInstance()
    val year = today.get(Calendar.YEAR)
    val month = today.get(Calendar.MONTH)
    val dayOfMonth = today.get(Calendar.DAY_OF_MONTH)
    val daysInMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${month + 1} $year",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            // Optionally, add navigation for previous/next month
        }

        // Days of the week header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Grid of days
        LazyColumn {
            items((1..daysInMonth).chunked(7)) { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    week.forEach { day ->
                        DayItem(day = day, isToday = day == dayOfMonth)
                    }
                    // Fill remaining days of the week with empty spaces if needed
                    if (week.size < 7) {
                        repeat(7 - week.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

// DayItem with Highlight for Today
@Composable
fun DayItem(day: Int, isToday: Boolean) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .padding(2.dp)
            .background(
                if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            )
            .clickable {
                // Handle day click if needed
            }
    ) {
        Text(
            text = day.toString(),
            color = if (isToday) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}

// TaskItem (reused from previous implementation)
@Composable
fun TaskItem(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = task.title ?: "Untitled Task",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = task.time ?: "No Time Set",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

// DrawerItem Data Class
data class DrawerItem(val title: String, val icon: ImageVector)
// Drawer Content Composable
@Composable
fun DrawerContent(
    navController: NavHostController,
    userViewModel: UserViewModel,
    scope: CoroutineScope,
    drawerState: DrawerState,
    modifier: Modifier
) {
    val drawerItems = listOf(
        DrawerItem("About", Icons.Default.Info),
        DrawerItem("Settings", Icons.Default.Settings),
        DrawerItem("Logout", Icons.Default.ExitToApp)
    )
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(24.dp))
        drawerItems.forEach { item ->
            NavigationDrawerItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = false,
                onClick = {
                    when (item.title) {
                        "About" -> navController.navigate("about")
                        "Settings" -> navController.navigate("settings")
                        "Logout" -> {
                            userViewModel.logoutUser()
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }
                    scope.launch { drawerState.close() }
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

@Composable
fun GoalCard(goal: Goal, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = goal.title ?: "Untitled Goal",
                style = MaterialTheme.typography.titleMedium
            )
            if (isSelected) {
                Text(
                    text = "Selected",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun MilestoneCard(milestone: Milestone, tasks: List<Task>, onAddTaskClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = milestone.title ?: "Untitled Milestone",
                style = MaterialTheme.typography.titleMedium,
            )
            // Display tasks under the milestone
            tasks.forEach { task ->
                TaskItem(task)
            }
            Button(
                onClick = onAddTaskClick,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Add Task")
            }
        }
    }
}




////@OptIn(ExperimentalMaterial3Api::class)
////@RequiresApi(Build.VERSION_CODES.O)
////@Composable
////fun HomeScreen(
////    navController: NavHostController,
////    userViewModel: UserViewModel, // Injected via Hilt
////    taskRepository: TaskRepository,
////    userId:String,
////    goalId: String,       // Add goalId as a parameter
////    milestoneId: String   // Add milestoneId as a parameter
////) {
////    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
////    var loading by remember { mutableStateOf(true) }
////    var errorMessage by remember { mutableStateOf<String?>(null) }
////
////    // State for Drawer
////    val drawerState = rememberDrawerState(DrawerValue.Closed)
////    val scope = rememberCoroutineScope()
////
////    // Fetch today's tasks from the repository
////    LaunchedEffect(Unit) {
////        taskRepository.getTasksForToday(
////            userId = userId,          // Pass userId
////            goalId = goalId,          // Pass goalId
////            milestoneId = milestoneId, // Pass milestoneId
////            onSuccess = { fetchedTasks: List<Task> ->
////                tasks = fetchedTasks
////                loading = false
////            },
////            onFailure = { exception: Exception ->
////                errorMessage = exception.message
////                loading = false
////            }
////        )
////    }
////
////    // Drawer Content
////    val drawerItems = listOf(
////        DrawerItem("About", Icons.Default.Info),
////        DrawerItem("Settings", Icons.Default.Settings),
////        DrawerItem("Logout", Icons.Default.ExitToApp)
////    )
////
////    // Scaffold with Drawer
////    ModalNavigationDrawer(
////        drawerState = drawerState,
////        drawerContent = {
////            ModalDrawerSheet {
////                Spacer(modifier = Modifier.height(24.dp))
////                drawerItems.forEach { item ->
////                    NavigationDrawerItem(
////                        icon = { Icon(item.icon, contentDescription = item.title) },
////                        label = { Text(item.title) },
////                        selected = false, // Manage selection state if needed
////                        onClick = {
////                            // Handle navigation based on item.title
////                            when (item.title) {
////                                "About" -> {
////                                    navController.navigate("about")
////                                }
////                                "Settings" -> {
////                                    navController.navigate("settings")
////                                }
////                                "Logout" -> {
////                                    userViewModel.logoutUser()
////                                    navController.navigate("login") {
////                                        popUpTo("home") { inclusive = true }
////                                    }
////                                }
////                            }
////                            scope.launch {
////                                drawerState.close()
////                            }
////                        },
////                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
////                    )
////                }
////            }
////        }
////    ) {
////        Scaffold(
////            topBar = {
////                TopAppBar(
////                    title = { Text("Daily Works") },
////                    navigationIcon = {
////                        IconButton(onClick = {
////                            scope.launch {
////                                drawerState.open()
////                            }
////                        }) {
////                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
////                        }
////                    },
////                    actions = {
////                        IconButton(onClick = { navController.navigate("goal_setup") }) {
////                            Icon(Icons.Filled.Person, contentDescription = "Profile")
////                        }
////                    }
////                )
////            },
////            bottomBar = {
////                BottomNavigationBar(navController)
////            }
////        ) { innerPadding ->
////            Column(
////                modifier = Modifier
////                    .fillMaxSize()
////                    .padding(innerPadding)
////                    .padding(16.dp)
////            ) {
////                // Greeting Section
////                GreetingSection()
////
////                // Calendar Section
////                CalendarView()
////
////                // Today's Tasks Section
////                Text(
////                    text = "Today's Tasks",
////                    style = MaterialTheme.typography.titleLarge,
////                    modifier = Modifier.padding(vertical = 16.dp)
////                )
////
////                // Display loading, error, or tasks
////                when {
////                    loading -> {
////                        Box(
////                            modifier = Modifier.fillMaxWidth(),
////                            contentAlignment = Alignment.Center
////                        ) {
////                            CircularProgressIndicator()
////                        }
////                    }
////                    errorMessage != null -> {
////                        Text(
////                            text = errorMessage ?: "An error occurred",
////                            color = MaterialTheme.colorScheme.error,
////                            textAlign = TextAlign.Center,
////                            modifier = Modifier
////                                .fillMaxWidth()
////                                .padding(16.dp)
////                        )
////                    }
////                    else -> {
////                        LazyColumn(
////                            modifier = Modifier.weight(1f)
////                        ) {
////                            items(tasks) { task ->
////                                TaskItem(task = task)
////                            }
////                        }
////                    }
////                }
////
////                // Button to view all tasks
////                Button(
////                    onClick = { navController.navigate("all_tasks") },
////                    modifier = Modifier.padding(vertical = 16.dp)
////                ) {
////                    Text("View All Tasks")
////                }
////            }
////        }
////    }
////}
////

//
//// Greeting Section
//@Composable
//fun GreetingSection() {
//    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
//    val greeting = when {
//        currentHour < 12 -> "Good Morning"
//        currentHour < 18 -> "Good Afternoon"
//        else -> "Good Evening"
//    }
//
//    Text(
//        text = greeting,
//        style = MaterialTheme.typography.headlineMedium,
//        modifier = Modifier.padding(bottom = 16.dp)
//    )
//}
//
//// CalendarView with Grid Layout
//@Composable

//

//
//
//
//////Newlyadded Code
////@OptIn(ExperimentalMaterial3Api::class)
////@RequiresApi(Build.VERSION_CODES.O)
////@Composable
////fun HomeScreen(
////    navController: NavHostController,
////    userViewModel: UserViewModel,
////    goalViewModel: GoalViewModel,
////    milestoneViewModel: MilestoneViewModel,
////    taskViewModel: TaskViewModel,
////    userId: String
////) {
////    // State variables
////    var goals by remember { mutableStateOf<List<Goal>>(emptyList()) }
////    var selectedGoalId by remember { mutableStateOf<String?>(null) }
////    var milestones by remember { mutableStateOf<Map<String, List<Milestone>>>(emptyMap()) }
////    var tasks by remember { mutableStateOf<Map<String, List<Task>>>(emptyMap()) }
////    var loading by remember { mutableStateOf(true) }
////    var errorMessage by remember { mutableStateOf<String?>(null) }
////
////    // State for Drawer
////    val drawerState = rememberDrawerState(DrawerValue.Closed)
////    val scope = rememberCoroutineScope()
////

////
////    // UI Layout
////    ModalNavigationDrawer(
////        drawerState = drawerState,
////        drawerContent = {
////            DrawerContent(
////                navController = navController,
////                userViewModel = userViewModel,
////                scope = scope,
////                drawerState = drawerState,
////                modifier = Modifier.width(240.dp)
////            )
////        }
////    ) {
////        Scaffold(
////            topBar = {
////                TopAppBar(
////                    title = { Text("Daily Works") },
////                    navigationIcon = {
////                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
////                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
////                        }
////                    },
////                    actions = {
////                        IconButton(onClick = { navController.navigate("goal_setup") }) {
////                            Icon(Icons.Filled.Person, contentDescription = "Profile")
////                        }
////                    }
////                )
////            },
////            bottomBar = {
////                BottomNavigationBar(navController)
////            }
////        ) { innerPadding ->
////            Column(
////                modifier = Modifier
////                    .fillMaxSize()
////                    .padding(innerPadding)
////                    .padding(16.dp)
////            ) {
////                // Greeting Section
////                GreetingSection()
////
////                // Calendar Section
////                CalendarView()
////
////                // Hierarchical Goals, Milestones, and Tasks Section
////                if (loading) {
////                    Box(
////                        modifier = Modifier.fillMaxWidth(),
////                        contentAlignment = Alignment.Center
////                    ) {
////                        CircularProgressIndicator()
////                    }
////                } else if (errorMessage != null) {
////                    Text(
////                        text = errorMessage ?: "An error occurred",
////                        color = MaterialTheme.colorScheme.error,
////                        textAlign = TextAlign.Center,
////                        modifier = Modifier.fillMaxWidth()
////                    )
////                } else {
////                    LazyColumn {
////                        items(goals) { goal ->
////                            GoalCard(
////                                goal = goal,
////                                isSelected = goal.goalId == selectedGoalId,
////                                onClick = {
////                                    selectedGoalId = if (goal.goalId == selectedGoalId) null else goal.goalId
////                                }
////                            )
////
////                            if (goal.goalId == selectedGoalId) {
////                                milestones[goal.goalId]?.let { milestoneList ->
////                                    this@LazyColumn.items(milestoneList) { milestone ->
////                                        MilestoneCard(
////                                            milestone = milestone,
////                                            tasks = tasks[milestone.milestoneId] ?: emptyList(),
////                                            onAddTaskClick = {
////                                                navController.navigate("task_setup?goalId=${goal.goalId}&milestoneId=${milestone.milestoneId}")
////                                            }
////                                        )
////                                    }
////                                }
////                            }
////                        }
////                    }
////                }
////
////                // Button to add a new goal
////                Button(
////                    onClick = { navController.navigate("goal_setup") },
////                    modifier = Modifier.padding(vertical = 16.dp)
////                ) {
////                    Text("Add New Goal")
////                }
////            }
////        }
////    }
////}
////


//
//
//
////Newly added Code
//@OptIn(ExperimentalMaterial3Api::class)
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun HomeScreen(
//    navController: NavHostController,
//    userViewModel: UserViewModel,
//    goalViewModel: GoalViewModel,
//    milestoneViewModel: MilestoneViewModel,
//    taskViewModel: TaskViewModel,
//    userId: String
//) {
//    // State variables for goals, milestones, and tasks
//    var goals by remember { mutableStateOf<List<Goal>>(emptyList()) }
//    var selectedGoalId by remember { mutableStateOf<String?>(null) }
//    var milestones by remember { mutableStateOf<Map<String, List<Milestone>>>(emptyMap()) }
//    var tasks by remember { mutableStateOf<Map<String, List<Task>>>(emptyMap()) }
//    var loading by remember { mutableStateOf(true) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//    var userName by remember { mutableStateOf("User") }
//
//    // State for Drawer
//    val drawerState = rememberDrawerState(DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//
//    // Fetch User's Name
//    LaunchedEffect(userId) {
//        userViewModel.getUserName(
//            userId = userId,
//            onSuccess = { fetchedName: String ->
//                userName = fetchedName
//            },
//            onFailure = {
//                userName = "User"
//            }
//        )
//    }
//
//    // Fetch Goals for the user
//    LaunchedEffect(userId) {
//        goalViewModel.getGoalsForUser(
//            userId = userId,
//            onSuccess = { fetchedGoals: List<Goal> ->
//                goals = fetchedGoals
//                loading = false
//            },
//            onFailure = { exception: Exception ->
//                errorMessage = exception.message
//                loading = false
//            }
//        )
//    }
//
//    // Fetch Milestones when a Goal is selected
//    LaunchedEffect(selectedGoalId) {
//        selectedGoalId?.let { goalId ->
//            milestoneViewModel.getMilestonesForGoal(
//                userId = userId,
//                goalId = goalId,
//                onSuccess = { fetchedMilestones: List<Milestone> ->
//                    milestones = milestones.toMutableMap().apply {
//                        put(goalId, fetchedMilestones)
//                    }
//                },
//                onFailure = { exception: Exception ->
//                    errorMessage = exception.message
//                }
//            )
//        }
//    }
//
//    // Fetch Tasks for each Milestone
//    LaunchedEffect(milestones) {
//        milestones.keys.forEach { goalId ->
//            milestones[goalId]?.forEach { milestone ->
//                taskViewModel.fetchTasksForMilestone(
//                    userId = userId,
//                    goalId = goalId,
//                    milestoneId = milestone.milestoneId,
//                    onSuccess = { fetchedTasks: List<Task> ->
//                        tasks = tasks.toMutableMap().apply {
//                            put(milestone.milestoneId, fetchedTasks)
//                        }
//                    },
//                    onFailure = { exception: Exception ->
//                        errorMessage = exception.message
//                    }
//                )
//            }
//        }
//    }
//
//    // UI Layout
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            DrawerContent(
//                navController = navController,
//                userViewModel = userViewModel,
//                scope = scope,
//                drawerState = drawerState,
//                modifier = Modifier.width(240.dp) // Customize the drawer width
//            )
//        }
//    ) {
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = {
//                        Text("Daily Works")
//                    },
//                    navigationIcon = {
//                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
//                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
//                        }
//                    },
//                    actions = {
//                        IconButton(onClick = { /* Navigate to Notifications */ }) {
//                            Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
//                        }
//                    }
//                )
//            },
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = { /* Navigate to add new goal, milestone, or task */ },
//                    content = { Icon(Icons.Filled.Add, contentDescription = "Add") }
//                )
//            }
//        ) { innerPadding ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding)
//                    .padding(16.dp)
//            ) {
//                // Greeting Section with User's Name
//                Text(
//                    text = "Good Morning, $userName!",
//                    style = MaterialTheme.typography.headlineMedium,
//                    color = MaterialTheme.colorScheme.primary
//                )
//
//                // Calendar Section
//                CalendarView()
//
//                // Hierarchical Goals, Milestones, and Tasks Section
//                if (loading) {
//                    Box(
//                        modifier = Modifier.fillMaxWidth(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        CircularProgressIndicator()
//                    }
//                } else if (errorMessage != null) {
//                    Text(
//                        text = errorMessage ?: "An error occurred",
//                        color = MaterialTheme.colorScheme.error,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                } else {
//                    LazyColumn {
//                        items(goals) { goal ->
//                            GoalCard(
//                                goal = goal,
//                                isSelected = goal.goalId == selectedGoalId,
//                                onClick = {
//                                    selectedGoalId = if (goal.goalId == selectedGoalId) null else goal.goalId
//                                }
//                            )
//
//                            if (goal.goalId == selectedGoalId) {
//                                milestones[goal.goalId]?.let { milestoneList ->
//                                    this@LazyColumn.items(milestoneList) { milestone ->
//                                        MilestoneCard(
//                                            milestone = milestone,
//                                            tasks = tasks[milestone.milestoneId] ?: emptyList(),
//                                            onAddTaskClick = {
//                                                navController.navigate("task_setup?goalId=${goal.goalId}&milestoneId=${milestone.milestoneId}")
//                                            }
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}

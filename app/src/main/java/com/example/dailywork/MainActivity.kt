package com.example.dailywork

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dailywork.data.database.FirebaseService
import com.example.dailywork.data.repository.GoalRepository
import com.example.dailywork.data.repository.MilestoneRepository
import com.example.dailywork.data.repository.TaskRepository
import com.example.dailywork.data.repository.UserRepository
import com.example.dailywork.ui.screens.AllTasksScreen
import com.example.dailywork.ui.screens.GoalSetupScreen
import com.example.dailywork.ui.screens.HomeScreen
import com.example.dailywork.ui.screens.LoginScreen
import com.example.dailywork.ui.screens.MilestoneSetupScreen
import com.example.dailywork.ui.screens.ProfileScreen
import com.example.dailywork.ui.screens.RegisterScreen
import com.example.dailywork.ui.screens.ScheduleScreen
import com.example.dailywork.ui.screens.SplashScreen
import com.example.dailywork.ui.screens.TaskSetupScreen
import com.example.dailywork.ui.theme.AppTheme
import com.example.dailywork.viewmodels.GoalViewModel
import com.example.dailywork.viewmodels.MilestoneViewModel
import com.example.dailywork.viewmodels.TaskViewModel
import com.example.dailywork.viewmodels.UserViewModel

class MainActivity : ComponentActivity() {
    private val firebaseService = FirebaseService()
    private val currentUser = firebaseService.getCurrentUser()
    private val userId = currentUser?.uid // Get the current user's UID


    // Repositories
    private val userRepository = UserRepository(firebaseService = FirebaseService())
    private val goalRepository = GoalRepository()
    private val milestoneRepository = MilestoneRepository()
    private val taskRepository = TaskRepository()

    // ViewModels
    private val userViewModel = UserViewModel(userRepository)
    private val goalViewModel = GoalViewModel(goalRepository)
    private val milestoneViewModel = MilestoneViewModel(milestoneRepository)
    private val taskViewModel = TaskViewModel(taskRepository)

    private val userName = userViewModel.currentUser.value?.username

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                // The main navigation controller
                val navController: NavHostController = rememberNavController()

                // Setting up the navigation graph
                NavigationGraph(
                    navController = navController,
                    userViewModel = userViewModel,
                    goalViewModel = goalViewModel,
                    milestoneViewModel = milestoneViewModel,
                    taskViewModel = taskViewModel
                )
            }
        }
    }


//    @RequiresApi(Build.VERSION_CODES.O)
//    @Composable
//    private fun NavigationGraph(
//        navController: NavHostController,
//        userViewModel: UserViewModel,
//        goalViewModel: GoalViewModel,
//        milestoneViewModel: MilestoneViewModel,
//        taskViewModel: TaskViewModel
//    ) {
//        // Define the navigation graph with various screens
//        NavHost(navController = navController, startDestination = "splash") {
//            composable("splash") {
//                SplashScreen(navController = navController, userViewModel = userViewModel)
//            }
//            composable("login") {
//                LoginScreen(userViewModel = userViewModel, navController = navController)
//            }
//            composable("register") {
//                RegisterScreen(userViewModel = userViewModel, navController = navController)
//            }
//            composable("home") {
//                if (userId != null) {
//                    HomeScreen(
//                        navController = navController,
//                        userViewModel = userViewModel,
//                        goalViewModel= goalViewModel,
//                        milestoneViewModel=milestoneViewModel,
//                        taskViewModel=taskViewModel,
//                        userId = userId
//                    )
//                }
//            }
//            // New Screens
////            composable("goal_setup") {
////                if (userId != null) {
////                    GoalSetupScreen(
////                        goalViewModel = goalViewModel,
////                        userViewModel = userViewModel,
////                        userId = userId,
////                        onGoalSaved = {
////                            navController.navigate("milestoneSetup") // Navigate to the next screen after saving the goal
////                        }
////                    )
////                }
////            }
//            composable("goal_setup") {
//                if (userId != null) {
//                    GoalSetupScreen(
//                        navController = navController,
//                        goalViewModel = goalViewModel,
//                        userId =userId,// Replace with actual UID
//                        onGoalSaved = {
//                            // Handle what happens after saving the goal, e.g., navigate to home screen
//                            navController.navigate("home")
//                        }
//                    )
//                }
//            }
//            composable("profile") {
//                ProfileScreen(navController = navController)
//            }
//            composable("all_tasks") {
//                AllTasksScreen(navController = navController)
//            }
//            composable("schedule") {
//                if (userId != null) {
//                    ScheduleScreen(
//                        navController = navController,
//                        selectedDate = Calendar.getInstance(),
//                        taskRepository = taskRepository,
//                        userId = userId,    // Add userId, goalId, and milestoneId
//                        goalId = "",
//                        milestoneId = ""
//                    )
//                }
//            }
////            composable("milestoneSetup") {
////                val userId = Firebase.auth.currentUser?.uid ?: "unknown"
////                MilestoneSetupScreen(
////                    navController = navController, milestoneViewModel = milestoneViewModel)
////            }
////            composable("taskSetup") {
////                TaskSetupScreen(navController = navController, taskViewModel = taskViewModel)
////            }
////            composable("schedule") {
////                ScheduleScreen(navController = navController, taskViewModel = taskViewModel)
////            }
//        }
//    }

    //Newly Added Code:
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun NavigationGraph(
        navController: NavHostController,
        userViewModel: UserViewModel,
        goalViewModel: GoalViewModel,
        milestoneViewModel: MilestoneViewModel,
        taskViewModel: TaskViewModel
    ) {
        // Define the navigation graph with various screens
        NavHost(navController = navController, startDestination = "splash") {
            composable("splash") {
                SplashScreen(navController = navController, userViewModel = userViewModel)
            }
            composable("login") {
                LoginScreen(userViewModel = userViewModel, navController = navController)
            }
            composable("register") {
                RegisterScreen(userViewModel = userViewModel, navController = navController)
            }
            composable("home") {
                if (userId!=null) {
                        HomeScreen(
                            navController = navController,
                            userViewModel = userViewModel,
                            goalViewModel = goalViewModel,
                            milestoneViewModel = milestoneViewModel,
                            taskViewModel = taskViewModel,
                            userId = userId,
                            userName = userName?:"Gemi"
                        )
                }
            }
            composable("goal_setup") {
                if (userId != null) {
                    GoalSetupScreen(
                        navController = navController,
                        goalViewModel = goalViewModel,
                        userId = userId,  // Pass the user ID to the GoalSetupScreen
                        onGoalSaved = { goalId ->
                            // After saving the goal, navigate to MilestoneSetupScreen
                            navController.navigate("milestone_setup/$goalId")
                        }
                    )
                }
            }
            composable("profile") {
                if (userId != null) {
                    ProfileScreen(
                        navController = navController,
                        userViewModel = userViewModel,
                        goalViewModel = goalViewModel,
                        userId = userId
                    )
                }
            }
            composable("all_tasks") {
                AllTasksScreen(navController = navController)
            }
            composable("schedule") {
                if (userId != null) {
                    ScheduleScreen(
                        navController = navController,
                        taskRepository = taskRepository,
                        userId = userId,  // Add userId, goalId, and milestoneId
                        goalId = "",
                        milestoneId = ""
                    )
                }
            }
            composable("milestone_setup/{goalId}") { backStackEntry ->
                // Retrieve the goalId from the navigation arguments
                val goalId = backStackEntry.arguments?.getString("goalId") ?: ""
                if (userId != null && goalId.isNotEmpty()) {
                    MilestoneSetupScreen(
                        navController = navController,
                        milestoneViewModel = milestoneViewModel,
                        goalId = goalId,
                        userId = userId,
                        onMilestoneCreated = {
                            // After the milestone is created, navigate to the task setup screen
                            navController.navigate("task_setup")
                        }
                    )
                }
            }
            composable("task_setup") {
                // Task Setup Screen Placeholder
                if (userId != null) {
                    TaskSetupScreen(
                        navController = navController,
                        taskRepository = taskRepository,
                        userId = userId,
                        goalId = "",
                        milestoneId = "",
                        onTaskCreated = {
                            // After the task is created, navigate to the schedule screen
                            navController.navigate("schedule")
                        }

                    )
                }
            }
        }
    }

}

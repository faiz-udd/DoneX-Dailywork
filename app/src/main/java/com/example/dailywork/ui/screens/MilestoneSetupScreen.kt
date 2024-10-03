//package com.example.dailywork.ui.screens
//import android.widget.Toast
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Button
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
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
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.example.dailywork.viewmodels.MilestoneViewModel
//import kotlinx.coroutines.launch

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MilestoneSetupScreen(
//    navController: NavHostController,
//    milestoneViewModel: MilestoneViewModel,
//    goalId: String,
//    userId: String, // Add userId parameter to match the repository signature
//    onMilestoneCreated: () -> Unit
//) {
//    val context = LocalContext.current
//    var milestoneTitle by remember { mutableStateOf("") }
//    var milestoneDescription by remember{ mutableStateOf("") }
//    var milestoneDueDate by remember { mutableStateOf("") }
//    var loading by remember { mutableStateOf(false) }
//    val scope = rememberCoroutineScope()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = "Milestone Setup") }
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = "Set Up Your Milestone",
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier.padding(bottom = 32.dp)
//            )

//            // Milestone Title Input
//            TextField(
//                value = milestoneTitle,
//                onValueChange = { milestoneTitle = it },
//                label = { Text("Milestone Title") },
//                singleLine = true,
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            TextField(
//                value = milestoneDescription,
//                onValueChange = { milestoneDescription = it },
//                label = { Text("Milestone Description") },
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(24.dp))
//            // Milestone Due Date Input (For simplicity, we use a text field, but it can be replaced by a date picker)
//            TextField(
//                value = milestoneDueDate,
//                onValueChange = { milestoneDueDate = it },
//                label = { Text("Due Date (YYYY-MM-DD)") },
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Save Milestone Button
//            Button(
//                onClick = {
//                    loading = true
//                    scope.launch {
//                        if (milestoneTitle.isNotEmpty() && milestoneDueDate.isNotEmpty()) {
//                            milestoneViewModel.addMilestone(
//                                userId = userId,  // Pass userId here
//                                goalId = goalId,
//                                title = milestoneTitle,
//                                description = milestoneDescription,  // Assuming there's a description field
//                                dueDateString = milestoneDueDate  // Pass dueDate as String
//                            )
//                            // Listen for success/failure events by observing ViewModel's state flows
//                            milestoneViewModel.errorMessage.collect { error ->
//                                if (error != null) {
//                                    Toast.makeText(
//                                        context,
//                                        "Failed to Create Milestone: $error",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//                            }
//
//                            milestoneViewModel.isLoading.collect { isLoading ->
//                                loading = isLoading
//                            }
//
//                            if (!loading) {
//                                Toast.makeText(context, "Milestone Created", Toast.LENGTH_SHORT)
//                                    .show()
//                                onMilestoneCreated()
//                                navController.navigate("task_setup")
//                            }
//                        } else {
//                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                        loading = false
//                    }
//                },
//                modifier = Modifier.fillMaxWidth(),
//                enabled = !loading
//            ) {
//                if (loading) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(24.dp),
//                        color = MaterialTheme.colorScheme.onPrimary
//                    )
//                } else {
//                    Text(text = "Save Milestone", fontSize = 18.sp)
//                }
//            }
//
//        }
//    }
//}




//Newly Added Code
package com.example.dailywork.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dailywork.viewmodels.MilestoneViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneSetupScreen(
    navController: NavHostController,
    milestoneViewModel: MilestoneViewModel,
    goalId: String,
    userId: String,
    onMilestoneCreated: () -> Unit
) {
    val context = LocalContext.current
    var milestoneTitle by remember { mutableStateOf("") }
    var milestoneDescription by remember { mutableStateOf("") }
    var milestoneDueDate by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Milestone Setup", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Set Up Your Milestone",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            // More styling with inputs and buttons...

            // Milestone Title Input
            TextField(
                value = milestoneTitle,
                onValueChange = { milestoneTitle = it },
                label = { Text("Milestone Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = milestoneDescription,
                onValueChange = { milestoneDescription = it },
                label = { Text("Milestone Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Milestone Due Date Input (For simplicity, we use a text field, but it can be replaced by a date picker)
            TextField(
                value = milestoneDueDate,
                onValueChange = { milestoneDueDate = it },
                label = { Text("Due Date (YYYY-MM-DD)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Milestone Button
            Button(
                onClick = {
                    loading = true
                    scope.launch {
                        if (milestoneTitle.isNotEmpty() && milestoneDueDate.isNotEmpty()) {
                            milestoneViewModel.addMilestone(
                                userId = userId,  // Pass userId here
                                goalId = goalId,
                                title = milestoneTitle,
                                description = milestoneDescription,  // Assuming there's a description field
                                dueDateString = milestoneDueDate  // Pass dueDate as String
                            )
                            // Listen for success/failure events by observing ViewModel's state flows
                            milestoneViewModel.errorMessage.collect { error ->
                                if (error != null) {
                                    Toast.makeText(
                                        context,
                                        "Failed to Create Milestone: $error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            milestoneViewModel.isLoading.collect { isLoading ->
                                loading = isLoading
                            }

                            if (!loading) {
                                Toast.makeText(context, "Milestone Created", Toast.LENGTH_SHORT)
                                    .show()
                                onMilestoneCreated()
                                navController.navigate("task_setup")
                            }
                        } else {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT)
                                .show()
                        }
                        loading = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(text = "Save Milestone", fontSize = 18.sp)
                }
            }

        }
    }
}

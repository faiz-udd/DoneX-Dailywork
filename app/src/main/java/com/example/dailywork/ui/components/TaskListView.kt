package com.example.dailywork.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailywork.data.model.Task

@Composable
fun TaskListView(
    tasks: List<Task>, // List of tasks to display
    onTaskClick: (Task) -> Unit // Callback for task click
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Display a header
        Text(
            text = "Tasks",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        // Check if there are any tasks
        if (tasks.isEmpty()) {
            Text(
                text = "No tasks available for this day.",
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp
            )
        } else {
            // Display the list of tasks
            for (task in tasks) {
                TaskItem(task = task, onClick = { onTaskClick(task) })
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
    // Layout for each task item
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Task details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp
            )
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp
            )
        }

        // Task status
        Text(
            text = if (task.completed) "Completed" else "Pending",
            style = MaterialTheme.typography.bodyMedium,
            color = if (task.completed) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
        )
    }
}

package com.example.dailywork.utils

object Constants {

    // Firebase Collection Names
    const val USERS_COLLECTION = "users"
    const val GOALS_COLLECTION = "goals"
    const val MILESTONES_COLLECTION = "milestones"
    const val TASKS_COLLECTION = "tasks"

    // User Fields
    const val USER_NAME_FIELD = "name"
    const val USER_EMAIL_FIELD = "email"
    const val USER_PROFILE_IMAGE_FIELD = "profileImageUrl"
    const val USER_ID_FIELD = "userId"

    // Goal Fields
    const val GOAL_TITLE_FIELD = "title"
    const val GOAL_DESCRIPTION_FIELD = "description"
    const val GOAL_START_DATE_FIELD = "startDate"
    const val GOAL_END_DATE_FIELD = "endDate"
    const val GOAL_USER_ID_FIELD = "userId"

    // Milestone Fields
    const val MILESTONE_TITLE_FIELD = "title"
    const val MILESTONE_DESCRIPTION_FIELD = "description"
    const val MILESTONE_GOAL_ID_FIELD = "goalId"
    const val MILESTONE_START_DATE_FIELD = "startDate"
    const val MILESTONE_END_DATE_FIELD = "endDate"

    // Task Fields
    const val TASK_TITLE_FIELD = "title"
    const val TASK_DESCRIPTION_FIELD = "description"
    const val TASK_TIME_FIELD = "time"
    const val TASK_MILESTONE_ID_FIELD = "milestoneId"

    // General App Constants
    const val DATE_FORMAT = "yyyy-MM-dd"
    const val TIME_FORMAT = "HH:mm"
    const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm"

    // Navigation Routes
    const val HOME_SCREEN = "home"
    const val LOGIN_SCREEN = "login"
    const val GOAL_SETUP_SCREEN = "goal_setup"
    const val MILESTONE_SETUP_SCREEN = "milestone_setup"
    const val TASK_SETUP_SCREEN = "task_setup"
    const val SCHEDULE_SCREEN = "schedule"
    const val ADD_SCHEDULE_SCREEN = "add_schedule"

    // Error Messages
    const val ERROR_TASK_CREATION = "Failed to create task. Please try again."
    const val ERROR_GOAL_CREATION = "Failed to create goal. Please try again."
    const val ERROR_MILESTONE_CREATION = "Failed to create milestone. Please try again."

    // Success Messages
    const val SUCCESS_TASK_CREATION = "Task created successfully!"
    const val SUCCESS_GOAL_CREATION = "Goal created successfully!"
    const val SUCCESS_MILESTONE_CREATION = "Milestone created successfully!"

    // Default Values
    const val DEFAULT_GOAL_ID = "defaultGoalId"
    const val DEFAULT_MILESTONE_ID = "defaultMilestoneId"
}

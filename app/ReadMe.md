***DoneX - Daily Work Scheduler Ap***
DoneX is an Android application designed to streamline daily task management and help users achieve their personal goals efficiently. Built using Kotlin and Firebase Firestore, DoneX offers a powerful and user-friendly interface to organize tasks, set goals, and track progress, ensuring users stay on top of their daily activities and milestones.

**Features**
*Daily Task Management:* Create and manage tasks for each day, helping you stay organized and productive.
*Goal Setting*: Define yearly goals and break them down into monthly milestones and weekly tasks for easier tracking.
*Milestone Tracking:* Keep track of your progress by managing tasks tied to specific milestones.
*Firestore Integration:* All data, including tasks, goals, and milestones, is securely stored in Firestore, ensuring seamless synchronization across devices.
*Task Reminders:* Receive reminders throughout the day (9 AM, 3 PM, 8 PM) to ensure tasks are completed on time.

Kotlin: The primary language for building the Android application.
Jetpack Compose: Used for UI development to create a clean and responsive interface.
Firestore: Firebase’s NoSQL cloud database for storing user data, tasks, and goals.
Firebase Authentication: For secure user login and personalized data access.

**How to Use**
*User Registration:* New users can sign up with their credentials. Existing users can log in using Firebase Authentication.
*Set Your Goals:* Define a yearly goal and break it into monthly milestones and weekly tasks.
*Daily Planner:* Schedule daily tasks linked to milestones, and view all tasks in a calendar view.
Reminders: Receive reminders three times a day with motivational messages and benefits of completing your tasks.
Track Progress: View your progress over time, with milestones updated automatically based on task completion.
Installation Guide
Clone the repository:
bash
Copy code
git clone https://github.com/your-repo/DoneX.git
Open the project in Android Studio.
Sync the project with Gradle files to download dependencies.
Set up Firebase:
Create a Firebase project in the Firebase Console.
Add your Android app to Firebase.
Download the google-services.json file and place it in the app/ directory.
Build and run the app on your Android device or emulator.
License
This project is licensed under the MIT License - see the LICENSE file for details.
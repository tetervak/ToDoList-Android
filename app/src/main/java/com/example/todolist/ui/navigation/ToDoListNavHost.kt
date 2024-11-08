package com.example.todolist.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todolist.ToDoListAppState
import com.example.todolist.ui.screens.edittask.EditTaskScreen
import com.example.todolist.ui.screens.login.LoginScreen
import com.example.todolist.ui.screens.settings.SettingsScreen
import com.example.todolist.ui.screens.signup.SignUpScreen
import com.example.todolist.ui.screens.splash.SplashScreen
import com.example.todolist.ui.screens.stats.StatsScreen
import com.example.todolist.ui.screens.tasks.TasksScreen

@ExperimentalMaterial3Api
@Composable
fun ToDoListNavHost(
    appState: ToDoListAppState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = appState.navController,
        startDestination = SPLASH_SCREEN,
        modifier = modifier
    ) {
        composable(SPLASH_SCREEN) {
            SplashScreen(
                onSplashed = {
                    appState.navigateAndPopUp(TASKS_SCREEN, SPLASH_SCREEN)
                }
            )
        }

        composable(SETTINGS_SCREEN) {
            SettingsScreen(
                restartApp = { appState.clearAndNavigate(SPLASH_SCREEN) },
                onLogin = { appState.navigate(LOGIN_SCREEN) },
                onSignUp = { appState.navigate(SIGN_UP_SCREEN) }
            )
        }

        composable(STATS_SCREEN) {
            StatsScreen()
        }

        composable(LOGIN_SCREEN) {
            LoginScreen(
                onLoggedIn = {
                    appState.navigateAndPopUp(SETTINGS_SCREEN, LOGIN_SCREEN)
                }
            )
        }

        composable(SIGN_UP_SCREEN) {
            SignUpScreen(
                onSignedUp = {
                    appState.navigateAndPopUp(SETTINGS_SCREEN, SIGN_UP_SCREEN)
                }
            )
        }

        composable(TASKS_SCREEN) {
            TasksScreen(
                onAddTask = { appState.navigate(EDIT_TASK_SCREEN) },
                onEditTask = { taskId ->
                    appState.navigate("$EDIT_TASK_SCREEN?$TASK_ID={${taskId}}")
                },
                onStats = { appState.navigate(STATS_SCREEN) },
                onSettings = { appState.navigate(SETTINGS_SCREEN) }
            )
        }

        composable(
            route = "$EDIT_TASK_SCREEN?$TASK_ID={$TASK_ID}",
            arguments = listOf(navArgument(TASK_ID) { nullable = true; defaultValue = null })
        ) {
            EditTaskScreen(
                popUpScreen = { appState.popUp() }
            )
        }
    }
}
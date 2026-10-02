package com.learntrack.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.learntrack.app.ui.dashboard.DashboardScreen
import com.learntrack.app.ui.details.DetailsScreen
import com.learntrack.app.ui.login.LoginScreen

object Routes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val DETAILS = "details/{courseId}"
    fun details(courseId: Int) = "details/$courseId"
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Routes.DASHBOARD) {
                    popUpTo(Routes.LOGIN) { inclusive = true } // back won't return to login
                }
            })
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(onCourseClick = { id ->
                navController.navigate(Routes.details(id))
            })
        }

        composable(
            route = Routes.DETAILS,
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) {
            DetailsScreen(onBack = { navController.popBackStack() })
        }
    }
}
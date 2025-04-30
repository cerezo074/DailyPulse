package com.eli.examples.dailypulse.android.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppScaffold() {
    val navController = rememberNavController()

    Scaffold {
        AppNavHost(
            navController = navController
        )
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screens.ARTICLES.route
    ) {
        composable(Screens.ARTICLES.route) {
            ArticlesScreen(
                onAboutButtonClick = { navController.navigate(Screens.ABOUT_DEVICE.route) }
            )
        }
        composable(Screens.ABOUT_DEVICE.route) {
            AboutScreen(
                onUpButtonClick = { navController.popBackStack() }
            )
        }
    }
}


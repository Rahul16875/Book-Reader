package com.example.bookreader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookreader.screens.ReaderSplashScreen
import com.example.bookreader.screens.details.BookDetailsScreen
import com.example.bookreader.screens.home.HomeScreenViewModel
import com.example.bookreader.screens.home.ReaderHomeScreen
import com.example.bookreader.screens.login.ReaderLoginScreen
import com.example.bookreader.screens.search.BookSearchViewModel
import com.example.bookreader.screens.search.SearchScreen
import com.example.bookreader.screens.stats.StatsScreen
import com.example.bookreader.screens.update.UpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name){
        composable(ReaderScreens.SplashScreen.name){
            ReaderSplashScreen(navController = navController)
        }

        composable(ReaderScreens.LoginScreen.name){
            ReaderLoginScreen(navController = navController)
        }
        composable(ReaderScreens.ReaderHomeScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderHomeScreen(navController = navController,viewModel = homeViewModel)
        }
        composable(ReaderScreens.ReaderStatsScreen.name){
            StatsScreen(navController = navController)
        }
        composable(ReaderScreens.SearchScreen.name){
            val searchViewModel = hiltViewModel<BookSearchViewModel>()
            SearchScreen(navController = navController, viewModel = searchViewModel)
        }

        val detailName = ReaderScreens.DetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })){backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                BookDetailsScreen(navController = navController, bookId = it.toString())
            }
        }

        val updateName = ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}",
                    arguments = listOf(navArgument("bookItemId"){
                        type = NavType.StringType
                    })){ it ->
                it.arguments?.getString("bookItemId").let {
                    UpdateScreen(navController= navController,bookItemId = it.toString())
                }
        }
    }
}
package com.mark.dru_movies.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Details : Screen("details/{movieId}") {
        fun createRoute(movieId: Int) = "details/$movieId"
    }
}
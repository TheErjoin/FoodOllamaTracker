package com.sadistictech.home.router

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sadistictech.home.presentation.HomeScreen

object HomeScreenRouter {
    const val ROUTE = "home_graph"

    fun NavGraphBuilder.homeNavGraph() {
        composable(route = ROUTE) {
            HomeScreen(viewModel = hiltViewModel())
        }
    }
}

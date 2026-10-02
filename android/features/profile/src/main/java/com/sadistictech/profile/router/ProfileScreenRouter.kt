package com.sadistictech.profile.router

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sadistictech.profile.presentation.ProfileScreen

object ProfileScreenRouter {
    const val ROUTE = "profile_graph"

    fun NavGraphBuilder.profileNavGraph() {
        composable(route = ROUTE) {
            ProfileScreen()
        }
    }
}

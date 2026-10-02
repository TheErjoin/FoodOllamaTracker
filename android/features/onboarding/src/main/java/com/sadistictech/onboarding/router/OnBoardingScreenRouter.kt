package com.sadistictech.onboarding.router

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sadistictech.onboarding.presentation.OnBoardingScreen

object OnBoardingScreenRouter {
    const val ROUTE = "onboarding_graph"

    fun NavGraphBuilder.onBoardingNavGraph() {
        composable(route = ROUTE) {
            OnBoardingScreen()
        }
    }
}

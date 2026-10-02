package com.sadistictech.foodollamatracker.navigationcore

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sadistictech.home.router.HomeScreenRouter
import com.sadistictech.home.router.HomeScreenRouter.homeNavGraph
import com.sadistictech.onboarding.router.OnBoardingScreenRouter
import com.sadistictech.onboarding.router.OnBoardingScreenRouter.onBoardingNavGraph
import com.sadistictech.profile.router.ProfileScreenRouter.profileNavGraph
import com.sadistictech.settings.router.SettingsScreenRouter.settingsNavGraph

@Composable
fun NavGraph(navController: NavHostController) {
    val startDestination = HomeScreenRouter.ROUTE

    NavDrawer(navController = navController) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            homeNavGraph()
            onBoardingNavGraph()
            settingsNavGraph()
            profileNavGraph()
        }
    }
}
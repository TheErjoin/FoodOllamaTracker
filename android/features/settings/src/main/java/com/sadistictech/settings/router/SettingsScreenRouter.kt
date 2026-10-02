package com.sadistictech.settings.router

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sadistictech.settings.presentation.SettingsScreen

object SettingsScreenRouter {
    const val ROUTE = "settings_graph"

    fun NavGraphBuilder.settingsNavGraph() {
        composable(route = ROUTE) {
            SettingsScreen()
        }
    }
}

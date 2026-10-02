package com.sadistictech.foodollamatracker.navigationcore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sadistictech.presentation.ui.INACTIVE_ICON_COLOR
import com.sadistictech.presentation.ui.PILL_COLOR
import com.sadistictech.foodollamatracker.components.MainBottomNavigation
import com.sadistictech.home.router.HomeScreenRouter
import com.sadistictech.profile.router.ProfileScreenRouter

@Composable
fun NavDrawer(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedIndex = if (currentRoute == ProfileScreenRouter.ROUTE) 1 else 0
    val showBottomBar = currentRoute == HomeScreenRouter.ROUTE ||
            currentRoute == ProfileScreenRouter.ROUTE

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                MainBottomNavigation(
                    items = listOf(
                        Icons.Default.Home,
                        Icons.Default.Person
                    ),
                    selectedIndex = selectedIndex,
                    pillColor = PILL_COLOR,
                    activeIconColor = Color.White,
                    inactiveIconColor = INACTIVE_ICON_COLOR,
                    onItemSelected = { index ->
                        when (index) {
                            0 -> navController.navigate(HomeScreenRouter.ROUTE) {
                                popUpTo(HomeScreenRouter.ROUTE) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }

                            1 -> navController.navigate(ProfileScreenRouter.ROUTE) {
                                popUpTo(HomeScreenRouter.ROUTE) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            content()
        }
    }
}
package com.sadistictech.foodollamatracker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun MainBottomNavigation(
    modifier: Modifier = Modifier,
    items: List<ImageVector>,
    selectedIndex: Int,
    pillColor: Color,
    activeIconColor: Color,
    inactiveIconColor: Color,
    onItemSelected: (Int) -> Unit
) {

    // Temporary navbar. Look so shit. Need to recreate to beautiful
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(pillColor)
            .navigationBarsPadding()
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            containerColor = pillColor,
            tonalElevation = 0.dp,
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            items.forEachIndexed { index, icon ->
                NavigationBarItem(
                    selected = selectedIndex == index,
                    onClick = { onItemSelected(index) },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = icon.name
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = activeIconColor,
                        unselectedIconColor = inactiveIconColor,
                        indicatorColor = activeIconColor.copy(alpha = 0.16f)
                    )
                )
            }
        }
    }
}
package com.sadistictech.foodollamatracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sadistictech.presentation.ui.WHITE

// Temporary desition
private val DarkColorScheme = darkColorScheme(
    background = WHITE
)

// Temporary desition
private val LightColorScheme = lightColorScheme(
    background = WHITE
)

@Composable
fun FoodOllamaTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.systemBars))
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}
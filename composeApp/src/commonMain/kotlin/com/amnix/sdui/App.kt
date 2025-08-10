package com.amnix.sdui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.amnix.sdui.screens.SduiPlaygroundScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import sdui_kt.composeapp.generated.resources.Res
import sdui_kt.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    var isDarkMode by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme(),
    ) {
        SduiPlaygroundScreen(
            isDarkMode = isDarkMode,
            onDarkModeChange = { isDarkMode = it },
        )
    }
}

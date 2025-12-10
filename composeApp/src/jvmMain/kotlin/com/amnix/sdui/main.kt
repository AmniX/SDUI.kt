package com.amnix.sdui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "sdui-kt",
        state = rememberWindowState(placement = WindowPlacement.Fullscreen),
    ) {
        App()
    }
}

package com.amnix.sdui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.amnix.sdui.components.ResponsiveLayout
import com.amnix.sdui.data.SduiDemoExamples
import com.amnix.sdui.sdui.SduiSerializer
import com.amnix.sdui.sdui.model.SduiAction
import com.amnix.sdui.sdui.renderer.ActionDispatcher
import com.amnix.sdui.sdui.renderer.FormState

@Composable
fun SduiPlaygroundScreen() {
    var selectedExample by remember { mutableStateOf(SduiDemoExamples.examples.first()) }
    var jsonInput by remember { mutableStateOf(selectedExample.json) }
    var actionMessage by remember { mutableStateOf<String?>(null) }
    var mobilePreviewDarkMode by remember { mutableStateOf(false) }

    // Load JSON from resources when example changes
    LaunchedEffect(selectedExample) {
        val loadedExample = SduiDemoExamples.loadExample(selectedExample)
        selectedExample = loadedExample
        jsonInput = loadedExample.json
    }

    val formState = remember { mutableStateMapOf<String, Any?>() }

    // Functional dispatcher for handling actions
    val dispatcher = remember {
        object : ActionDispatcher {
            override fun dispatch(action: SduiAction) {
                when (action) {
                    is SduiAction.Navigate -> {
                        if (action.route == "success") {
                            actionMessage = "✅ Form submitted! Data: ${formState.toMap()}"
                        } else {
                            actionMessage = "🧭 Navigation to: ${action.route}"
                        }
                    }

                    is SduiAction.Reset -> {
                        FormState.clearAllFormValues(formState)
                        actionMessage = "🔄 Form reset!"
                    }

                    else -> {
                        actionMessage = "ℹ️ Action: ${action::class.simpleName}"
                    }
                }
            }
        }
    }

    // Auto-clear action message
    LaunchedEffect(actionMessage) {
        if (actionMessage != null) {
            kotlinx.coroutines.delay(3000)
            actionMessage = null
        }
    }

    val parsedComponent = remember(jsonInput) {
        runCatching { SduiSerializer.deserialize(jsonInput) }
    }

    // Use the responsive layout component
    ResponsiveLayout(
        selectedExample = selectedExample,
        onExampleChange = { selectedExample = it },
        jsonInput = jsonInput,
        onJsonChange = { jsonInput = it },
        actionMessage = actionMessage,
        onActionMessage = { actionMessage = it },
        parsedComponent = parsedComponent,
        dispatcher = dispatcher,
        formState = formState,
    )
}

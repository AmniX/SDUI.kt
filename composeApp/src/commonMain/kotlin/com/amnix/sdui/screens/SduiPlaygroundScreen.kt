package com.amnix.sdui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.amnix.sdui.components.LogEntry
import com.amnix.sdui.components.LogType
import com.amnix.sdui.components.ResponsiveLayout
import com.amnix.sdui.components.createLogEntry
import com.amnix.sdui.data.SduiDemoExamples
import com.amnix.sdui.sdui.SduiSerializer
import com.amnix.sdui.sdui.SerializationResult
import com.amnix.sdui.sdui.model.SduiAction
import com.amnix.sdui.sdui.renderer.ActionDispatcher
import com.amnix.sdui.sdui.renderer.FormState

@Composable
fun SduiPlaygroundScreen() {
    var selectedExample by remember { mutableStateOf(SduiDemoExamples.examples.first()) }
    var jsonInput by remember { mutableStateOf(selectedExample.json) }
    var jsonFontSize by remember { mutableStateOf(14) }

    // Use a list to store all log entries instead of a single message
    val logs = remember { mutableStateListOf<LogEntry>() }

    // Keep track of the last successfully parsed component for fallback rendering
    var lastWorkingComponent by remember { mutableStateOf<com.amnix.sdui.sdui.components.SduiComponent?>(null) }

    // Load JSON from resources when example changes
    LaunchedEffect(selectedExample) {
        val loadedExample = SduiDemoExamples.loadExample(selectedExample)
        selectedExample = loadedExample
        jsonInput = loadedExample.json

        // Add a log entry when example changes
        logs.add(createLogEntry("Loaded example: ${selectedExample.name}", LogType.INFO))

        // Try to parse the initial JSON and set it as the last working component
        try {
            val initialResult = SduiSerializer.deserializeWithValidation(loadedExample.json)
            if (initialResult is SerializationResult.Success) {
                lastWorkingComponent = initialResult.data
                logs.add(createLogEntry("✅ Initial component loaded successfully", LogType.SUCCESS))
            }
        } catch (e: Exception) {
            logs.add(createLogEntry("⚠️ Initial component failed to load: ${e.message}", LogType.WARNING))
        }
    }

    val formState = remember { mutableStateMapOf<String, Any?>() }

    // Functional dispatcher for handling actions
    val dispatcher = remember {
        object : ActionDispatcher {
            override fun dispatch(action: SduiAction) {
                when (action) {
                    is SduiAction.Navigate -> {
                        val route = action.route
                        if (route == null) {
                            logs.add(createLogEntry("⚠️ Navigation action missing route", LogType.WARNING))
                        } else if (route == "success") {
                            logs.add(createLogEntry("✅ Form submitted! Data: ${formState.toMap()}", LogType.SUCCESS))
                        } else {
                            logs.add(createLogEntry("🧭 Navigation to: $route", LogType.ACTION))
                        }
                    }

                    is SduiAction.Reset -> {
                        FormState.clearAllFormValues(formState)
                        logs.add(createLogEntry("🔄 Form reset!", LogType.ACTION))
                    }

                    else -> {
                        logs.add(createLogEntry("ℹ️ Action: ${action::class.simpleName}", LogType.INFO))
                    }
                }
            }
        }
    }

    // Use the new error handling system with fallback to last working component
    val parsedComponent = remember(jsonInput) {
        when (val result = SduiSerializer.deserializeWithValidation(jsonInput)) {
            is SerializationResult.Success -> {
                logs.add(createLogEntry("✅ JSON parsed successfully", LogType.SUCCESS))
                // Update the last working component
                lastWorkingComponent = result.data
                Result.success(result.data)
            }
            is SerializationResult.Error -> {
                val errorMessage = "❌ ${result.message}: ${result.details ?: ""}"
                logs.add(createLogEntry(errorMessage, LogType.ERROR))

                // If we have a last working component, show a fallback message
                if (lastWorkingComponent != null) {
                    logs.add(
                        createLogEntry("⚠️ Rendering last working component while you fix the JSON", LogType.WARNING),
                    )
                } else {
                    logs.add(createLogEntry("🔍 No previous component to fall back to", LogType.INFO))
                }

                // Return the last working component as a fallback, or failure if none exists
                if (lastWorkingComponent != null) {
                    Result.success(lastWorkingComponent!!)
                } else {
                    Result.failure(IllegalArgumentException("${result.message}: ${result.details ?: ""}"))
                }
            }
        }
    }

    // Use the responsive layout component
    ResponsiveLayout(
        selectedExample = selectedExample,
        onExampleChange = { selectedExample = it },
        jsonInput = jsonInput,
        onJsonChange = { jsonInput = it },
        logs = logs,
        onClearLogs = { logs.clear() },
        parsedComponent = parsedComponent,
        dispatcher = dispatcher,
        formState = formState,
        jsonFontSize = jsonFontSize,
        onJsonFontSizeChange = { jsonFontSize = it },
    )
}

package com.amnix.sdui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amnix.sdui.getPlatform
import com.amnix.sdui.sdui.SduiSerializer
import com.amnix.sdui.sdui.components.SduiComponent
import com.amnix.sdui.sdui.renderer.ActionDispatcher
import com.amnix.sdui.sdui.renderer.LoggingActionDispatcher
import com.amnix.sdui.sdui.renderer.RenderSduiComponent

@Composable
fun SduiPlaygroundScreen(
    isDarkMode: Boolean = false,
    onDarkModeChange: (Boolean) -> Unit = {},
) {
    var jsonInput by remember { mutableStateOf("") }

    // Initialize with sample JSON
    LaunchedEffect(Unit) {
        jsonInput =
            """
            {
                "type": "column",
                "id": "main-container",
                "children": [
                    {
                        "type": "text",
                        "id": "title",
                        "text": "Welcome to SDUI Playground!",
                        "style": {
                            "fontSize": 24,
                            "fontWeight": "bold",
                            "textColor": "#1976D2",
                            "alignment": "center"
                        }
                    },
                    {
                        "type": "spacer",
                        "id": "spacer-1",
                        "height": 16
                    },
                    {
                        "type": "text",
                        "id": "description",
                        "text": "This is a sample form to test SDUI components. Try editing the JSON above to see live changes!",
                        "style": {
                            "fontSize": 16,
                            "textColor": "#666666",
                            "alignment": "center"
                        }
                    },
                    {
                        "type": "spacer",
                        "id": "spacer-2",
                        "height": 24
                    },
                    {
                        "type": "textField",
                        "id": "name",
                        "placeholder": "Enter your name",
                        "value": "",
                        "style": {
                            "width": "100%"
                        }
                    },
                    {
                        "type": "spacer",
                        "id": "spacer-3",
                        "height": 16
                    },
                    {
                        "type": "textField",
                        "id": "email",
                        "placeholder": "Enter your email",
                        "value": "",
                        "style": {
                            "width": "100%"
                        }
                    },
                    {
                        "type": "spacer",
                        "id": "spacer-4",
                        "height": 16
                    },
                    {
                        "type": "switch",
                        "id": "newsletter",
                        "label": "Subscribe to newsletter",
                        "checked": false
                    },
                    {
                        "type": "spacer",
                        "id": "spacer-5",
                        "height": 16
                    },
                    {
                        "type": "checkbox",
                        "id": "terms",
                        "label": "I agree to the terms and conditions",
                        "checked": false
                    },
                    {
                        "type": "spacer",
                        "id": "spacer-6",
                        "height": 24
                    },
                    {
                        "type": "row",
                        "id": "button-row",
                        "children": [
                            {
                                "type": "button",
                                "id": "submit",
                                "text": "Submit",
                                "action": {
                                    "type": "navigate",
                                    "payload": {
                                        "route": "success"
                                    }
                                },
                                "style": {
                                    "flex": 1
                                }
                            },
                            {
                                "type": "spacer",
                                "id": "button-spacer",
                                "width": 16
                            },
                            {
                                "type": "button",
                                "id": "reset",
                                "text": "Reset",
                                "action": {
                                    "type": "reset",
                                    "payload": {}
                                },
                                "style": {
                                    "flex": 1
                                }
                            }
                        ]
                    },
                    {
                        "type": "spacer",
                        "id": "spacer-7",
                        "height": 24
                    },
                    {
                        "type": "card",
                        "id": "info-card",
                        "children": [
                            {
                                "type": "text",
                                "id": "card-title",
                                "text": "Form Data Preview",
                                "style": {
                                    "fontSize": 18,
                                    "fontWeight": "bold"
                                }
                            },
                            {
                                "type": "spacer",
                                "id": "card-spacer",
                                "height": 8
                            },
                            {
                                "type": "text",
                                "id": "card-content",
                                "text": "Click 'Submit Form' to see the current form state in the console.",
                                "style": {
                                    "fontSize": 14,
                                    "textColor": "#666666"
                                }
                            }
                        ]
                    }
                ]
            }
            """.trimIndent()
    }

    val parsedComponentResult =
        remember(jsonInput) {
            runCatching { SduiSerializer.deserialize(jsonInput) }.onFailure {
                it.printStackTrace()
            }
        }

    val formState = remember { mutableStateMapOf<String, Any?>() }
    val dispatcher = remember { LoggingActionDispatcher() }

    val platform = getPlatform()
    val isDesktopOrWasm = platform.name.contains("Java") || platform.name.contains("Web")

    if (isDesktopOrWasm) {
        // Desktop/Wasm layout using Row
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
                    .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Left side - JSON Editor
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "SDUI JSON Playground",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    // Dark mode toggle
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = onDarkModeChange,
                    )
                }

                // JSON Editor
                Text(
                    text = "JSON Input",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                OutlinedTextField(
                    value = jsonInput,
                    onValueChange = { jsonInput = it },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                    placeholder = { Text("Paste your SDUI JSON here...") },
                    textStyle =
                        MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                )

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(
                        onClick = {
                            jsonInput =
                                """
                                {
                                    "type": "column",
                                    "id": "main-container",
                                    "children": [
                                        {
                                            "type": "text",
                                            "id": "title",
                                            "text": "Welcome to SDUI Playground!",
                                            "style": {
                                                "fontSize": 24,
                                                "fontWeight": "bold",
                                                "textColor": "#1976D2",
                                                "alignment": "center"
                                            }
                                        },
                                        {
                                            "type": "spacer",
                                            "id": "spacer-1",
                                            "height": 16
                                        },
                                        {
                                            "type": "text",
                                            "id": "description",
                                            "text": "This is a sample form to test SDUI components. Try editing the JSON above to see live changes!",
                                            "style": {
                                                "fontSize": 16,
                                                "textColor": "#666666",
                                                "alignment": "center"
                                            }
                                        },
                                        {
                                            "type": "spacer",
                                            "id": "spacer-2",
                                            "height": 24
                                        },
                                        {
                                            "type": "textField",
                                            "id": "name",
                                            "placeholder": "Enter your name",
                                            "value": "",
                                            "style": {
                                                "width": "100%"
                                            }
                                        },
                                        {
                                            "type": "spacer",
                                            "id": "spacer-3",
                                            "height": 16
                                        },
                                        {
                                            "type": "textField",
                                            "id": "email",
                                            "placeholder": "Enter your email",
                                            "value": "",
                                            "style": {
                                                "width": "100%"
                                            }
                                        },
                                        {
                                            "type": "spacer",
                                            "id": "spacer-4",
                                            "height": 16
                                        },
                                        {
                                            "type": "switch",
                                            "id": "newsletter",
                                            "label": "Subscribe to newsletter",
                                            "checked": false
                                        },
                                        {
                                            "type": "spacer",
                                            "id": "spacer-5",
                                            "height": 16
                                        },
                                        {
                                            "type": "checkbox",
                                            "id": "terms",
                                            "label": "I agree to the terms and conditions",
                                            "checked": false
                                        },
                                        {
                                            "type": "spacer",
                                            "id": "spacer-6",
                                            "height": 24
                                        },
                                        {
                                            "type": "row",
                                            "id": "button-row",
                                            "children": [
                                                {
                                                    "type": "button",
                                                    "id": "submit",
                                                    "text": "Submit",
                                                    "action": {
                                                        "type": "navigate",
                                                        "payload": {
                                                            "route": "success"
                                                        }
                                                    },
                                                    "style": {
                                                        "flex": 1
                                                    }
                                                },
                                                {
                                                    "type": "spacer",
                                                    "id": "button-spacer",
                                                    "width": 16
                                                },
                                                {
                                                    "type": "button",
                                                    "id": "reset",
                                                    "text": "Reset",
                                                    "action": {
                                                        "type": "reset",
                                                        "payload": {}
                                                    },
                                                    "style": {
                                                        "flex": 1
                                                    }
                                                }
                                            ]
                                        },
                                        {
                                            "type": "spacer",
                                            "id": "spacer-7",
                                            "height": 24
                                        },
                                        {
                                            "type": "card",
                                            "id": "info-card",
                                            "children": [
                                                {
                                                    "type": "text",
                                                    "id": "card-title",
                                                    "text": "Form Data Preview",
                                                    "style": {
                                                        "fontSize": 18,
                                                        "fontWeight": "bold"
                                                    }
                                                },
                                                {
                                                    "type": "spacer",
                                                    "id": "card-spacer",
                                                    "height": 8
                                                },
                                                {
                                                    "type": "text",
                                                    "id": "card-content",
                                                    "text": "Click 'Submit Form' to see the current form state in the console.",
                                                    "style": {
                                                        "fontSize": 14,
                                                        "textColor": "#666666"
                                                    }
                                                }
                                            ]
                                        }
                                    ]
                                }
                                """.trimIndent()
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Load Sample JSON")
                    }

                    Button(
                        onClick = {
                            println("Form State: ${formState.toMap()}")
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Submit Form")
                    }
                }
            }

            // Right side - Rendering Area
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Live Preview",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                // Error Display
                parsedComponentResult.exceptionOrNull()?.let { error ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                        ) {
                            Text(
                                text = "Parsing Error",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Red,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = error.message ?: "Unknown error occurred",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Red,
                            )
                        }
                    }
                }

                // Rendering Area
                parsedComponentResult.getOrNull()?.let { component ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                            ),
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                        ) {
                            RenderSduiComponent(
                                component = component,
                                dispatcher = dispatcher,
                                formState = formState,
                            )
                        }
                    }
                }
            }
        }
    } else {
        // Mobile layout using Column
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "SDUI JSON Playground",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                // Dark mode toggle
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = onDarkModeChange,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // JSON Editor Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = "JSON Input",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = jsonInput,
                        onValueChange = { jsonInput = it },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                        placeholder = { Text("Paste your SDUI JSON here...") },
                        textStyle =
                            MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurface,
                            ),
                        colors =
                            OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Button(
                            onClick = {
                                jsonInput =
                                    """
                                    {
                                        "type": "column",
                                        "id": "main-container",
                                        "children": [
                                            {
                                                "type": "text",
                                                "id": "title",
                                                "text": "Welcome to SDUI Playground!",
                                                "style": {
                                                    "fontSize": 24,
                                                    "fontWeight": "bold",
                                                    "textColor": "#1976D2",
                                                    "alignment": "center"
                                                }
                                            },
                                            {
                                                "type": "spacer",
                                                "id": "spacer-1",
                                                "height": 16
                                            },
                                            {
                                                "type": "text",
                                                "id": "description",
                                                "text": "This is a sample form to test SDUI components. Try editing the JSON above to see live changes!",
                                                "style": {
                                                    "fontSize": 16,
                                                    "textColor": "#666666",
                                                    "alignment": "center"
                                                }
                                            },
                                            {
                                                "type": "spacer",
                                                "id": "spacer-2",
                                                "height": 24
                                            },
                                            {
                                                "type": "textField",
                                                "id": "name",
                                                "placeholder": "Enter your name",
                                                "value": "",
                                                "style": {
                                                    "width": "100%"
                                                }
                                            },
                                            {
                                                "type": "spacer",
                                                "id": "spacer-3",
                                                "height": 16
                                            },
                                            {
                                                "type": "textField",
                                                "id": "email",
                                                "placeholder": "Enter your email",
                                                "value": "",
                                                "style": {
                                                    "width": "100%"
                                                }
                                            },
                                            {
                                                "type": "spacer",
                                                "id": "spacer-4",
                                                "height": 16
                                            },
                                            {
                                                "type": "switch",
                                                "id": "newsletter",
                                                "label": "Subscribe to newsletter",
                                                "checked": false
                                            },
                                            {
                                                "type": "spacer",
                                                "id": "spacer-5",
                                                "height": 16
                                            },
                                            {
                                                "type": "checkbox",
                                                "id": "terms",
                                                "label": "I agree to the terms and conditions",
                                                "checked": false
                                            },
                                            {
                                                "type": "spacer",
                                                "id": "spacer-6",
                                                "height": 24
                                            },
                                            {
                                                "type": "row",
                                                "id": "button-row",
                                                "children": [
                                                    {
                                                        "type": "button",
                                                        "id": "submit",
                                                        "text": "Submit",
                                                        "action": {
                                                            "type": "navigate",
                                                            "payload": {
                                                                "route": "success"
                                                            }
                                                        },
                                                        "style": {
                                                            "flex": 1
                                                        }
                                                    },
                                                    {
                                                        "type": "spacer",
                                                        "id": "button-spacer",
                                                        "width": 16
                                                    },
                                                    {
                                                        "type": "button",
                                                        "id": "reset",
                                                        "text": "Reset",
                                                        "action": {
                                                            "type": "reset",
                                                            "payload": {}
                                                        },
                                                        "style": {
                                                            "flex": 1
                                                        }
                                                    }
                                                ]
                                            },
                                            {
                                                "type": "spacer",
                                                "id": "spacer-7",
                                                "height": 24
                                            },
                                            {
                                                "type": "card",
                                                "id": "info-card",
                                                "children": [
                                                    {
                                                        "type": "text",
                                                        "id": "card-title",
                                                        "text": "Form Data Preview",
                                                        "style": {
                                                            "fontSize": 18,
                                                            "fontWeight": "bold"
                                                        }
                                                    },
                                                    {
                                                        "type": "spacer",
                                                        "id": "card-spacer",
                                                        "height": 8
                                                    },
                                                    {
                                                        "type": "text",
                                                        "id": "card-content",
                                                        "text": "Click 'Submit Form' to see the current form state in the console.",
                                                        "style": {
                                                            "fontSize": 14,
                                                            "textColor": "#666666"
                                                        }
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                    """.trimIndent()
                            },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Load Sample JSON")
                        }

                        Button(
                            onClick = {
                                println("Form State: ${formState.toMap()}")
                            },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Submit Form")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Error Display
            parsedComponentResult.exceptionOrNull()?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = "Parsing Error",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Red,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error.message ?: "Unknown error occurred",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Red,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Rendering Area
            parsedComponentResult.getOrNull()?.let { component ->
                Text(
                    text = "Rendered UI",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        RenderSduiComponent(
                            component = component,
                            dispatcher = dispatcher,
                            formState = formState,
                        )
                    }
                }
            }
        }
    }
}

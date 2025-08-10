package com.amnix.sdui.sdui.renderer

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import com.amnix.sdui.sdui.SduiSerializer
import com.amnix.sdui.sdui.components.SduiComponent
import com.amnix.sdui.sdui.model.SduiAction

/**
 * Demo composable showing how to use the SDUI renderer
 */
@Composable
fun SduiRendererDemo() {
    MaterialTheme {
        val formState = remember { mutableStateMapOf<String, Any?>() }
        val dispatcher = LoggingActionDispatcher()

        // Example JSON for a simple form
        val jsonString =
            """
            {
              "type": "Column",
              "id": "main_form",
              "style": {
                "padding": {
                  "all": 16
                },
                "spacing": 8
              },
              "children": [
                {
                  "type": "Text",
                  "id": "title",
                  "text": "User Registration Form",
                  "style": {
                    "fontSize": 24,
                    "fontWeight": "bold",
                    "textAlign": "center"
                  }
                },
                {
                  "type": "TextField",
                  "id": "name_field",
                  "placeholder": "Enter your name",
                  "style": {
                    "width": 300
                  }
                },
                {
                  "type": "TextField",
                  "id": "email_field",
                  "placeholder": "Enter your email",
                  "keyboardType": "email",
                  "style": {
                    "width": 300
                  }
                },
                {
                  "type": "TextField",
                  "id": "password_field",
                  "placeholder": "Enter your password",
                  "isPassword": true,
                  "style": {
                    "width": 300
                  }
                },
                {
                  "type": "Row",
                  "id": "preferences_row",
                  "style": {
                    "spacing": 16
                  },
                  "children": [
                    {
                      "type": "Checkbox",
                      "id": "newsletter_checkbox",
                      "label": "Subscribe to newsletter",
                      "checked": false
                    },
                    {
                      "type": "Switch",
                      "id": "notifications_switch",
                      "label": "Enable notifications",
                      "checked": true
                    }
                  ]
                },
                {
                  "type": "Slider",
                  "id": "age_slider",
                  "label": "Age",
                  "value": 25,
                  "minValue": 18,
                  "maxValue": 100,
                  "step": 1,
                  "style": {
                    "width": 300
                  }
                },
                {
                  "type": "Button",
                  "id": "submit_button",
                  "text": "Submit Registration",
                  "style": {
                    "backgroundColor": "#007AFF",
                    "textColor": "#FFFFFF",
                    "cornerRadius": 8,
                    "padding": {
                      "horizontal": 24,
                      "vertical": 12
                    }
                  },
                  "action": {
                    "type": "api_call",
                    "url": "/api/register",
                    "method": "POST"
                  }
                }
              ]
            }
            """.trimIndent()

        val component =
            try {
                SduiSerializer.deserialize(jsonString)
            } catch (e: Exception) {
                // Handle deserialization errors
                println("Error deserializing component: ${e.message}")
                null
            }
        component?.let { RenderSduiComponent(it, dispatcher, formState) }
    }
}

/**
 * Example of a custom action dispatcher
 */
class CustomActionDispatcher : ActionDispatcher {
    override fun dispatch(action: SduiAction) {
        when (action) {
            is SduiAction.ApiCall -> {
                println("Making API call to: ${action.url}")
                println("Method: ${action.method}")
                println("Headers: ${action.headers}")
                println("Body: ${action.body}")
            }
            is SduiAction.Navigate -> {
                println("Navigating to: ${action.route}")
                println("Payload: ${action.payload}")
            }
            is SduiAction.ShowDialog -> {
                println("Showing dialog: ${action.title}")
                println("Message: ${action.message}")
            }
            is SduiAction.UpdateState -> {
                println("Updating state: ${action.key} = ${action.value}")
            }
            is SduiAction.Reset -> {
                println("Resetting form state")
                println("Payload: ${action.payload}")
            }
            is SduiAction.Custom -> {
                println("Custom action: ${action.action}")
                println("Data: ${action.data}")
            }
        }
    }
}

/**
 * Example usage with form submission
 */
@Composable
fun SduiFormExample() {
    MaterialTheme {
        val formState = remember { mutableStateMapOf<String, Any?>() }
        val dispatcher = CustomActionDispatcher()

        // Example form component
        val formComponent =
            SduiComponent.ColumnComponent(
                id = "example_form",
                children =
                    listOf(
                        SduiComponent.TextComponent(
                            id = "form_title",
                            text = "Example Form",
                            style =
                                com.amnix.sdui.sdui.model.Style(
                                    fontSize = 20f,
                                    fontWeight = "bold",
                                ),
                        ),
                        SduiComponent.TextFieldComponent(
                            id = "name_input",
                            placeholder = "Enter name",
                            value = "",
                        ),
                        SduiComponent.ButtonComponent(
                            id = "submit_btn",
                            text = "Submit",
                            action =
                                SduiAction.ApiCall(
                                    url = "/api/submit",
                                    method = "POST",
                                ),
                        ),
                    ),
            )

        RenderSduiComponent(formComponent, dispatcher, formState)
    }
} 

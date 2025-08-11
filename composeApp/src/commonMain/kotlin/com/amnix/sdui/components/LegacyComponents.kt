package com.amnix.sdui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amnix.sdui.data.DemoExample
import com.amnix.sdui.data.SduiDemoExamples

/**
 * Legacy components for backward compatibility and compact layout usage
 */

@Composable
fun ExampleSelectorCard(
    selectedExample: DemoExample,
    isDropdownExpanded: Boolean,
    onDropdownExpandedChange: (Boolean) -> Unit,
    onExampleChange: (DemoExample) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Demo Examples",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )
                
                // Reset button
                OutlinedButton(
                    onClick = onReset,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.secondary,
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(36.dp),
                ) {
                    Text(
                        text = "Reset",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { onDropdownExpandedChange(true) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("${selectedExample.name} - ${selectedExample.description}")
            }

            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { onDropdownExpandedChange(false) },
            ) {
                SduiDemoExamples.examples.forEach { example ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = example.name,
                                    fontWeight = FontWeight.Medium,
                                )
                                Text(
                                    text = example.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        },
                        onClick = {
                            onExampleChange(example.copy(json = "")) // Reset JSON to trigger reload
                            onDropdownExpandedChange(false)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun JsonEditorCard(
    jsonInput: String,
    onJsonChange: (String) -> Unit,
    selectedExample: DemoExample,
    formState: MutableMap<String, Any?>,
    parsedComponent: Result<com.amnix.sdui.sdui.components.SduiComponent>,
    fontSize: Int = 14,
    onFontSizeChange: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val parseError = parsedComponent.exceptionOrNull()
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "JSON Configuration",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )
                
                // Font size controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    OutlinedButton(
                        onClick = { 
                            if (fontSize > 8) onFontSizeChange(fontSize - 1)
                        },
                        modifier = Modifier.size(28.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        ),
                        border = BorderStroke(
                            1.dp, 
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(4.dp),
                    ) {
                        Text(
                            text = "−",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    
                    Text(
                        text = "${fontSize}sp",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.width(40.dp),
                        textAlign = TextAlign.Center,
                    )
                    
                    OutlinedButton(
                        onClick = { 
                            if (fontSize < 24) onFontSizeChange(fontSize + 1)
                        },
                        modifier = Modifier.size(28.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        ),
                        border = BorderStroke(
                            1.dp, 
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(4.dp),
                    ) {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = jsonInput,
                onValueChange = onJsonChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (parseError != null) 120.dp else 150.dp),
                placeholder = { 
                    Text(
                        "Enter SDUI JSON...",
                        fontSize = fontSize.sp,
                        fontFamily = FontFamily.Monospace,
                    ) 
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = fontSize.sp,
                ),
                isError = parseError != null,
            )
            
            // Simple error display for legacy component
            if (parseError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                    ),
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(text = "⚠️")
                        Column {
                            Text(
                                text = "JSON Error",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                            )
                            Text(
                                text = parseError.message ?: "Invalid JSON",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        onJsonChange(
                            selectedExample.json.ifEmpty {
                                // If JSON not loaded yet, trigger a reload
                                "Loading..."
                            },
                        )
                    },
                ) {
                    Text("Reset to Example")
                }
            }
        }
    }
}



@Composable
fun ErrorCard(error: Throwable, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f)),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "❌ JSON Parse Error",
                fontWeight = FontWeight.Bold,
                color = Color.Red,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = error.message ?: "Unknown error",
                color = Color.Red,
            )
        }
    }
}

package com.amnix.sdui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amnix.sdui.data.DemoExample
import com.amnix.sdui.data.SduiDemoExamples

/**
 * Modern header section with title, subtitle, and dark mode toggle
 */
@Composable
fun ModernHeaderSection(isDarkMode: Boolean, onDarkModeChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "SDUI Playground",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Build dynamic UIs with JSON",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = if (isDarkMode) "🌙" else "☀️",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = onDarkModeChange,
                )
            }
        }

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    MaterialTheme.colorScheme.outline.copy(
                        alpha = if (isDarkMode) 0.1f else 0.2f,
                    ),
                ),
        )
    }
}

/**
 * Modern header section without dark mode toggle (for left panel)
 */
@Composable
fun ModernHeaderSectionWithoutToggle(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column {
            Text(
                text = "SDUI Playground",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "Build dynamic UIs with JSON",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        }

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                ),
        )
    }
}

/**
 * Ultra-compact header with title and minimalistic sample chooser in one row
 */
@Composable
fun CompactHeaderWithSampleChooser(
    selectedExample: DemoExample,
    isDropdownExpanded: Boolean,
    onDropdownExpandedChange: (Boolean) -> Unit,
    onExampleChange: (DemoExample) -> Unit,
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Single row with title and minimalistic sample chooser
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left side - App title and subtitle
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "SDUI Playground",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Build dynamic UIs with JSON",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }

            // Right side - Minimalistic sample chooser
            Box {
                OutlinedButton(
                    onClick = { onDropdownExpandedChange(true) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(36.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = selectedExample.name,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = "▼",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                        )
                    }
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
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    Text(
                                        text = example.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                    )
                                }
                            },
                            onClick = {
                                onExampleChange(example.copy(json = ""))
                                onDropdownExpandedChange(false)
                            },
                        )
                    }
                }
            }
        }

        // Subtle divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                ),
        )
    }
}

/**
 * Modern example selector with dropdown
 */
@Composable
fun ModernExampleSelector(
    selectedExample: DemoExample,
    isDropdownExpanded: Boolean,
    onDropdownExpandedChange: (Boolean) -> Unit,
    onExampleChange: (DemoExample) -> Unit,
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionHeader(
            title = "Examples",
            color = MaterialTheme.colorScheme.primary,
        )

        ElevatedCard(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(
                    alpha = if (isDarkMode) 0.85f else 1.0f,
                ),
            ),
            shape = RoundedCornerShape(12.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = { onDropdownExpandedChange(true) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                text = selectedExample.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                            )
                            Text(
                                text = selectedExample.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            )
                        }
                        Text(text = "▼")
                    }
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
                                onExampleChange(example.copy(json = ""))
                                onDropdownExpandedChange(false)
                            },
                        )
                    }
                }
            }
        }
    }
}

/**
 * Code editor-style JSON editor with syntax highlighting and developer-friendly design
 */
@Composable
fun ModernJsonEditor(
    jsonInput: String,
    onJsonChange: (String) -> Unit,
    selectedExample: DemoExample,
    formState: MutableMap<String, Any?>,
    onShowFormData: (String) -> Unit,
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Minimal header with code editor styling
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = Color(0xFF4CAF50), // Green dot like VS Code
                            shape = RoundedCornerShape(50.dp),
                        ),
                )
                Text(
                    text = "config.json",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            
            Text(
                text = "${jsonInput.lines().size} lines",
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }

        // Code editor-style container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFFAFAFA),
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 1.dp,
                    color = if (isDarkMode) Color(0xFF3C3C3C) else Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(12.dp),
                ),
        ) {
            Column {
                // Editor header with controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isDarkMode) Color(0xFF2D2D2D) else Color(0xFFF0F0F0),
                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Traffic light dots (macOS style)
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color(0xFFFF5F57), RoundedCornerShape(50.dp)),
                        )
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color(0xFFFFBD2E), RoundedCornerShape(50.dp)),
                        )
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color(0xFF28CA42), RoundedCornerShape(50.dp)),
                        )
                    }
                    
                    Text(
                        text = "JSON",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace,
                        color = if (isDarkMode) Color(0xFF9CA3AF) else Color(0xFF6B7280),
                    )
                }

                // JSON Text Field with code editor styling
                OutlinedTextField(
                    value = jsonInput,
                    onValueChange = onJsonChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(650.dp),
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = if (isDarkMode) Color(0xFFE5E7EB) else Color(0xFF1F2937),
                    ),
                    placeholder = {
                        Text(
                            "{\n  \"type\": \"column\",\n  \"children\": [\n    // Add your JSON here...\n  ]\n}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            color = if (isDarkMode) Color(0xFF6B7280) else Color(0xFF9CA3AF),
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = if (isDarkMode) Color(0xFF60A5FA) else Color(0xFF3B82F6),
                    ),
                    shape = RoundedCornerShape(0.dp),
                )
            }
        }

        // Action buttons with minimal styling
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(
                onClick = {
                    onJsonChange(
                        selectedExample.json.ifEmpty {
                            "Loading..."
                        },
                    )
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(6.dp),
            ) {
                Text(
                    "Reset",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                )
            }

            OutlinedButton(
                onClick = {
                    onShowFormData("📋 Form data: ${formState.toMap()}")
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.secondary,
                ),
                shape = RoundedCornerShape(6.dp),
            ) {
                Text(
                    "Debug",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

/**
 * Modern status card for displaying action messages
 */
@Composable
fun ModernStatusCard(message: String, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            IconContainer(
                icon = "ℹ️",
                backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

/**
 * Modern error display card
 */
@Composable
fun ModernErrorDisplay(error: Throwable, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            IconContainer(
                icon = "⚠️",
                backgroundColor = MaterialTheme.colorScheme.error.copy(alpha = 0.2f),
            )
            Column {
                Text(
                    text = "JSON Parse Error",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
                Text(
                    text = error.message ?: "Unknown error",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                )
            }
        }
    }
}

/**
 * Section header with colored indicator dot
 */
@Composable
private fun SectionHeader(title: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(
                    color = color,
                    shape = RoundedCornerShape(50.dp),
                ),
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

/**
 * Icon container with background
 */
@Composable
private fun IconContainer(
    icon: String,
    backgroundColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

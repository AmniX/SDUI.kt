package com.amnix.sdui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amnix.sdui.components.TerminalLogWindow
import com.amnix.sdui.components.LogEntry
import com.amnix.sdui.components.MobilePhonePreview
import com.amnix.sdui.data.DemoExample
import com.amnix.sdui.sdui.components.SduiComponent
import com.amnix.sdui.sdui.renderer.ActionDispatcher
import com.amnix.sdui.sdui.renderer.FormState
import com.amnix.sdui.sdui.renderer.RenderSduiComponent

/**
 * Responsive layout that switches between wide and compact based on available width
 */
@Composable
fun ResponsiveLayout(
    selectedExample: DemoExample,
    onExampleChange: (DemoExample) -> Unit,
    jsonInput: String,
    onJsonChange: (String) -> Unit,
    logs: List<LogEntry>,
    onClearLogs: () -> Unit,
    parsedComponent: Result<com.amnix.sdui.sdui.components.SduiComponent>,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
    jsonFontSize: Int,
    onJsonFontSizeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Use BoxWithConstraints for responsive layout based on available width
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        // Responsive breakpoint for layout switching:
        // - Wide layout (horizontal): 800dp+
        //   ✓ Desktop computers, laptops
        //   ✓ Tablets in landscape mode (10"+ tablets)
        //   ✓ Wide browser windows
        //   ✓ Foldable devices unfolded
        // - Compact layout (vertical): <800dp
        //   ✓ Mobile phones (all orientations)
        //   ✓ Tablets in portrait mode
        //   ✓ Narrow browser windows
        //   ✓ Small desktop windows
        val isWideLayout = maxWidth >= 800.dp

        if (isWideLayout) {
            // Wide Layout: Left-to-Right (Desktop, Tablets, Wide Browsers)
            WideLayout(
                selectedExample = selectedExample,
                onExampleChange = onExampleChange,
                jsonInput = jsonInput,
                onJsonChange = onJsonChange,
                logs = logs,
                onClearLogs = onClearLogs,
                parsedComponent = parsedComponent,
                dispatcher = dispatcher,
                formState = formState,
                jsonFontSize = jsonFontSize,
                onJsonFontSizeChange = onJsonFontSizeChange,
            )
        } else {
            // Compact Layout: Single Column (Mobile, Narrow Tablets, Small Windows)
            CompactLayout(
                selectedExample = selectedExample,
                onExampleChange = onExampleChange,
                jsonInput = jsonInput,
                onJsonChange = onJsonChange,
                logs = logs,
                onClearLogs = onClearLogs,
                parsedComponent = parsedComponent,
                dispatcher = dispatcher,
                formState = formState,
                jsonFontSize = jsonFontSize,
                onJsonFontSizeChange = onJsonFontSizeChange,
            )
        }
    }
}

/**
 * Wide layout for desktop and tablet landscape
 */
@Composable
fun WideLayout(
    selectedExample: DemoExample,
    onExampleChange: (DemoExample) -> Unit,
    jsonInput: String,
    onJsonChange: (String) -> Unit,
    logs: List<LogEntry>,
    onClearLogs: () -> Unit,
    parsedComponent: Result<com.amnix.sdui.sdui.components.SduiComponent>,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
    jsonFontSize: Int,
    onJsonFontSizeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            // Left Panel - Controls and JSON Editor
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.6f),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    // Compact Header with Sample Chooser at the top
                    CompactHeaderWithSampleChooser(
                        selectedExample = selectedExample,
                        isDropdownExpanded = isDropdownExpanded,
                        onDropdownExpandedChange = { isDropdownExpanded = it },
                        onExampleChange = onExampleChange,
                        onReset = {
                            onJsonChange(selectedExample.json.ifEmpty { "Loading..." })
                        },
                    )

                    // JSON Editor
                    ModernJsonEditor(
                        jsonInput = jsonInput,
                        onJsonChange = onJsonChange,
                        selectedExample = selectedExample,
                        formState = formState,
                        parsedComponent = parsedComponent,
                        fontSize = jsonFontSize,
                        onFontSizeChange = onJsonFontSizeChange,
                    )

                    // Terminal Log Window
                    TerminalLogWindow(
                        logs = logs,
                        onClearLogs = onClearLogs
                    )
                }
            }

            // Right Panel - Mobile Preview
            MobilePreviewPanel(
                parsedComponent = parsedComponent,
                dispatcher = dispatcher,
                formState = formState,
                modifier = Modifier.width(400.dp)
            )
        }
    }
}

/**
 * Compact layout for mobile and narrow screens
 */
@Composable
fun CompactLayout(
    selectedExample: DemoExample,
    onExampleChange: (DemoExample) -> Unit,
    jsonInput: String,
    onJsonChange: (String) -> Unit,
    logs: List<LogEntry>,
    onClearLogs: () -> Unit,
    parsedComponent: Result<com.amnix.sdui.sdui.components.SduiComponent>,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
    jsonFontSize: Int,
    onJsonFontSizeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Header (simplified for compact layout)
        Text(
            text = "SDUI Playground",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
        )

        // Demo Example Selector
        ExampleSelectorCard(
            selectedExample = selectedExample,
            isDropdownExpanded = isDropdownExpanded,
            onDropdownExpandedChange = { isDropdownExpanded = it },
            onExampleChange = onExampleChange,
            onReset = {
                onJsonChange(selectedExample.json.ifEmpty { "Loading..." })
            },
        )

        // JSON Editor
        JsonEditorCard(
            jsonInput = jsonInput,
            onJsonChange = onJsonChange,
            selectedExample = selectedExample,
            formState = formState,
            parsedComponent = parsedComponent,
            fontSize = jsonFontSize,
            onFontSizeChange = onJsonFontSizeChange,
        )

        // Terminal Log Window
        TerminalLogWindow(
            logs = logs,
            onClearLogs = onClearLogs
        )

        // Rendered UI with mobile preview theming
        parsedComponent.getOrNull()?.let { component ->
            CompactPreviewCard(
                component = component,
                dispatcher = dispatcher,
                formState = formState,
            )
        }
    }
}

/**
 * Mobile preview panel for wide layout
 */
@Composable
private fun MobilePreviewPanel(
    parsedComponent: Result<com.amnix.sdui.sdui.components.SduiComponent>,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(420.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Preview Header (no dark mode toggle)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(50.dp),
                        ),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Mobile Preview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            // Center the phone vertically between header and bottom
            Spacer(modifier = Modifier.weight(1f))

            // Mobile Phone Frame
            MobilePhonePreview(
                parsedComponent = parsedComponent,
                dispatcher = dispatcher,
                formState = formState,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

/**
 * Compact preview card for narrow layouts
 */
@Composable
private fun CompactPreviewCard(
    component: com.amnix.sdui.sdui.components.SduiComponent,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
    modifier: Modifier = Modifier,
    isFallback: Boolean = false,
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
                    text = "🎨 Rendered UI",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )
                
                // Show fallback indicator if this is a fallback component
                if (isFallback) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "⚠️",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = "Fallback",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            RenderSduiComponent(
                component = component,
                dispatcher = dispatcher,
                formState = formState,
            )
        }
    }
}

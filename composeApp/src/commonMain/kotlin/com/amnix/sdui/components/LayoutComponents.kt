package com.amnix.sdui.components

import androidx.compose.foundation.background
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amnix.sdui.data.DemoExample
import com.amnix.sdui.sdui.renderer.ActionDispatcher
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
    actionMessage: String?,
    onActionMessage: (String) -> Unit,
    parsedComponent: Result<com.amnix.sdui.sdui.components.SduiComponent>,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
    mobilePreviewDarkMode: Boolean,
    onMobilePreviewDarkModeChange: (Boolean) -> Unit,
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
                actionMessage = actionMessage,
                onActionMessage = onActionMessage,
                parsedComponent = parsedComponent,
                dispatcher = dispatcher,
                formState = formState,
                mobilePreviewDarkMode = mobilePreviewDarkMode,
                onMobilePreviewDarkModeChange = onMobilePreviewDarkModeChange,
            )
        } else {
            // Compact Layout: Single Column (Mobile, Narrow Tablets, Small Windows)
            CompactLayout(
                selectedExample = selectedExample,
                onExampleChange = onExampleChange,
                jsonInput = jsonInput,
                onJsonChange = onJsonChange,
                actionMessage = actionMessage,
                onActionMessage = onActionMessage,
                parsedComponent = parsedComponent,
                dispatcher = dispatcher,
                formState = formState,
                mobilePreviewDarkMode = mobilePreviewDarkMode,
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
    actionMessage: String?,
    onActionMessage: (String) -> Unit,
    parsedComponent: Result<com.amnix.sdui.sdui.components.SduiComponent>,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
    mobilePreviewDarkMode: Boolean,
    onMobilePreviewDarkModeChange: (Boolean) -> Unit,
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
            // Left Panel - Controls and JSON Editor (expanded for more JSON space)
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.6f),
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
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
                        isDarkMode = false, // Always use light theme for left panel
                    )

                    // JSON Editor (now gets more space)
                    ModernJsonEditor(
                        jsonInput = jsonInput,
                        onJsonChange = onJsonChange,
                        selectedExample = selectedExample,
                        formState = formState,
                        onShowFormData = onActionMessage,
                        isDarkMode = false, // Always use light theme for left panel
                    )

                    // Status Messages
                    actionMessage?.let { message ->
                        ModernStatusCard(message)
                    }

                    parsedComponent.exceptionOrNull()?.let { error ->
                        ModernErrorDisplay(error)
                    }
                }
            }

            // Right Panel - Mobile Preview (fixed width for phone simulation)
            MobilePreviewPanel(
                parsedComponent = parsedComponent,
                dispatcher = dispatcher,
                formState = formState,
                isDarkMode = mobilePreviewDarkMode,
                onDarkModeChange = onMobilePreviewDarkModeChange,
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
    actionMessage: String?,
    onActionMessage: (String) -> Unit,
    parsedComponent: Result<com.amnix.sdui.sdui.components.SduiComponent>,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
    mobilePreviewDarkMode: Boolean,
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
        )

        // JSON Editor
        JsonEditorCard(
            jsonInput = jsonInput,
            onJsonChange = onJsonChange,
            selectedExample = selectedExample,
            formState = formState,
            onShowFormData = onActionMessage,
        )

        // Action Message
        actionMessage?.let { message ->
            ActionMessageCard(message)
        }

        // Error Display
        parsedComponent.exceptionOrNull()?.let { error ->
            ErrorCard(error)
        }

        // Rendered UI with mobile preview theming
        parsedComponent.getOrNull()?.let { component ->
            CompactPreviewCard(
                component = component,
                dispatcher = dispatcher,
                formState = formState,
                isDarkMode = mobilePreviewDarkMode,
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
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
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
            // Preview Header with Dark Mode Toggle
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
                            .size(6.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(50.dp),
                            ),
                    )
                    Text(
                        text = "Mobile Preview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }

                // Dark Mode Toggle for Mobile Preview Only
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

            // Mobile Phone Frame
            MobilePhonePreview(
                parsedComponent = parsedComponent,
                dispatcher = dispatcher,
                formState = formState,
                isDarkMode = isDarkMode,
            )
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
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🎨 Rendered UI",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(16.dp))

            RenderSduiComponent(
                component = component,
                dispatcher = dispatcher,
                formState = formState,
            )
        }
    }
}

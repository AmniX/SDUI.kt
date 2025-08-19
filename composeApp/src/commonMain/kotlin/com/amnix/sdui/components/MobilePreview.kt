package com.amnix.sdui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amnix.sdui.sdui.components.SduiComponent
import com.amnix.sdui.sdui.renderer.ActionDispatcher
import com.amnix.sdui.sdui.renderer.RenderSduiComponent

/**
 * A realistic mobile phone preview component that simulates how SDUI content
 * will appear on an actual mobile device.
 */
@Composable
fun MobilePhonePreview(
    parsedComponent: Result<SduiComponent>,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
    modifier: Modifier = Modifier,
) {
    // Mobile phone dimensions (typical Android phone aspect ratio)
    val phoneWidth = 320.dp
    val phoneHeight = 640.dp

    // Phone frame with realistic styling
    ElevatedCard(
        modifier = modifier
            .width(phoneWidth + 24.dp)
            .height(phoneHeight + 48.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFF1A1A1A),
        ),
        shape = RoundedCornerShape(28.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Phone status bar simulation
            PhoneStatusBar(
                phoneWidth = phoneWidth,
            )

            // Phone screen content
            PhoneScreen(
                phoneWidth = phoneWidth,
                parsedComponent = parsedComponent,
                dispatcher = dispatcher,
                formState = formState,
                modifier = Modifier.weight(1f),
            )

            // Phone home indicator
            PhoneHomeIndicator(
                phoneWidth = phoneWidth,
            )
        }
    }
}

@Composable
private fun PhoneStatusBar(phoneWidth: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .width(phoneWidth)
            .height(24.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            ),
    ) {
        // Status bar content
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "9:41",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black,
                fontSize = 12.sp,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Signal, WiFi, Battery icons (simplified)
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(1.dp),
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun PhoneScreen(
    phoneWidth: androidx.compose.ui.unit.Dp,
    parsedComponent: Result<SduiComponent>,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier.width(phoneWidth),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White,
        ),
        shape = RectangleShape,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            parsedComponent.getOrNull()?.let { component ->
                RenderSduiComponent(
                    component = component,
                    dispatcher = dispatcher,
                    state = formState,
                )
            } ?: run {
                // Empty state for mobile preview
                MobilePreviewEmptyState()
            }
        }
    }
}

@Composable
private fun MobilePreviewEmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "📱",
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No Preview",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )
        Text(
            text = "Fix JSON to see mobile preview",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        )
    }
}

@Composable
private fun PhoneHomeIndicator(phoneWidth: Dp) {
    Box(
        modifier = Modifier
            .width(phoneWidth)
            .height(24.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        // Home indicator bar
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(4.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(2.dp),
                ),
        )
    }
}

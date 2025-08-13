package com.amnix.sdui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Minimal logs viewer that matches the playground UI design
 */
@Composable
fun TerminalLogWindow(logs: List<LogEntry>, onClearLogs: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize(), // Changed from fixed height to fill available space
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp), // Reduced from 16dp to save space
        ) {
            // Ultra-compact header - minimal title and small clear button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Logs",
                    style = MaterialTheme.typography.labelMedium, // Smaller text style
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f), // Less prominent
                    fontSize = 12.sp, // Explicit smaller font size
                )

                // Ultra-compact clear button
                Button(
                    onClick = onClearLogs,
                    modifier = Modifier.height(24.dp), // Reduced from 28dp
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 8.dp,
                        vertical = 2.dp,
                    ), // Minimal padding
                ) {
                    Text(
                        text = "Clear",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp, // Smaller font for button text
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp)) // Reduced from 8dp

            // Logs content area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.small,
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.small,
                    )
                    .padding(10.dp), // Reduced from 12dp
            ) {
                if (logs.isEmpty()) {
                    Text(
                        text = "No logs yet. Actions will appear here...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontSize = 11.sp, // Reduced from 12sp
                        style = MaterialTheme.typography.bodySmall,
                    )
                } else {
                    LazyColumn(
                        reverseLayout = true,
                        verticalArrangement = Arrangement.spacedBy(4.dp), // Reduced from 6dp
                    ) {
                        items(logs.reversed()) { logEntry ->
                            LogEntryRow(logEntry = logEntry)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LogEntryRow(logEntry: LogEntry) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        // Timestamp
        Text(
            text = logEntry.timestamp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            fontSize = 10.sp,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(70.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Message with appropriate color
        Text(
            text = logEntry.message,
            color = when (logEntry.type) {
                LogType.SUCCESS -> MaterialTheme.colorScheme.primary
                LogType.WARNING -> Color(0xFFFF9800)
                LogType.ERROR -> MaterialTheme.colorScheme.error
                LogType.ACTION -> MaterialTheme.colorScheme.secondary
                LogType.INFO -> MaterialTheme.colorScheme.onSurface
            },
            fontSize = 12.sp,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
        )
    }
}

/**
 * Data class for log entries
 */
data class LogEntry(
    val message: String,
    val timestamp: String,
    val color: Color = Color(0xFF00FF00),
    val type: LogType = LogType.INFO,
)

enum class LogType {
    INFO,
    SUCCESS,
    WARNING,
    ERROR,
    ACTION,
}

/**
 * Helper function to create a log entry
 */
fun createLogEntry(message: String, type: LogType = LogType.INFO): LogEntry {
    // Simple timestamp using current time
    val timestamp = "12:34:56" // Placeholder timestamp for now

    val color = when (type) {
        LogType.SUCCESS -> Color(0xFF00FF00)
        LogType.WARNING -> Color(0xFFFFFF00)
        LogType.ERROR -> Color(0xFFFF0000)
        LogType.ACTION -> Color(0xFF00FFFF)
        LogType.INFO -> Color(0xFF00FF00)
    }

    return LogEntry(
        message = message,
        timestamp = timestamp,
        color = color,
        type = type,
    )
}

package com.amnix.sdui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Terminal-style log window for displaying action messages and logs
 */
@Composable
fun TerminalLogWindow(
    logs: List<LogEntry>,
    onClearLogs: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        color = Color(0xFF1E1E1E),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Terminal Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Terminal dots
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(12.dp)
                                .height(12.dp)
                                .background(Color(0xFFFF5F57), shape = MaterialTheme.shapes.small)
                        )
                        Box(
                            modifier = Modifier
                                .width(12.dp)
                                .height(12.dp)
                                .background(Color(0xFFFFBD2E), shape = MaterialTheme.shapes.small)
                        )
                        Box(
                            modifier = Modifier
                                .width(12.dp)
                                .height(12.dp)
                                .background(Color(0xFF28CA42), shape = MaterialTheme.shapes.small)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "SDUI Terminal",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Monospace
                    )
                }
                
                Button(
                    onClick = onClearLogs,
                    modifier = Modifier.size(24.dp)
                ) {
                    Text(
                        text = "Clear",
                        color = Color(0xFFCCCCCC),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Terminal Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFF000000))
                    .border(1.dp, Color(0xFF333333))
                    .padding(12.dp)
            ) {
                if (logs.isEmpty()) {
                    Text(
                        text = "No logs yet. Actions will appear here...",
                        color = Color(0xFF888888),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                } else {
                    LazyColumn(
                        reverseLayout = true,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
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
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = logEntry.timestamp,
            color = Color(0xFF888888),
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.width(80.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = logEntry.message,
            color = logEntry.color,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(1f)
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
    val type: LogType = LogType.INFO
)

enum class LogType {
    INFO, SUCCESS, WARNING, ERROR, ACTION
}

/**
 * Helper function to create a log entry
 */
fun createLogEntry(
    message: String,
    type: LogType = LogType.INFO
): LogEntry {
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
        type = type
    )
}

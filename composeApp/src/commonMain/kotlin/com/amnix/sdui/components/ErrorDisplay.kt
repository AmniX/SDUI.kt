package com.amnix.sdui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.amnix.sdui.sdui.SerializationResult

@Composable
fun ErrorDisplay(
    result: SerializationResult<*>,
    modifier: Modifier = Modifier
) {
    when (result) {
        is SerializationResult.Success -> {
            // No errors to display
        }
        is SerializationResult.Error -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFEBEE))
                    .border(1.dp, Color(0xFFE57373))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "❌ Error",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD32F2F)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = result.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFD32F2F)
                    )
                }
                
                result.details?.let { details ->
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Details:",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD32F2F)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = details,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFD32F2F),
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                }
                
                // Show line/column information if available
                if (result.line != null || result.column != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Row {
                        result.line?.let { line ->
                            Text(
                                text = "Line: $line",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFD32F2F)
                            )
                        }
                        if (result.line != null && result.column != null) {
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                        result.column?.let { column ->
                            Text(
                                text = "Column: $column",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFD32F2F)
                            )
                        }
                    }
                }
            }
        }
    }
}

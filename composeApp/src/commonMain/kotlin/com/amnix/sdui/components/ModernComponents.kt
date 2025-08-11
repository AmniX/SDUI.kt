package com.amnix.sdui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amnix.sdui.data.DemoExample
import com.amnix.sdui.data.SduiDemoExamples

/**
 * Modern header section with title, subtitle, and dark mode toggle
 */
@Composable
fun ModernHeaderSection(modifier: Modifier = Modifier) {
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

            
        }

        
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
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            
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
                containerColor = MaterialTheme.colorScheme.surface,
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
 * Code editor-style JSON editor with integrated error display and developer-friendly design
 */
@Composable
fun ModernJsonEditor(
    jsonInput: String,
    onJsonChange: (String) -> Unit,
    selectedExample: DemoExample,
    formState: MutableMap<String, Any?>,
    onShowFormData: (String) -> Unit,
    parseError: Throwable? = null,
    fontSize: Int = 14,
    onFontSizeChange: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        
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
                            color = if (parseError != null) Color(0xFFFF5F57) else Color(0xFF4CAF50),
                            shape = RoundedCornerShape(50.dp),
                        ),
                )
                Text(
                    text = "${selectedExample.resourcePath}.json",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                
                
                if (parseError != null) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFFF5F57).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp),
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                    ) {
                        Text(
                            text = "ERROR",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFFFF5F57),
                        )
                    }
                }
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Font size controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    OutlinedButton(
                        onClick = { 
                            if (fontSize > 8) onFontSizeChange(fontSize - 1)
                        },
                        modifier = Modifier.size(24.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        ),
                        border = androidx.compose.foundation.BorderStroke(
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
                        modifier = Modifier.width(36.dp),
                        textAlign = TextAlign.Center,
                    )
                    
                    OutlinedButton(
                        onClick = { 
                            if (fontSize < 24) onFontSizeChange(fontSize + 1)
                        },
                        modifier = Modifier.size(24.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        ),
                        border = androidx.compose.foundation.BorderStroke(
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
                
                if (parseError != null) {
                    Text(
                        text = "⚠️",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Text(
                    text = "${jsonInput.lines().size} lines",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }
        }

        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFFAFAFA),
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(12.dp),
                ),
        ) {
            Column {
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFF0F0F0),
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
                        color = Color(0xFF6B7280),
                    )
                }

                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (parseError != null) 580.dp else 650.dp),
                ) {
                    val verticalScrollState = rememberScrollState()
                    val horizontalScrollState = rememberScrollState()

                    val totalLines = jsonInput.lines().size
                    val numDigits = maxOf(2, totalLines.toString().length)
                    val gutterMinWidth = 40.dp
                    
                    val gutterWidth = maxOf(
                        gutterMinWidth,
                        (16.dp * numDigits.toFloat()) + 16.dp,
                    )

                    
                    Box(
                        modifier = Modifier
                            .width(gutterWidth)
                            .background(
                                color = Color(0xFFF5F5F5),
                                shape = RoundedCornerShape(bottomStart = 12.dp),
                            )
                            .padding(start = 8.dp, end = 8.dp)
                            .verticalScroll(verticalScrollState)
                    ) {
                        val lineNumbersText = buildString {
                            val numDigits = totalLines.toString().length
                            for (lineIndex in 1..totalLines) {
                                append(lineIndex.toString().padStart(numDigits, ' '))
                                if (lineIndex != totalLines) append('\n')
                            }
                        }
                        Text(
                            text = lineNumbersText,
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = fontSize.sp,
                                lineHeight = (fontSize * 1.4f).sp,
                                lineHeightStyle = LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Center,
                                    trim = LineHeightStyle.Trim.None,
                                ),
                                color = Color(0xFF9CA3AF),
                                textAlign = TextAlign.Right,
                            ),
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .padding(end = 4.dp),
                            softWrap = false,
                            overflow = TextOverflow.Clip,
                        )
                    }

                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(verticalScrollState)
                            .horizontalScroll(horizontalScrollState)
                            .padding(vertical = 12.dp, horizontal = 12.dp),
                    ) {
                        BasicTextField(
                            value = jsonInput,
                            onValueChange = onJsonChange,
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = fontSize.sp,
                                lineHeight = (fontSize * 1.4f).sp,
                                lineHeightStyle = LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Center,
                                    trim = LineHeightStyle.Trim.None,
                                ),
                                color = Color(0xFF1F2937),
                            ),
                            cursorBrush = SolidColor(Color(0xFF3B82F6)),
                            visualTransformation = JsonSyntaxHighlightTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                if (jsonInput.isEmpty()) {
                                    Text(
                                        "{\n  \"type\": \"column\",\n  \"children\": [\n    Add your JSON here...\n  ]\n}",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = fontSize.sp,
                                        lineHeight = (fontSize * 1.4f).sp,
                                        color = Color(0xFF9CA3AF),
                                    )
                                }
                                innerTextField()
                            },
                        )
                    }
                }
                
                
                if (parseError != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFFF5F57).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                            )
                            .padding(12.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = "⚠️",
                                style = MaterialTheme.typography.bodySmall,
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Text(
                                    text = "JSON Parse Error",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFFF5F57),
                                )
                                Text(
                                    text = parseError.message ?: "Invalid JSON syntax",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFFF5F57).copy(alpha = 0.8f),
                                )
                            }
                        }
                    }
                }
            }
        }

        
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

// JSON syntax highlighting using VisualTransformation
private fun JsonSyntaxHighlightTransformation(): VisualTransformation {
    val keyColor = Color(0xFF1E88E5)
    val stringColor = Color(0xFF2E7D32)
    val numberColor = Color(0xFF6A1B9A)
    val literalColor = Color(0xFFEF6C00)
    val punctuationColor = Color(0xFF9E9E9E)

    return VisualTransformation { originalText ->
        val text = originalText.text
        if (text.isEmpty()) {
            return@VisualTransformation TransformedText(originalText, OffsetMapping.Identity)
        }

        val builder = AnnotatedString.Builder()
        builder.append(text)

        var i = 0
        val n = text.length
        var inString = false
        var stringStart = -1
        var escape = false

        fun isWordBoundary(idx: Int): Boolean {
            return idx < 0 || idx >= n || !text[idx].isLetter()
        }

        while (i < n) {
            val c = text[i]
            if (inString) {
                if (escape) {
                    escape = false
                } else if (c == '\\') {
                    escape = true
                } else if (c == '"') {
                    
                    val end = i + 1
                    
                    var j = end
                    while (j < n && text[j].isWhitespace()) j++
                    val isKey = j < n && text[j] == ':'
                    builder.addStyle(
                        SpanStyle(color = if (isKey) keyColor else stringColor),
                        stringStart,
                        end,
                    )
                    inString = false
                }
                i++
                continue
            } else {
                when (c) {
                    '"' -> {
                        inString = true
                        stringStart = i
                        i++
                        continue
                    }
                    '{', '}', '[', ']', ':', ',' -> {
                        builder.addStyle(SpanStyle(color = punctuationColor), i, i + 1)
                        i++
                        continue
                    }
                    '-', in '0'..'9' -> {
                        
                        val start = i
                        var k = i
                        var hasDigits = false
                        if (text[k] == '-') k++
                        while (k < n && text[k].isDigit()) { k++; hasDigits = true }
                        if (k < n && text[k] == '.') {
                            k++
                            while (k < n && text[k].isDigit()) { k++; hasDigits = true }
                        }
                        if (hasDigits) {
                            
                            if (k < n && (text[k] == 'e' || text[k] == 'E')) {
                                k++
                                if (k < n && (text[k] == '+' || text[k] == '-')) k++
                                while (k < n && text[k].isDigit()) k++
                            }
                            builder.addStyle(SpanStyle(color = numberColor), start, k)
                            i = k
                            continue
                        }
                    }
                    't' -> {
                        
                        if (i + 3 < n && text.substring(i, i + 4) == "true" && isWordBoundary(i - 1) && isWordBoundary(i + 4)) {
                            builder.addStyle(SpanStyle(color = literalColor), i, i + 4)
                            i += 4
                            continue
                        }
                    }
                    'f' -> {
                        
                        if (i + 4 < n && text.substring(i, i + 5) == "false" && isWordBoundary(i - 1) && isWordBoundary(i + 5)) {
                            builder.addStyle(SpanStyle(color = literalColor), i, i + 5)
                            i += 5
                            continue
                        }
                    }
                    'n' -> {
                        
                        if (i + 3 < n && text.substring(i, i + 4) == "null" && isWordBoundary(i - 1) && isWordBoundary(i + 4)) {
                            builder.addStyle(SpanStyle(color = literalColor), i, i + 4)
                            i += 4
                            continue
                        }
                    }
                }
                i++
            }
        }

        TransformedText(builder.toAnnotatedString(), OffsetMapping.Identity)
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

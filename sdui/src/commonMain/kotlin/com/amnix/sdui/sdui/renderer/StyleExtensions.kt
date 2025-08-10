package com.amnix.sdui.sdui.renderer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.amnix.sdui.sdui.model.Style

/**
 * Apply SDUI style to a Compose modifier
 */
fun Modifier.applyStyle(style: Style?): Modifier {
    if (style == null) return this

    var modifier = this

    // Apply padding
    style.padding?.let { padding ->
        modifier =
            modifier.padding(
                start = padding.start?.dp ?: padding.horizontal?.dp ?: padding.all?.dp ?: 0.dp,
                end = padding.end?.dp ?: padding.horizontal?.dp ?: padding.all?.dp ?: 0.dp,
                top = padding.top?.dp ?: padding.vertical?.dp ?: padding.all?.dp ?: 0.dp,
                bottom = padding.bottom?.dp ?: padding.vertical?.dp ?: padding.all?.dp ?: 0.dp,
            )
    }

    // Apply margin (using padding for margin effect)
    style.margin?.let { margin ->
        modifier =
            modifier.padding(
                start = margin.start?.dp ?: margin.horizontal?.dp ?: margin.all?.dp ?: 0.dp,
                end = margin.end?.dp ?: margin.horizontal?.dp ?: margin.all?.dp ?: 0.dp,
                top = margin.top?.dp ?: margin.vertical?.dp ?: margin.all?.dp ?: 0.dp,
                bottom = margin.bottom?.dp ?: margin.vertical?.dp ?: margin.all?.dp ?: 0.dp,
            )
    }

    // Apply background color
    style.backgroundColor?.let { bgColor ->
        modifier = modifier.background(parseColor(bgColor))
    }

    // Apply width and height constraints
    style.width?.let { width ->
        when {
            width == "100%" -> modifier = modifier.fillMaxWidth()
            width.endsWith("%") -> {
                val percentage = width.removeSuffix("%").toFloatOrNull() ?: 100f
                modifier = modifier.width((percentage / 100f).dp)
            }
            width.endsWith("dp") -> {
                val dpValue = width.removeSuffix("dp").toFloatOrNull() ?: 0f
                modifier = modifier.width(dpValue.dp)
            }
            else -> {
                val dpValue = width.toFloatOrNull() ?: 0f
                modifier = modifier.width(dpValue.dp)
            }
        }
    }

    style.height?.let { height ->
        when {
            height == "100%" -> modifier = modifier.fillMaxHeight()
            height.endsWith("%") -> {
                val percentage = height.removeSuffix("%").toFloatOrNull() ?: 100f
                modifier = modifier.height((percentage / 100f).dp)
            }
            height.endsWith("dp") -> {
                val dpValue = height.removeSuffix("dp").toFloatOrNull() ?: 0f
                modifier = modifier.height(dpValue.dp)
            }
            else -> {
                val dpValue = height.toFloatOrNull() ?: 0f
                modifier = modifier.height(dpValue.dp)
            }
        }
    }

    // Apply min/max constraints
    style.minWidth?.let { minWidth ->
        val dpValue =
            when {
                minWidth.endsWith("dp") -> minWidth.removeSuffix("dp").toFloatOrNull() ?: 0f
                else -> minWidth.toFloatOrNull() ?: 0f
            }
        modifier = modifier.defaultMinSize(minWidth = dpValue.dp)
    }

    style.minHeight?.let { minHeight ->
        val dpValue =
            when {
                minHeight.endsWith("dp") -> minHeight.removeSuffix("dp").toFloatOrNull() ?: 0f
                else -> minHeight.toFloatOrNull() ?: 0f
            }
        modifier = modifier.defaultMinSize(minHeight = dpValue.dp)
    }

    style.maxWidth?.let { maxWidth ->
        val dpValue =
            when {
                maxWidth.endsWith("dp") -> maxWidth.removeSuffix("dp").toFloatOrNull() ?: 0f
                else -> maxWidth.toFloatOrNull() ?: 0f
            }
        if (dpValue > 0) {
            modifier = modifier.width(dpValue.dp)
        }
    }

    style.maxHeight?.let { maxHeight ->
        val dpValue =
            when {
                maxHeight.endsWith("dp") -> maxHeight.removeSuffix("dp").toFloatOrNull() ?: 0f
                else -> maxHeight.toFloatOrNull() ?: 0f
            }
        if (dpValue > 0) {
            modifier = modifier.height(dpValue.dp)
        }
    }

    // Apply corner radius
    style.cornerRadius?.let { radius ->
        modifier =
            modifier.background(
                color = Color.Transparent,
                shape = RoundedCornerShape(radius.dp),
            )
    }

    // Apply border
    style.borderWidth?.let { borderWidth ->
        style.borderColor?.let { borderColor ->
            modifier =
                modifier.border(
                    width = borderWidth.dp,
                    color = parseColor(borderColor),
                    shape = RoundedCornerShape(style.cornerRadius?.dp ?: 0.dp),
                )
        }
    }

    // Apply shadow
    style.shadowRadius?.let { shadowRadius ->
        style.shadowColor?.let { shadowColor ->
            modifier =
                modifier.shadow(
                    elevation = shadowRadius.dp,
                    shape = RoundedCornerShape(style.cornerRadius?.dp ?: 0.dp),
                    ambientColor = parseColor(shadowColor),
                    spotColor = parseColor(shadowColor),
                )
        }
    }

    // Apply opacity
    style.opacity?.let { opacity ->
        modifier = modifier.alpha(opacity)
    }

    // Apply rotation
    style.rotation?.let { rotation ->
        modifier = modifier.rotate(rotation)
    }

    // Apply scale
    style.scale?.let { scale ->
        modifier = modifier.scale(scale)
    }

    // Apply z-index
    style.zIndex?.let { zIndex ->
        modifier = modifier.zIndex(zIndex.toFloat())
    }

    return modifier
}

/**
 * Parse color string to Compose Color
 */
fun parseColor(colorString: String): Color = try {
    when {
        colorString.startsWith("#") -> {
            val hex = colorString.removePrefix("#")
            when (hex.length) {
                3 ->
                    Color(
                        red = hex[0].toString().repeat(2).toInt(16) / 255f,
                        green = hex[1].toString().repeat(2).toInt(16) / 255f,
                        blue = hex[2].toString().repeat(2).toInt(16) / 255f,
                    )
                6 ->
                    Color(
                        red = hex.substring(0, 2).toInt(16) / 255f,
                        green = hex.substring(2, 4).toInt(16) / 255f,
                        blue = hex.substring(4, 6).toInt(16) / 255f,
                    )
                8 ->
                    Color(
                        red = hex.substring(2, 4).toInt(16) / 255f,
                        green = hex.substring(4, 6).toInt(16) / 255f,
                        blue = hex.substring(6, 8).toInt(16) / 255f,
                        alpha = hex.substring(0, 2).toInt(16) / 255f,
                    )
                else -> Color.Black
            }
        }
        else -> Color.Black
    }
} catch (e: Exception) {
    Color.Black
}

/**
 * Get horizontal alignment from string
 */
fun parseHorizontalAlignment(alignment: String?): Alignment.Horizontal = when (alignment?.lowercase()) {
    "start", "left" -> Alignment.Start
    "center" -> Alignment.CenterHorizontally
    "end", "right" -> Alignment.End
    else -> Alignment.CenterHorizontally
}

/**
 * Get vertical alignment from string
 */
fun parseVerticalAlignment(alignment: String?): Alignment.Vertical = when (alignment?.lowercase()) {
    "top" -> Alignment.Top
    "center" -> Alignment.CenterVertically
    "bottom" -> Alignment.Bottom
    else -> Alignment.CenterVertically
}

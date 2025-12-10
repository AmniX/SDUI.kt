package com.amnix.sdui.sdui

import com.amnix.sdui.sdui.components.SduiComponent
import com.amnix.sdui.sdui.validation.SduiValidator
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * Result class for serialization operations
 */
sealed class SerializationResult<out T> {
    data class Success<T>(val data: T) : SerializationResult<T>()
    data class Error(val message: String, val details: String? = null, val line: Int? = null, val column: Int? = null) :
        SerializationResult<Nothing>()
}

/**
 * Enhanced SDUI Serializer with comprehensive error handling and validation
 */
object SduiSerializer {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
        prettyPrint = true
    }

    /**
     * Deserialize JSON with comprehensive error handling and validation
     */
    fun deserializeWithValidation(jsonString: String): SerializationResult<SduiComponent> = try {
        // First, try to parse the JSON
        val component = json.decodeFromString(SduiComponent.serializer(), jsonString)

        // If parsing succeeds, run validation
        val validationResult = SduiValidator.validate(component)
        if (validationResult.isValid) {
            SerializationResult.Success(component)
        } else {
            SerializationResult.Error(
                message = "Validation failed",
                details = validationResult.issues.joinToString("\n") { it.message },
            )
        }
    } catch (e: SerializationException) {
        // Handle serialization-specific errors
        val errorDetails = extractSerializationErrorDetails(e, jsonString)
        SerializationResult.Error(
            message = "JSON parsing failed: ${e.message}",
            details = errorDetails,
            line = extractLineNumber(e, jsonString),
            column = extractColumnNumber(e, jsonString),
        )
    } catch (e: Exception) {
        // Handle other unexpected errors
        SerializationResult.Error(
            message = "Unexpected error: ${e.message}",
            details = e.stackTraceToString(),
        )
    }

    /**
     * Extract detailed error information from serialization exceptions
     */
    private fun extractSerializationErrorDetails(e: SerializationException, jsonString: String): String = buildString {
        appendLine("Error Type: ${e::class.simpleName}")
        appendLine("Message: ${e.message}")

        // Try to provide context around the error
        val lines = jsonString.lines()
        if (lines.isNotEmpty()) {
            appendLine("JSON Context:")
            lines.forEachIndexed { index, line ->
                appendLine("${index + 1}: $line")
            }
        }
    }

    /**
     * Extract line number from error (approximate)
     */
    private fun extractLineNumber(e: SerializationException, jsonString: String): Int? {
        // This is a simplified approach - in a real implementation you might want to use a more sophisticated parser
        return try {
            val message = e.message ?: return null
            if (message.contains("line")) {
                val lineMatch = Regex("line (\\d+)").find(message)
                lineMatch?.groupValues?.get(1)?.toInt()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Extract column number from error (approximate)
     */
    private fun extractColumnNumber(e: SerializationException, jsonString: String): Int? {
        return try {
            val message = e.message ?: return null
            if (message.contains("column")) {
                val columnMatch = Regex("column (\\d+)").find(message)
                columnMatch?.groupValues?.get(1)?.toInt()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

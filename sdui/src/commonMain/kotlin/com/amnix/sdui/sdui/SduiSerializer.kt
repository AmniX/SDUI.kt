package com.amnix.sdui.sdui

import com.amnix.sdui.sdui.components.SduiComponent
import com.amnix.sdui.sdui.validation.SduiValidator
import kotlinx.serialization.json.Json
import kotlinx.serialization.SerializationException

/**
 * Result class for serialization operations
 */
sealed class SerializationResult<out T> {
    data class Success<T>(val data: T) : SerializationResult<T>()
    data class Error(val message: String, val details: String? = null, val line: Int? = null, val column: Int? = null) : SerializationResult<Nothing>()
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
    fun deserializeWithValidation(jsonString: String): SerializationResult<SduiComponent> {
        return try {
            // First, try to parse the JSON
            val component = json.decodeFromString(SduiComponent.serializer(), jsonString)
            
            // If parsing succeeds, run validation
            val validationResult = SduiValidator.validate(component)
            if (validationResult.isValid) {
                SerializationResult.Success(component)
            } else {
                SerializationResult.Error(
                    message = "Validation failed",
                    details = validationResult.errors.joinToString("\n") { it.message }
                )
            }
        } catch (e: SerializationException) {
            // Handle serialization-specific errors
            val errorDetails = extractSerializationErrorDetails(e, jsonString)
            SerializationResult.Error(
                message = "JSON parsing failed: ${e.message}",
                details = errorDetails,
                line = extractLineNumber(e, jsonString),
                column = extractColumnNumber(e, jsonString)
            )
        } catch (e: Exception) {
            // Handle other unexpected errors
            SerializationResult.Error(
                message = "Unexpected error: ${e.message}",
                details = e.stackTraceToString()
            )
        }
    }

    /**
     * Legacy deserialize method for backward compatibility
     */
    fun deserialize(jsonString: String): SduiComponent = 
        deserializeWithValidation(jsonString).let { result ->
            when (result) {
                is SerializationResult.Success -> result.data
                is SerializationResult.Error -> throw IllegalArgumentException("Deserialization failed: ${result.message}")
            }
        }

    /**
     * Serialize component to JSON
     */
    fun serialize(component: SduiComponent): String = 
        json.encodeToString(SduiComponent.serializer(), component)

    /**
     * Serialize list of components
     */
    fun serializeList(components: List<SduiComponent>): String =
        json.encodeToString(kotlinx.serialization.builtins.ListSerializer(SduiComponent.serializer()), components)

    /**
     * Deserialize list with validation
     */
    fun deserializeListWithValidation(jsonString: String): SerializationResult<List<SduiComponent>> {
        return try {
            val components = json.decodeFromString(kotlinx.serialization.builtins.ListSerializer(SduiComponent.serializer()), jsonString)
            
            // Validate each component
            val validationErrors = mutableListOf<String>()
            components.forEachIndexed { index, component ->
                val result = SduiValidator.validate(component)
                if (!result.isValid) {
                    validationErrors.add("Component $index: ${result.errors.joinToString(", ") { it.message }}")
                }
            }
            
            if (validationErrors.isEmpty()) {
                SerializationResult.Success(components)
            } else {
                SerializationResult.Error(
                    message = "Validation failed for list components",
                    details = validationErrors.joinToString("\n")
                )
            }
        } catch (e: Exception) {
            SerializationResult.Error(
                message = "List deserialization failed: ${e.message}",
                details = e.stackTraceToString()
            )
        }
    }

    /**
     * Extract detailed error information from serialization exceptions
     */
    private fun extractSerializationErrorDetails(e: SerializationException, jsonString: String): String {
        return buildString {
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
            } else null
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
            } else null
        } catch (e: Exception) {
            null
        }
    }
}

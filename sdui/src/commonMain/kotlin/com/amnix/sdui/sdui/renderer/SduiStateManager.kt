package com.amnix.sdui.sdui.renderer

import kotlin.time.ExperimentalTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalTime::class)
/**
 * Represents a state value that can be of any type
 */
sealed class SduiStateValue {
    data class StringValue(val value: String) : SduiStateValue()

    data class LongValue(val value: Int) : SduiStateValue()

    data class BooleanValue(val value: Boolean) : SduiStateValue()

    data class DoubleValue(val value: Double) : SduiStateValue()

    data class ListValue(val value: List<String>) : SduiStateValue()

    data class MapValue(val value: Map<String, String>) : SduiStateValue()

    data class JsonValue(val value: String) : SduiStateValue()

    companion object {
        fun fromString(value: String): SduiStateValue = when {
            value.equals("true", ignoreCase = true) -> BooleanValue(true)
            value.equals("false", ignoreCase = true) -> BooleanValue(false)
            value.toIntOrNull() != null -> LongValue(value.toInt())
            value.toDoubleOrNull() != null -> DoubleValue(value.toDouble())
            value.startsWith("[") && value.endsWith("]") -> {
                // Simple list parsing (comma-separated)
                val items =
                    value
                        .removeSurrounding("[", "]")
                        .split(",")
                        .map { it.trim().removeSurrounding("\"") }
                        .filter { it.isNotEmpty() }
                ListValue(items)
            }
            value.startsWith("{") && value.endsWith("}") -> {
                // Simple map parsing (key=value,key=value)
                val map =
                    value
                        .removeSurrounding("{", "}")
                        .split(",")
                        .mapNotNull { pair ->
                            val keyValue = pair.trim().split("=", limit = 2)
                            if (keyValue.size == 2) {
                                keyValue[0].trim().removeSurrounding("\"") to
                                    keyValue[1].trim().removeSurrounding("\"")
                            } else {
                                null
                            }
                        }.toMap()
                MapValue(map)
            }
            else -> StringValue(value)
        }
    }
}

@OptIn(ExperimentalTime::class)
/**
 * State change event for notifying listeners
 */
data class StateChangeEvent(
    val key: String,
    val oldValue: SduiStateValue?,
    val newValue: SduiStateValue,
    val timestamp: Long =
        kotlin.time.Clock.System
            .now()
            .toEpochMilliseconds(),
)

/**
 * State change listener interface
 */
interface StateChangeListener {
    fun onStateChanged(event: StateChangeEvent)
}

/**
 * Central state manager for SDUI applications
 */
@OptIn(ExperimentalTime::class)
class SduiStateManager {
    private val _state = MutableStateFlow<Map<String, SduiStateValue>>(emptyMap())
    val state: StateFlow<Map<String, SduiStateValue>> = _state.asStateFlow()

    private val listeners = mutableListOf<StateChangeListener>()

    /**
     * Get a state value by key
     */
    fun getState(key: String): SduiStateValue? = _state.value[key]

    /**
     * Get a state value as string
     */
    fun getStringState(key: String): String? = when (val value = getState(key)) {
        is SduiStateValue.StringValue -> value.value
        is SduiStateValue.LongValue -> value.value.toString()
        is SduiStateValue.BooleanValue -> value.value.toString()
        is SduiStateValue.DoubleValue -> value.value.toString()
        is SduiStateValue.ListValue -> value.value.joinToString(",")
        is SduiStateValue.MapValue -> value.value.entries.joinToString(",") { "${it.key}=${it.value}" }
        is SduiStateValue.JsonValue -> value.value
        null -> null
    }

    /**
     * Get a state value as boolean
     */
    fun getBooleanState(key: String): Boolean? = when (val value = getState(key)) {
        is SduiStateValue.BooleanValue -> value.value
        is SduiStateValue.StringValue -> value.value.equals("true", ignoreCase = true)
        is SduiStateValue.LongValue -> value.value != 0
        is SduiStateValue.DoubleValue -> value.value != 0.0
        is SduiStateValue.ListValue -> value.value.isNotEmpty()
        is SduiStateValue.MapValue -> value.value.isNotEmpty()
        is SduiStateValue.JsonValue -> value.value.isNotEmpty()
        null -> null
    }

    /**
     * Get a state value as integer
     */
    fun getLongState(key: String): Long? = when (val value = getState(key)) {
        is SduiStateValue.LongValue -> value.value.toLong()
        is SduiStateValue.StringValue -> value.value.toLongOrNull()
        is SduiStateValue.DoubleValue -> value.value.toLong()
        is SduiStateValue.BooleanValue -> if (value.value) 1L else 0
        is SduiStateValue.ListValue -> value.value.size.toLong()
        is SduiStateValue.MapValue -> value.value.size.toLong()
        is SduiStateValue.JsonValue -> value.value.length.toLong()
        null -> null
    }

    /**
     * Get a state value as double
     */
    fun getDoubleState(key: String): Double? = when (val value = getState(key)) {
        is SduiStateValue.DoubleValue -> value.value
        is SduiStateValue.LongValue -> value.value.toDouble()
        is SduiStateValue.StringValue -> value.value.toDoubleOrNull()
        is SduiStateValue.BooleanValue -> if (value.value) 1.0 else 0.0
        is SduiStateValue.ListValue -> value.value.size.toDouble()
        is SduiStateValue.MapValue -> value.value.size.toDouble()
        is SduiStateValue.JsonValue -> value.value.length.toDouble()
        null -> null
    }

    /**
     * Get a state value as list
     */
    fun getListState(key: String): List<String>? = when (val value = getState(key)) {
        is SduiStateValue.ListValue -> value.value
        is SduiStateValue.StringValue -> value.value.split(",").map { it.trim() }
        is SduiStateValue.MapValue -> value.value.entries.map { "${it.key}=${it.value}" }
        is SduiStateValue.JsonValue -> listOf(value.value)
        is SduiStateValue.BooleanValue -> listOf(value.value.toString())
        is SduiStateValue.LongValue -> listOf(value.value.toString())
        is SduiStateValue.DoubleValue -> listOf(value.value.toString())
        null -> null
    }

    /**
     * Get a state value as map
     */
    fun getMapState(key: String): Map<String, String>? = when (val value = getState(key)) {
        is SduiStateValue.MapValue -> value.value
        is SduiStateValue.StringValue -> {
            // Try to parse as key=value pairs
            value.value
                .split(",")
                .mapNotNull { pair ->
                    val keyValue = pair.trim().split("=", limit = 2)
                    if (keyValue.size == 2) {
                        keyValue[0] to keyValue[1]
                    } else {
                        null
                    }
                }.toMap()
        }
        is SduiStateValue.JsonValue -> mapOf("json" to value.value)
        is SduiStateValue.BooleanValue -> mapOf("value" to value.value.toString())
        is SduiStateValue.LongValue -> mapOf("value" to value.value.toString())
        is SduiStateValue.DoubleValue -> mapOf("value" to value.value.toString())
        is SduiStateValue.ListValue -> value.value.associateWith { it }
        null -> null
    }

    /**
     * Update state with a new value
     */
    fun updateState(key: String, value: SduiStateValue) {
        val oldValue = _state.value[key]
        _state.update { currentState ->
            currentState + (key to value)
        }

        // Notify listeners
        val event = StateChangeEvent(key, oldValue, value)
        listeners.forEach { listener ->
            try {
                listener.onStateChanged(event)
            } catch (e: Exception) {
                println("Error in state change listener: ${e.message}")
            }
        }
    }

    /**
     * Update state with a string value (auto-converted to appropriate type)
     */
    fun updateState(key: String, value: String) {
        updateState(key, SduiStateValue.fromString(value))
    }

    /**
     * Update state with a boolean value
     */
    fun updateState(key: String, value: Boolean) {
        updateState(key, SduiStateValue.BooleanValue(value))
    }

    /**
     * Update state with an integer value
     */
    fun updateState(key: String, value: Int) {
        updateState(key, SduiStateValue.LongValue(value))
    }

    /**
     * Update state with a double value
     */
    fun updateState(key: String, value: Double) {
        updateState(key, SduiStateValue.DoubleValue(value))
    }

    /**
     * Update state with a list value
     */
    fun updateState(key: String, value: List<String>) {
        updateState(key, SduiStateValue.ListValue(value))
    }

    /**
     * Update state with a map value
     */
    fun updateState(key: String, value: Map<String, String>) {
        updateState(key, SduiStateValue.MapValue(value))
    }

    /**
     * Update state with JSON value
     */
    fun updateStateJson(key: String, jsonValue: String) {
        updateState(key, SduiStateValue.JsonValue(jsonValue))
    }

    /**
     * Remove a state key
     */
    fun removeState(key: String) {
        val oldValue = _state.value[key]
        _state.update { currentState ->
            currentState - key
        }

        // Notify listeners of removal
        val event = StateChangeEvent(key, oldValue, SduiStateValue.StringValue(""))
        listeners.forEach { listener ->
            try {
                listener.onStateChanged(event)
            } catch (e: Exception) {
                println("Error in state change listener: ${e.message}")
            }
        }
    }

    /**
     * Clear all state
     */
    fun clearState() {
        val oldState = _state.value
        _state.value = emptyMap()

        // Notify listeners of all removals
        oldState.forEach { (key, value) ->
            val event = StateChangeEvent(key, value, SduiStateValue.StringValue(""))
            listeners.forEach { listener ->
                try {
                    listener.onStateChanged(event)
                } catch (e: Exception) {
                    println("Error in state change listener: ${e.message}")
                }
            }
        }
    }

    /**
     * Add a state change listener
     */
    fun addListener(listener: StateChangeListener) {
        listeners.add(listener)
    }

    /**
     * Remove a state change listener
     */
    fun removeListener(listener: StateChangeListener) {
        listeners.remove(listener)
    }

    /**
     * Get all state keys
     */
    fun getStateKeys(): Set<String> = _state.value.keys

    /**
     * Check if a state key exists
     */
    fun hasState(key: String): Boolean = _state.value.containsKey(key)

    /**
     * Get state as a map of string values
     */
    fun getStateAsStringMap(): Map<String, String> = _state.value.mapValues { (_, value) ->
        when (value) {
            is SduiStateValue.StringValue -> value.value
            is SduiStateValue.LongValue -> value.value.toString()
            is SduiStateValue.BooleanValue -> value.value.toString()
            is SduiStateValue.DoubleValue -> value.value.toString()
            is SduiStateValue.ListValue -> value.value.joinToString(",")
            is SduiStateValue.MapValue -> value.value.entries.joinToString(",") { "${it.key}=${it.value}" }
            is SduiStateValue.JsonValue -> value.value
        }
    }
}

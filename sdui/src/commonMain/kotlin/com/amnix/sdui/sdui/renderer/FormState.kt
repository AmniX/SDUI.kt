package com.amnix.sdui.sdui.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateMapOf

/**
 * Form state utilities for SDUI renderer
 */
object FormState {
    
    /**
     * Remember a form state map
     */
    @Composable
    fun rememberFormState(): MutableMap<String, Any?> {
        return remember { mutableStateMapOf() }
    }
    
    /**
     * Remember a single form value
     */
    @Composable
    fun <T> rememberFormValue(
        key: String,
        initialValue: T,
        formState: MutableMap<String, Any?>
    ): Pair<T, (T) -> Unit> {
        var value by remember { mutableStateOf(initialValue) }
        
        val setValue = { newValue: T ->
            value = newValue
            formState[key] = newValue
        }
        
        return value to setValue
    }
    
    /**
     * Get a form value with type safety
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getFormValue(key: String, formState: Map<String, Any?>, defaultValue: T): T {
        return formState[key] as? T ?: defaultValue
    }
    
    /**
     * Set a form value
     */
    fun setFormValue(key: String, value: Any?, formState: MutableMap<String, Any?>) {
        formState[key] = value
    }
    
    /**
     * Clear a form value
     */
    fun clearFormValue(key: String, formState: MutableMap<String, Any?>) {
        formState.remove(key)
    }
    
    /**
     * Clear all form values
     */
    fun clearAllFormValues(formState: MutableMap<String, Any?>) {
        formState.clear()
    }
    
    /**
     * Check if form has any values
     */
    fun hasFormValues(formState: Map<String, Any?>): Boolean {
        return formState.isNotEmpty()
    }
    
    /**
     * Get form values as a map
     */
    fun getFormValues(formState: Map<String, Any?>): Map<String, Any?> {
        return formState.toMap()
    }
} 
package com.amnix.sdui.sdui.renderer

import com.amnix.sdui.sdui.model.SduiAction

/**
 * Interface for dispatching SDUI actions
 */
interface ActionDispatcher {
    fun dispatch(action: SduiAction)
}

/**
 * Default no-op action dispatcher for preview/testing
 */
class EmptyActionDispatcher : ActionDispatcher {
    override fun dispatch(action: SduiAction) {
        // No-op implementation for preview/testing
    }
}

/**
 * Logging action dispatcher that prints actions to console
 */
class LoggingActionDispatcher : ActionDispatcher {
    override fun dispatch(action: SduiAction) {
        println("SDUI Action dispatched: $action")
    }
} 
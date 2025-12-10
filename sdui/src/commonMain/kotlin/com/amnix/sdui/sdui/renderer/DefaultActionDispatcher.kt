package com.amnix.sdui.sdui.renderer

import com.amnix.sdui.sdui.model.SduiAction
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * API response data class for handling HTTP responses
 */
data class ApiResponse(
    val statusCode: Int,
    val body: String?,
    val headers: Map<String, String>? = null,
    val isSuccess: Boolean = statusCode in 200..299,
)

/**
 * Default implementation of ActionDispatcher that handles real-world behaviors
 * like navigation, form submission, API calls, and showing dialogs.
 */
@OptIn(ExperimentalTime::class)
class DefaultActionDispatcher(
    private val onNavigate: (String, Map<String, String>?) -> Unit,
    private val onShowDialog: (title: String, message: String, type: String) -> Unit,
    private val submitForm: suspend (formData: Map<String, Any?>) -> Result<Unit>,
    private val callApi: suspend (
        url: String,
        method: String,
        headers: Map<String, String>?,
        body: Map<String, Any?>?,
    ) -> ApiResponse,
    private val customHandlers: Map<String, (Map<String, String>?) -> Unit> = emptyMap(),
    private val coroutineScope: CoroutineScope? = null,
    private val stateManager: SduiStateManager? = null,
) : ActionDispatcher {
    override fun dispatch(action: SduiAction) {
        when (action) {
            is SduiAction.Navigate -> handleNavigate(action)
            is SduiAction.ApiCall -> handleApiCall(action)
            is SduiAction.ShowDialog -> handleShowDialog(action)
            is SduiAction.UpdateState -> handleUpdateState(action)
            is SduiAction.Reset -> handleReset(action)
            is SduiAction.Custom -> handleCustomAction(action)
        }
    }

    /**
     * Handle navigation actions by calling the provided onNavigate callback
     */
    private fun handleNavigate(action: SduiAction.Navigate) {
        try {
            val route = action.route ?: "home"
            onNavigate(route, action.payload)
        } catch (e: Exception) {
            println("Navigation failed for route '${action.route ?: "home"}': ${e.message}")
        }
    }

    /**
     * Handle API call actions by executing the request asynchronously
     */
    private fun handleApiCall(action: SduiAction.ApiCall) {
        val scope = coroutineScope ?: return

        scope.launch {
            try {
                // Update loading state if state manager is available
                stateManager?.updateState("isLoading", true)

                val response = callApi(action.url, action.method, action.headers, action.body)

                // Update loading state
                stateManager?.updateState("isLoading", false)

                if (response.isSuccess) {
                    // Update success state
                    stateManager?.updateState("lastApiResponse", response.body ?: "")
                    stateManager?.updateState("apiStatus", "success")

                    // Handle success action
                    action.onSuccess?.let { successAction ->
                        dispatchActionFromString(successAction)
                    }
                } else {
                    // Update error state
                    stateManager?.updateState("lastApiError", response.body ?: "Unknown error")
                    stateManager?.updateState("apiStatus", "error")

                    // Handle error action
                    action.onError?.let { errorAction ->
                        dispatchActionFromString(errorAction)
                    }
                }
            } catch (e: Exception) {
                // Update error state
                stateManager?.updateState("isLoading", false)
                stateManager?.updateState("lastApiError", e.message ?: "Unknown error")
                stateManager?.updateState("apiStatus", "error")

                println("API call failed for URL '${action.url}': ${e.message}")
                // Handle error action on exception
                action.onError?.let { errorAction ->
                    dispatchActionFromString(errorAction)
                }
            }
        }
    }

    /**
     * Handle show dialog actions by calling the provided onShowDialog callback
     */
    private fun handleShowDialog(action: SduiAction.ShowDialog) {
        try {
            // Update dialog state as JSON string
            val dialogData = """{"title":"${action.title}","message":"${action.message}","type":"${action.type}"}"""
            stateManager?.updateStateJson("currentDialog", dialogData)

            onShowDialog(action.title, action.message, action.type)
        } catch (e: Exception) {
            println("Show dialog failed: ${e.message}")
        }
    }

    /**
     * Handle update state actions with actual state management
     */
    private fun handleUpdateState(action: SduiAction.UpdateState) {
        try {
            stateManager?.let { manager ->
                // Parse the value to determine its type
                val stateValue = SduiStateValue.fromString(action.value)
                manager.updateState(action.key, stateValue)

                println("State updated: ${action.key} = ${action.value}")
            } ?: run {
                // Fallback to logging if no state manager is provided
                println("State update (no manager): ${action.key} = ${action.value}")
            }
        } catch (e: Exception) {
            println("State update failed for key '${action.key}': ${e.message}")
        }
    }

    /**
     * Handle reset actions by clearing form state
     */
    private fun handleReset(action: SduiAction.Reset) {
        try {
            // Clear form state
            stateManager?.clearState()
            println("Form state reset")
        } catch (e: Exception) {
            println("Reset action failed: ${e.message}")
        }
    }

    /**
     * Handle custom actions by routing to registered handlers
     */
    private fun handleCustomAction(action: SduiAction.Custom) {
        val handler = customHandlers[action.action]
        if (handler != null) {
            try {
                handler(action.data)

                // Update custom action state
                stateManager?.updateState("lastCustomAction", action.action)
                stateManager?.updateState("customActionData", action.data?.toString() ?: "")
            } catch (e: Exception) {
                println("Custom action '${action.action}' failed: ${e.message}")
                stateManager?.updateState("lastCustomActionError", e.message ?: "Unknown error")
            }
        } else {
            println("No handler registered for custom action: ${action.action}")
            stateManager?.updateState("lastCustomActionError", "No handler for: ${action.action}")
        }
    }

    /**
     * Handle form submission by calling the provided submitForm callback
     */
    fun submitFormData(formData: Map<String, Any?>) {
        val scope = coroutineScope ?: return

        scope.launch {
            try {
                // Update form submission state
                stateManager?.updateState("isSubmitting", true)
                stateManager?.updateState("formData", formData.toString())

                val result = submitForm(formData)

                if (result.isSuccess) {
                    // Update success state
                    stateManager?.updateState("isSubmitting", false)
                    stateManager?.updateState("formSubmissionStatus", "success")
                    stateManager?.updateState(
                        "lastFormSubmission",
                        kotlin.time.Clock.System
                            .now()
                            .toEpochMilliseconds()
                            .toString(),
                    )

                    println("Form submitted successfully")
                } else {
                    // Update error state
                    stateManager?.updateState("isSubmitting", false)
                    stateManager?.updateState("formSubmissionStatus", "error")
                    stateManager?.updateState(
                        "formSubmissionError",
                        result.exceptionOrNull()?.message ?: "Unknown error",
                    )

                    println("Form submission failed: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                // Update error state
                stateManager?.updateState("isSubmitting", false)
                stateManager?.updateState("formSubmissionStatus", "error")
                stateManager?.updateState("formSubmissionError", e.message ?: "Unknown error")

                println("Form submission error: ${e.message}")
            }
        }
    }

    /**
     * Parse and dispatch actions from string format (e.g., "navigate:/home" or "show_dialog:Error")
     */
    private fun dispatchActionFromString(actionString: String) {
        val parts = actionString.split(":", limit = 2)
        if (parts.size != 2) {
            println("Invalid action string format: $actionString")
            return
        }

        val actionType = parts[0]
        val actionData = parts[1]

        when (actionType) {
            "navigate" -> {
                // Parse route and arguments from actionData
                val routeParts = actionData.split("?")
                val route = routeParts[0]
                val arguments =
                    if (routeParts.size > 1) {
                        parseQueryString(routeParts[1])
                    } else {
                        null
                    }
                onNavigate(route, arguments)
            }
            "show_dialog" -> {
                onShowDialog("Info", actionData, "info")
            }
            "update_state" -> {
                // Parse key=value format
                val keyValue = actionData.split("=", limit = 2)
                if (keyValue.size == 2) {
                    stateManager?.updateState(keyValue[0], keyValue[1])
                }
            }
            else -> {
                // Try to find a custom handler
                val handler = customHandlers[actionType]
                if (handler != null) {
                    handler(mapOf("data" to actionData))
                } else {
                    println("Unknown action type: $actionType")
                }
            }
        }
    }

    /**
     * Parse query string into a map of key-value pairs
     */
    private fun parseQueryString(queryString: String): Map<String, String> = queryString
        .split("&")
        .mapNotNull { param ->
            val keyValue = param.split("=", limit = 2)
            if (keyValue.size == 2) {
                keyValue[0] to keyValue[1]
            } else {
                null
            }
        }.toMap()

    /**
     * Get current state value
     */
    fun getState(key: String): SduiStateValue? = stateManager?.getState(key)

    /**
     * Get current state as string
     */
    fun getStringState(key: String): String? = stateManager?.getStringState(key)

    /**
     * Get current state as boolean
     */
    fun getBooleanState(key: String): Boolean? = stateManager?.getBooleanState(key)

    /**
     * Get current state as integer
     */
    fun getLongState(key: String): Long? = stateManager?.getLongState(key)

    /**
     * Get current state as double
     */
    fun getDoubleState(key: String): Double? = stateManager?.getDoubleState(key)

    /**
     * Get current state as list
     */
    fun getListState(key: String): List<String>? = stateManager?.getListState(key)

    /**
     * Get current state as map
     */
    fun getMapState(key: String): Map<String, String>? = stateManager?.getMapState(key)

    /**
     * Add a state change listener
     */
    fun addStateListener(listener: StateChangeListener) {
        stateManager?.addListener(listener)
    }

    /**
     * Remove a state change listener
     */
    fun removeStateListener(listener: StateChangeListener) {
        stateManager?.removeListener(listener)
    }
}

/**
 * Provider class to switch between different ActionDispatcher implementations
 */
object ActionDispatcherProvider {
    /**
     * Create a logging dispatcher for development/debugging
     */
    fun createLoggingDispatcher(): ActionDispatcher = object : ActionDispatcher {
        override fun dispatch(action: SduiAction) {
            println("SDUI Action (Logging): $action")
        }
    }

    /**
     * Create a default dispatcher with provided callbacks
     */
    fun createDefaultDispatcher(
        onNavigate: (String, Map<String, String>?) -> Unit,
        onShowDialog: (title: String, message: String, type: String) -> Unit,
        submitForm: suspend (formData: Map<String, Any?>) -> Result<Unit>,
        callApi: suspend (
            url: String,
            method: String,
            headers: Map<String, String>?,
            body: Map<String, Any?>?,
        ) -> ApiResponse,
        customHandlers: Map<String, (Map<String, String>?) -> Unit> = emptyMap(),
        coroutineScope: CoroutineScope? = null,
        stateManager: SduiStateManager? = null,
    ): ActionDispatcher = DefaultActionDispatcher(
        onNavigate = onNavigate,
        onShowDialog = onShowDialog,
        submitForm = submitForm,
        callApi = callApi,
        customHandlers = customHandlers,
        coroutineScope = coroutineScope,
        stateManager = stateManager,
    )
}

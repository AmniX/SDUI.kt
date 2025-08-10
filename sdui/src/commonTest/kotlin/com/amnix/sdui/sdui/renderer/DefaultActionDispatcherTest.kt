package com.amnix.sdui.sdui.renderer

import com.amnix.sdui.sdui.model.SduiAction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

@OptIn(kotlin.time.ExperimentalTime::class)
class DefaultActionDispatcherTest {
    @Test
    fun testFormSubmissionWithTimestamp() = runTest {
        val stateManager = SduiStateManager()
        var capturedFormData: Map<String, Any?>? = null

        val dispatcher =
            DefaultActionDispatcher(
                onNavigate = { _, _ -> },
                onShowDialog = { _, _, _ -> },
                submitForm = { formData ->
                    capturedFormData = formData
                    Result.success(Unit)
                },
                callApi = { _, _, _, _ -> ApiResponse(200, "success") },
                coroutineScope = this@runTest,
                stateManager = stateManager,
            )

        val timestamp =
            kotlin.time.Clock.System
                .now()
                .toEpochMilliseconds()
        dispatcher.submitFormData(
            mapOf(
                "type" to "user_action",
                "timestamp" to timestamp.toString(),
                "action" to "button_click",
            ),
        )

        // Verify form data was captured
        assertNotNull(capturedFormData)
        assertEquals("user_action", capturedFormData!!["type"])
        assertEquals(timestamp.toString(), capturedFormData!!["timestamp"])
        assertEquals("button_click", capturedFormData!!["action"])

        // Verify state was updated
        assertFalse(stateManager.getBooleanState("isSubmitting")!!)
        assertEquals("success", stateManager.getStringState("formSubmissionStatus"))
        assertNotNull(stateManager.getStringState("lastFormSubmission"))
    }

    @Test
    fun testApiCallWithTimestampHeaders() = runTest {
        val stateManager = SduiStateManager()
        var capturedApiCall: Triple<String, String, Map<String, String>?>? = null

        val dispatcher =
            DefaultActionDispatcher(
                onNavigate = { _, _ -> },
                onShowDialog = { _, _, _ -> },
                submitForm = { _ -> Result.success(Unit) },
                callApi = { url, method, headers, _ ->
                    capturedApiCall = Triple(url, method, headers)
                    ApiResponse(200, "success")
                },
                coroutineScope = this@runTest,
                stateManager = stateManager,
            )

        val timestamp =
            kotlin.time.Clock.System
                .now()
                .toEpochMilliseconds()
        dispatcher.dispatch(
            SduiAction.ApiCall(
                url = "/api/data",
                method = "POST",
                headers =
                mapOf(
                    "X-Timestamp" to timestamp.toString(),
                    "X-Timezone" to "UTC",
                    "Content-Type" to "application/json",
                ),
                body = mapOf("data" to "test"),
                onSuccess = "update_state:apiCall=success",
                onError = "show_dialog:API call failed",
            ),
        )

        // Verify API call was made with timestamp headers
        assertNotNull(capturedApiCall)
        assertEquals("/api/data", capturedApiCall!!.first)
        assertEquals("POST", capturedApiCall!!.second)
        assertEquals(timestamp.toString(), capturedApiCall!!.third!!["X-Timestamp"])
        assertEquals("UTC", capturedApiCall!!.third!!["X-Timezone"])

        // Verify state was updated
        assertFalse(stateManager.getBooleanState("isLoading")!!)
        assertEquals("success", stateManager.getStringState("apiStatus"))
        assertEquals("success", stateManager.getStringState("lastApiResponse"))
    }

    @Test
    fun testUpdateStateWithTimestamp() = runTest {
        val stateManager = SduiStateManager()

        val dispatcher =
            DefaultActionDispatcher(
                onNavigate = { _, _ -> },
                onShowDialog = { _, _, _ -> },
                submitForm = { _ -> Result.success(Unit) },
                callApi = { _, _, _, _ -> ApiResponse(200, "success") },
                coroutineScope = this@runTest,
                stateManager = stateManager,
            )

        val timestamp =
            kotlin.time.Clock.System
                .now()
                .toEpochMilliseconds()
        dispatcher.dispatch(
            SduiAction.UpdateState("currentTimestamp", timestamp.toString()),
        )

        // Verify state was updated
        assertEquals(timestamp.toString(), stateManager.getStringState("currentTimestamp"))
        assertEquals(timestamp, stateManager.getLongState("currentTimestamp"))
    }

    @Test
    fun testCustomActionWithTimestamp() = runTest {
        val stateManager = SduiStateManager()
        var capturedAction: String? = null
        var capturedData: Map<String, String>? = null

        val dispatcher =
            DefaultActionDispatcher(
                onNavigate = { _, _ -> },
                onShowDialog = { _, _, _ -> },
                submitForm = { _ -> Result.success(Unit) },
                callApi = { _, _, _, _ -> ApiResponse(200, "success") },
                customHandlers =
                mapOf(
                    "log_timestamp" to { data ->
                        capturedAction = "log_timestamp"
                        capturedData = data
                    },
                ),
                coroutineScope = this@runTest,
                stateManager = stateManager,
            )

        val timestamp =
            kotlin.time.Clock.System
                .now()
                .toEpochMilliseconds()
        dispatcher.dispatch(
            SduiAction.Custom(
                action = "log_timestamp",
                data =
                mapOf(
                    "timestamp" to timestamp.toString(),
                    "event" to "user_action",
                ),
            ),
        )

        // Verify custom action was called
        assertEquals("log_timestamp", capturedAction)
        assertEquals(timestamp.toString(), capturedData!!["timestamp"])
        assertEquals("user_action", capturedData!!["event"])

        // Verify state was updated
        assertEquals("log_timestamp", stateManager.getStringState("lastCustomAction"))
        assertTrue(stateManager.getStringState("customActionData")!!.contains("timestamp"))
    }

    @Test
    fun testStateManagerIntegration() = runTest {
        val stateManager = SduiStateManager()

        val dispatcher =
            DefaultActionDispatcher(
                onNavigate = { _, _ -> },
                onShowDialog = { _, _, _ -> },
                submitForm = { _ -> Result.success(Unit) },
                callApi = { _, _, _, _ -> ApiResponse(200, "success") },
                coroutineScope = this@runTest,
                stateManager = stateManager,
            )

        // Test state manager integration methods
        val timestamp =
            kotlin.time.Clock.System
                .now()
                .toEpochMilliseconds()
        stateManager.updateState("testTimestamp", timestamp.toString())

        assertEquals(timestamp.toString(), dispatcher.getStringState("testTimestamp"))
        assertEquals(timestamp, dispatcher.getLongState("testTimestamp"))

        // Test state listener integration
        var capturedEvent: StateChangeEvent? = null
        dispatcher.addStateListener(
            object : StateChangeListener {
                override fun onStateChanged(event: StateChangeEvent) {
                    capturedEvent = event
                }
            },
        )

        stateManager.updateState("listenerTest", "value")

        assertNotNull(capturedEvent)
        assertEquals("listenerTest", capturedEvent!!.key)
        assertEquals("value", (capturedEvent!!.newValue as SduiStateValue.StringValue).value)
    }

    @Test
    fun testFormSubmissionErrorWithTimestamp() = runTest {
        val stateManager = SduiStateManager()

        val dispatcher =
            DefaultActionDispatcher(
                onNavigate = { _, _ -> },
                onShowDialog = { _, _, _ -> },
                submitForm = { _ -> Result.failure(Exception("Form submission failed")) },
                callApi = { _, _, _, _ -> ApiResponse(200, "success") },
                coroutineScope = this@runTest,
                stateManager = stateManager,
            )

        val timestamp =
            kotlin.time.Clock.System
                .now()
                .toEpochMilliseconds()
        dispatcher.submitFormData(
            mapOf(
                "type" to "error_test",
                "timestamp" to timestamp.toString(),
            ),
        )

        // Verify error state was set
        assertFalse(stateManager.getBooleanState("isSubmitting")!!)
        assertEquals("error", stateManager.getStringState("formSubmissionStatus"))
        assertEquals("Form submission failed", stateManager.getStringState("formSubmissionError"))
    }

    @Test
    fun testApiCallErrorWithTimestamp() = runTest {
        val stateManager = SduiStateManager()

        val dispatcher =
            DefaultActionDispatcher(
                onNavigate = { _, _ -> },
                onShowDialog = { _, _, _ -> },
                submitForm = { _ -> Result.success(Unit) },
                callApi = { _, _, _, _ -> throw Exception("API call failed") },
                coroutineScope = this@runTest,
                stateManager = stateManager,
            )

        val timestamp =
            kotlin.time.Clock.System
                .now()
                .toEpochMilliseconds()
        dispatcher.dispatch(
            SduiAction.ApiCall(
                url = "/api/error",
                method = "GET",
                headers = mapOf("X-Timestamp" to timestamp.toString()),
                body = null,
                onSuccess = "update_state:success",
                onError = "show_dialog:API error",
            ),
        )

        // Verify error state was set
        assertFalse(stateManager.getBooleanState("isLoading")!!)
        assertEquals("error", stateManager.getStringState("apiStatus"))
        assertEquals("API call failed", stateManager.getStringState("lastApiError"))
    }

    @Test
    fun testTimestampConsistency() = runTest {
        val stateManager = SduiStateManager()

        val dispatcher =
            DefaultActionDispatcher(
                onNavigate = { _, _ -> },
                onShowDialog = { _, _, _ -> },
                submitForm = { _ -> Result.success(Unit) },
                callApi = { _, _, _, _ -> ApiResponse(200, "success") },
                coroutineScope = this@runTest,
                stateManager = stateManager,
            )

        val timestamp1 =
            kotlin.time.Clock.System
                .now()
                .toEpochMilliseconds()
        dispatcher.dispatch(SduiAction.UpdateState("timestamp1", timestamp1.toString()))

        // Small delay
        kotlinx.coroutines.delay(10)

        val timestamp2 =
            kotlin.time.Clock.System
                .now()
                .toEpochMilliseconds()
        dispatcher.dispatch(SduiAction.UpdateState("timestamp2", timestamp2.toString()))

        // Verify timestamps are in order
        val retrievedTimestamp1 = stateManager.getLongState("timestamp1")
        val retrievedTimestamp2 = stateManager.getLongState("timestamp2")

        assertTrue(retrievedTimestamp1!! <= retrievedTimestamp2!!)
        assertTrue(timestamp1 <= timestamp2)
    }
}

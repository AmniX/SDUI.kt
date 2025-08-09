package com.amnix.sdui.sdui.renderer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

@OptIn(kotlin.time.ExperimentalTime::class)
class SduiStateManagerTest {

    @Test
    fun testStateChangeEventTimestamp() {
        val stateManager = SduiStateManager()
        var capturedEvent: StateChangeEvent? = null
        
        stateManager.addListener(object : StateChangeListener {
            override fun onStateChanged(event: StateChangeEvent) {
                capturedEvent = event
            }
        })
        
        stateManager.updateState("testKey", "testValue")
        
        assertNotNull(capturedEvent)
        assertTrue(capturedEvent!!.timestamp > 0)
        assertEquals("testKey", capturedEvent!!.key)
        assertEquals("testValue", (capturedEvent!!.newValue as SduiStateValue.StringValue).value)
    }

    @Test
    fun testTimestampStateManagement() {
        val stateManager = SduiStateManager()
        
        // Test storing timestamp as string
        val timestamp = kotlin.time.Clock.System.now().toEpochMilliseconds()
        stateManager.updateState("currentTimestamp", timestamp.toString())
        
        val retrievedTimestamp = stateManager.getStringState("currentTimestamp")
        assertEquals(timestamp.toString(), retrievedTimestamp)
        
        // Test storing timestamp as integer
        stateManager.updateState("timestampInt", timestamp.toInt())
        val retrievedTimestampInt = stateManager.getLongState("timestampInt")
        assertEquals(timestamp, retrievedTimestampInt)
    }

    @Test
    fun testStateManagerWithDateTimeValues() {
        val stateManager = SduiStateManager()
        
        val now = kotlin.time.Clock.System.now()
        val epochMillis = now.toEpochMilliseconds()
        
        // Store various datetime-related values
        stateManager.updateState("sessionStart", epochMillis.toString())
        stateManager.updateState("timezone", "UTC")
        stateManager.updateState("isActive", true)
        
        // Test retrieval
        assertEquals(epochMillis.toString(), stateManager.getStringState("sessionStart"))
        assertEquals("UTC", stateManager.getStringState("timezone"))
        assertTrue(stateManager.getBooleanState("isActive")!!)
        
        // Test type conversion
        val sessionStartInt = stateManager.getLongState("sessionStart")
        assertEquals(epochMillis, sessionStartInt)
    }

    @Test
    fun testStateManagerWithJsonTimestamp() {
        val stateManager = SduiStateManager()
        
        val now = kotlin.time.Clock.System.now()
        val timestampJson = """
            {
                "epochMillis": ${now.toEpochMilliseconds()},
                "isoString": "${now.toString()}",
                "timezone": "UTC"
            }
        """.trimIndent()
        
        stateManager.updateStateJson("timestampData", timestampJson)
        
        val retrievedJson = stateManager.getStringState("timestampData")
        assertEquals(timestampJson, retrievedJson)
    }

    @Test
    fun testStateManagerListenerWithTimestamps() = runTest {
        val stateManager = SduiStateManager()
        val events = mutableListOf<StateChangeEvent>()
        
        stateManager.addListener(object : StateChangeListener {
            override fun onStateChanged(event: StateChangeEvent) {
                events.add(event)
            }
        })
        
        val timestamp1 = kotlin.time.Clock.System.now().toEpochMilliseconds()
        stateManager.updateState("timestamp1", timestamp1.toString())
        
        // Small delay to ensure different timestamps
        kotlinx.coroutines.delay(10)
        
        val timestamp2 = kotlin.time.Clock.System.now().toEpochMilliseconds()
        stateManager.updateState("timestamp2", timestamp2.toString())
        
        assertEquals(2, events.size)
        assertTrue(events[0].timestamp <= events[1].timestamp)
        assertEquals("timestamp1", events[0].key)
        assertEquals("timestamp2", events[1].key)
    }

    @Test
    fun testStateManagerClearWithTimestamps() {
        val stateManager = SduiStateManager()
        
        val now = kotlin.time.Clock.System.now()
        stateManager.updateState("currentTime", now.toEpochMilliseconds().toString())
        stateManager.updateState("timezone", "UTC")
        
        // Verify state exists
        assertNotNull(stateManager.getStringState("currentTime"))
        assertNotNull(stateManager.getStringState("timezone"))
        
        // Clear state
        stateManager.clearState()
        
        // Verify state is cleared
        assertNull(stateManager.getStringState("currentTime"))
        assertNull(stateManager.getStringState("timezone"))
    }

    @Test
    fun testStateManagerRemoveWithTimestamps() {
        val stateManager = SduiStateManager()
        
        val now = kotlin.time.Clock.System.now()
        stateManager.updateState("timestamp", now.toEpochMilliseconds().toString())
        
        // Verify state exists
        assertNotNull(stateManager.getStringState("timestamp"))
        
        // Remove specific state
        stateManager.removeState("timestamp")
        
        // Verify state is removed
        assertNull(stateManager.getStringState("timestamp"))
    }

    @Test
    fun testStateManagerHasStateWithTimestamps() {
        val stateManager = SduiStateManager()
        
        val now = kotlin.time.Clock.System.now()
        stateManager.updateState("currentTimestamp", now.toEpochMilliseconds().toString())
        
        assertTrue(stateManager.hasState("currentTimestamp"))
        assertFalse(stateManager.hasState("nonexistent"))
    }

    @Test
    fun testStateManagerGetStateKeysWithTimestamps() {
        val stateManager = SduiStateManager()
        
        val now = kotlin.time.Clock.System.now()
        stateManager.updateState("timestamp1", now.toEpochMilliseconds().toString())
        stateManager.updateState("timestamp2", (now.toEpochMilliseconds() + 1000).toString())
        
        val keys = stateManager.getStateKeys()
        assertTrue(keys.contains("timestamp1"))
        assertTrue(keys.contains("timestamp2"))
        assertEquals(2, keys.size)
    }

    @Test
    fun testStateManagerGetStateAsStringMapWithTimestamps() {
        val stateManager = SduiStateManager()
        
        val now = kotlin.time.Clock.System.now()
        stateManager.updateState("timestamp", now.toEpochMilliseconds().toString())
        stateManager.updateState("timezone", "UTC")
        stateManager.updateState("isActive", true)
        
        val stringMap = stateManager.getStateAsStringMap()
        assertEquals(now.toEpochMilliseconds().toString(), stringMap["timestamp"])
        assertEquals("UTC", stringMap["timezone"])
        assertEquals("true", stringMap["isActive"])
    }
} 
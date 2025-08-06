package com.amnix.sdui.sdui

import com.amnix.sdui.sdui.components.SduiComponent
import kotlinx.serialization.json.Json

object SduiSerializer {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }
    
    fun serialize(component: SduiComponent): String {
        return json.encodeToString(SduiComponent.serializer(), component)
    }
    
    fun deserialize(jsonString: String): SduiComponent {
        return json.decodeFromString(SduiComponent.serializer(), jsonString)
    }
    
    fun serializeList(components: List<SduiComponent>): String {
        return json.encodeToString(kotlinx.serialization.builtins.ListSerializer(SduiComponent.serializer()), components)
    }
    
    fun deserializeList(jsonString: String): List<SduiComponent> {
        return json.decodeFromString(kotlinx.serialization.builtins.ListSerializer(SduiComponent.serializer()), jsonString)
    }
} 
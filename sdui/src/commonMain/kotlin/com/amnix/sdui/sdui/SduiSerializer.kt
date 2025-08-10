package com.amnix.sdui.sdui

import com.amnix.sdui.sdui.components.SduiComponent
import kotlinx.serialization.json.Json

object SduiSerializer {
    private val json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        }

    fun serialize(component: SduiComponent): String = json.encodeToString(SduiComponent.serializer(), component)

    fun deserialize(jsonString: String): SduiComponent = json.decodeFromString(SduiComponent.serializer(), jsonString)

    fun serializeList(components: List<SduiComponent>): String =
        json.encodeToString(kotlinx.serialization.builtins.ListSerializer(SduiComponent.serializer()), components)

    fun deserializeList(jsonString: String): List<SduiComponent> =
        json.decodeFromString(kotlinx.serialization.builtins.ListSerializer(SduiComponent.serializer()), jsonString)
} 

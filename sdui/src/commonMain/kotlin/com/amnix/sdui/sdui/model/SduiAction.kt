package com.amnix.sdui.sdui.model

import kotlinx.serialization.Serializable

@Serializable
sealed class SduiAction {
    @Serializable
    @kotlinx.serialization.SerialName("navigate")
    data class Navigate(val payload: Map<String, String>? = null) : SduiAction() {
        val route: String?
            get() = payload?.get("route")
    }

    @Serializable
    @kotlinx.serialization.SerialName("api_call")
    data class ApiCall(
        val url: String,
        val method: String = "GET",
        val headers: Map<String, String>? = null,
        val body: Map<String, String>? = null,
        val onSuccess: String? = null,
        val onError: String? = null,
    ) : SduiAction()

    @Serializable
    @kotlinx.serialization.SerialName("show_dialog")
    data class ShowDialog(val title: String, val message: String, val type: String = "info") : SduiAction()

    @Serializable
    @kotlinx.serialization.SerialName("update_state")
    data class UpdateState(val key: String, val value: String) : SduiAction()

    @Serializable
    @kotlinx.serialization.SerialName("reset")
    data class Reset(val payload: Map<String, String>? = null) : SduiAction()

    @Serializable
    @kotlinx.serialization.SerialName("custom")
    data class Custom(val action: String, val data: Map<String, String>? = null) : SduiAction()
}

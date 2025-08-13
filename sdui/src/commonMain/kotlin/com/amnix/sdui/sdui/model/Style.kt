package com.amnix.sdui.sdui.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class Style(
    val padding: Padding? = null,
    val margin: Margin? = null,
    val backgroundColor: String? = null,
    val cornerRadius: Float? = null,
    val fontSize: Float? = null,
    val fontWeight: String? = null,
    val textColor: String? = null,
    val alignment: String? = null,
    val width: String? = null,
    val height: String? = null,
    @Serializable(with = AspectRatioAsStringSerializer::class)
    val aspectRatio: String? = null,
    val scroll: Boolean? = null,
    val maxWidth: String? = null,
    val maxHeight: String? = null,
    val minWidth: String? = null,
    val minHeight: String? = null,
    val flex: Float? = null,
    val flexDirection: String? = null,
    val justifyContent: String? = null,
    val alignItems: String? = null,
    val borderWidth: Float? = null,
    val borderColor: String? = null,
    val shadowRadius: Float? = null,
    val shadowColor: String? = null,
    val shadowOffsetX: Float? = null,
    val shadowOffsetY: Float? = null,
    val opacity: Float? = null,
    val rotation: Float? = null,
    val scale: Float? = null,
    val zIndex: Int? = null,
)

@Suppress("unused")
object AspectRatioAsStringSerializer : kotlinx.serialization.KSerializer<String?> {
    override val descriptor = kotlinx.serialization.descriptors.PrimitiveSerialDescriptor(
        "AspectRatio",
        kotlinx.serialization.descriptors.PrimitiveKind.STRING,
    )

    override fun serialize(encoder: kotlinx.serialization.encoding.Encoder, value: String?) {
        if (value == null) encoder.encodeNull() else encoder.encodeString(value)
    }

    override fun deserialize(decoder: kotlinx.serialization.encoding.Decoder): String? = try {
        decoder.decodeString()
    } catch (_: Exception) {
        null
    }
}

@Serializable
data class Padding(
    val top: Float? = null,
    val bottom: Float? = null,
    val start: Float? = null,
    val end: Float? = null,
    val horizontal: Float? = null,
    val vertical: Float? = null,
    val all: Float? = null,
)

@Serializable
data class Margin(
    val top: Float? = null,
    val bottom: Float? = null,
    val start: Float? = null,
    val end: Float? = null,
    val horizontal: Float? = null,
    val vertical: Float? = null,
    val all: Float? = null,
)

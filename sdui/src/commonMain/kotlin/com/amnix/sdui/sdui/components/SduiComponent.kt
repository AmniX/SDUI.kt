package com.amnix.sdui.sdui.components

import com.amnix.sdui.sdui.model.SduiAction
import com.amnix.sdui.sdui.model.Style
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlinx.serialization.Serializable

@Serializable
sealed class SduiComponent {
    abstract val id: String
    abstract val style: Style?
    abstract val action: SduiAction?
    abstract val visible: Boolean?
    abstract val meta: Map<String, String>?

    @Serializable
    @kotlinx.serialization.SerialName("text")
    data class TextComponent(
        override val id: String = "text_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val text: String,
        val maxLines: Int? = null,
        val textOverflow: String? = null,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("button")
    data class ButtonComponent(
        override val id: String = "button_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val text: String,
        val enabled: Boolean? = true,
        val loading: Boolean? = false,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("column")
    data class ColumnComponent(
        override val id: String = "column_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val children: List<SduiComponent> = emptyList(),
        val spacing: Float? = null,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("row")
    data class RowComponent(
        override val id: String = "row_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val children: List<SduiComponent> = emptyList(),
        val spacing: Float? = null,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("image")
    data class ImageComponent(
        override val id: String = "image_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val url: String,
        val altText: String? = null,
        val contentDescription: String? = null,
        val placeholder: String? = null,
        val errorPlaceholder: String? = null,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("textField")
    data class TextFieldComponent(
        override val id: String = "textfield_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val placeholder: String? = null,
        val value: String? = null,
        val enabled: Boolean? = true,
        val keyboardType: String? = null,
        val maxLines: Int? = 1,
        val isPassword: Boolean? = false,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("spacer")
    data class SpacerComponent(
        override val id: String = "spacer_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val width: Float? = null,
        val height: Float? = null,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("divider")
    data class DividerComponent(
        override val id: String = "divider_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val color: String? = null,
        val thickness: Float? = null,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("box")
    data class BoxComponent(
        override val id: String = "box_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val children: List<SduiComponent> = emptyList(),
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("card")
    data class CardComponent(
        override val id: String = "card_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val children: List<SduiComponent> = emptyList(),
        val elevation: Float? = null,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("list")
    data class ListComponent(
        override val id: String = "list_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val items: List<SduiComponent> = emptyList(),
        val itemSpacing: Float? = null,
        val scrollable: Boolean? = true,
        val showScrollIndicator: Boolean? = true,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("grid")
    data class GridComponent(
        override val id: String = "grid_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val items: List<SduiComponent> = emptyList(),
        val columns: Int = 2,
        val itemSpacing: Float? = null,
        val scrollable: Boolean? = true,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("switch")
    data class SwitchComponent(
        override val id: String = "switch_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val checked: Boolean = false,
        val enabled: Boolean? = true,
        val label: String? = null,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("checkbox")
    data class CheckboxComponent(
        override val id: String = "checkbox_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val checked: Boolean = false,
        val enabled: Boolean? = true,
        val label: String? = null,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("radioButton")
    data class RadioButtonComponent(
        override val id: String = "radiobutton_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val selected: Boolean = false,
        val enabled: Boolean? = true,
        val label: String? = null,
        val group: String? = null,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("progressBar")
    data class ProgressBarComponent(
        override val id: String = "progressbar_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val progress: Float = 0f,
        val indeterminate: Boolean? = false,
        val label: String? = null,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("slider")
    data class SliderComponent(
        override val id: String = "slider_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val value: Float = 0f,
        val minValue: Float = 0f,
        val maxValue: Float = 100f,
        val step: Float? = null,
        val enabled: Boolean? = true,
        val label: String? = null,
    ) : SduiComponent()

    @Serializable
    @kotlinx.serialization.SerialName("chip")
    data class ChipComponent(
        override val id: String = "chip_${Random.nextUInt()}",
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val text: String,
        val selected: Boolean? = false,
        val enabled: Boolean? = true,
        val icon: String? = null,
    ) : SduiComponent()
}

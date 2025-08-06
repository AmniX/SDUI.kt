package com.amnix.sdui.sdui.components

import com.amnix.sdui.sdui.model.SduiAction
import com.amnix.sdui.sdui.model.Style
import kotlinx.serialization.Serializable

@Serializable
sealed class SduiComponent {
    abstract val id: String?
    abstract val style: Style?
    abstract val action: SduiAction?
    abstract val visible: Boolean?
    abstract val meta: Map<String, String>?
    
    @Serializable
    @kotlinx.serialization.SerialName("Text")
    data class TextComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val text: String,
        val maxLines: Int? = null,
        val textOverflow: String? = null
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("Button")
    data class ButtonComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val text: String,
        val enabled: Boolean? = true,
        val loading: Boolean? = false
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("Column")
    data class ColumnComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val children: List<SduiComponent> = emptyList(),
        val spacing: Float? = null
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("Row")
    data class RowComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val children: List<SduiComponent> = emptyList(),
        val spacing: Float? = null
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("Image")
    data class ImageComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val url: String,
        val altText: String? = null,
        val contentDescription: String? = null,
        val placeholder: String? = null,
        val errorPlaceholder: String? = null
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("TextField")
    data class TextFieldComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val placeholder: String? = null,
        val value: String? = null,
        val enabled: Boolean? = true,
        val keyboardType: String? = null,
        val maxLines: Int? = 1,
        val isPassword: Boolean? = false
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("Spacer")
    data class SpacerComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val width: Float? = null,
        val height: Float? = null
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("Divider")
    data class DividerComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val color: String? = null,
        val thickness: Float? = null
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("Box")
    data class BoxComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val children: List<SduiComponent> = emptyList()
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("Card")
    data class CardComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val children: List<SduiComponent> = emptyList(),
        val elevation: Float? = null
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("List")
    data class ListComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val items: List<SduiComponent> = emptyList(),
        val itemSpacing: Float? = null,
        val scrollable: Boolean? = true,
        val showScrollIndicator: Boolean? = true
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("Grid")
    data class GridComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val items: List<SduiComponent> = emptyList(),
        val columns: Int = 2,
        val itemSpacing: Float? = null,
        val scrollable: Boolean? = true
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("Switch")
    data class SwitchComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val checked: Boolean = false,
        val enabled: Boolean? = true,
        val label: String? = null
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("Checkbox")
    data class CheckboxComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val checked: Boolean = false,
        val enabled: Boolean? = true,
        val label: String? = null
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("RadioButton")
    data class RadioButtonComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val selected: Boolean = false,
        val enabled: Boolean? = true,
        val label: String? = null,
        val group: String? = null
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("ProgressBar")
    data class ProgressBarComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val progress: Float = 0f,
        val indeterminate: Boolean? = false,
        val label: String? = null
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("Slider")
    data class SliderComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val value: Float = 0f,
        val minValue: Float = 0f,
        val maxValue: Float = 100f,
        val step: Float? = null,
        val enabled: Boolean? = true,
        val label: String? = null
    ) : SduiComponent()
    
    @Serializable
    @kotlinx.serialization.SerialName("Chip")
    data class ChipComponent(
        override val id: String? = null,
        override val style: Style? = null,
        override val action: SduiAction? = null,
        override val visible: Boolean? = true,
        override val meta: Map<String, String>? = null,
        val text: String,
        val selected: Boolean? = false,
        val enabled: Boolean? = true,
        val icon: String? = null
    ) : SduiComponent()
} 
package com.amnix.sdui.sdui.validation

import com.amnix.sdui.sdui.components.SduiComponent
import com.amnix.sdui.sdui.model.Style

/**
 * Validator interface for SDUI components
 */
interface SduiValidator {
    fun validate(component: SduiComponent): List<String>

    fun validate(components: List<SduiComponent>): List<String>
}

/**
 * Default implementation of SDUI validator with comprehensive validation rules
 */
class DefaultSduiValidator : SduiValidator {
    override fun validate(component: SduiComponent): List<String> {
        val errors = mutableListOf<String>()

        // Common validation for all components
        validateCommonFields(component, errors)

        // Component-specific validation
        when (component) {
            is SduiComponent.TextComponent -> validateTextComponent(component, errors)
            is SduiComponent.ButtonComponent -> validateButtonComponent(component, errors)
            is SduiComponent.ColumnComponent -> validateColumnComponent(component, errors)
            is SduiComponent.RowComponent -> validateRowComponent(component, errors)
            is SduiComponent.ImageComponent -> validateImageComponent(component, errors)
            is SduiComponent.TextFieldComponent -> validateTextFieldComponent(component, errors)
            is SduiComponent.SpacerComponent -> validateSpacerComponent(component, errors)
            is SduiComponent.DividerComponent -> validateDividerComponent(component, errors)
            is SduiComponent.BoxComponent -> validateBoxComponent(component, errors)
            is SduiComponent.CardComponent -> validateCardComponent(component, errors)
            is SduiComponent.ListComponent -> validateListComponent(component, errors)
            is SduiComponent.GridComponent -> validateGridComponent(component, errors)
            is SduiComponent.SwitchComponent -> validateSwitchComponent(component, errors)
            is SduiComponent.CheckboxComponent -> validateCheckboxComponent(component, errors)
            is SduiComponent.RadioButtonComponent -> validateRadioButtonComponent(component, errors)
            is SduiComponent.ProgressBarComponent -> validateProgressBarComponent(component, errors)
            is SduiComponent.SliderComponent -> validateSliderComponent(component, errors)
            is SduiComponent.ChipComponent -> validateChipComponent(component, errors)
        }

        return errors
    }

    override fun validate(components: List<SduiComponent>): List<String> {
        val errors = mutableListOf<String>()
        components.forEach { component ->
            errors.addAll(validate(component))
        }
        return errors
    }

    private fun validateCommonFields(
        component: SduiComponent,
        errors: MutableList<String>,
    ) {
        // Validate style properties
        component.style?.let { style ->
            validateStyle(style, component.id, errors)
        }
    }

    private fun validateStyle(
        style: Style,
        componentId: String?,
        errors: MutableList<String>,
    ) {
        val idPrefix = componentId?.let { "[$it] " } ?: ""

        // Validate width and height values
        style.width?.let { width ->
            if (!isValidDimension(width)) {
                errors.add("${idPrefix}Style width must be a valid dimension, got: $width")
            }
        }

        style.height?.let { height ->
            if (!isValidDimension(height)) {
                errors.add("${idPrefix}Style height must be a valid dimension, got: $height")
            }
        }

        style.maxWidth?.let { maxWidth ->
            if (!isValidDimension(maxWidth)) {
                errors.add("${idPrefix}Style maxWidth must be a valid dimension, got: $maxWidth")
            }
        }

        style.maxHeight?.let { maxHeight ->
            if (!isValidDimension(maxHeight)) {
                errors.add("${idPrefix}Style maxHeight must be a valid dimension, got: $maxHeight")
            }
        }

        style.minWidth?.let { minWidth ->
            if (!isValidDimension(minWidth)) {
                errors.add("${idPrefix}Style minWidth must be a valid dimension, got: $minWidth")
            }
        }

        style.minHeight?.let { minHeight ->
            if (!isValidDimension(minHeight)) {
                errors.add("${idPrefix}Style minHeight must be a valid dimension, got: $minHeight")
            }
        }

        // Validate opacity is between 0 and 1
        style.opacity?.let { opacity ->
            if (opacity < 0 || opacity > 1) {
                errors.add("${idPrefix}Style opacity must be between 0 and 1, got: $opacity")
            }
        }

        // Validate zIndex is non-negative
        style.zIndex?.let { zIndex ->
            if (zIndex < 0) {
                errors.add("${idPrefix}Style zIndex must be >= 0, got: $zIndex")
            }
        }
    }

    private fun validateTextComponent(
        component: SduiComponent.TextComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        if (component.text.isBlank()) {
            errors.add("${idPrefix}Text component text must not be blank")
        }
    }

    private fun validateButtonComponent(
        component: SduiComponent.ButtonComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        if (component.text.isBlank()) {
            errors.add("${idPrefix}Button component text must not be blank")
        }
    }

    private fun validateColumnComponent(
        component: SduiComponent.ColumnComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        if (component.children.isEmpty()) {
            errors.add("${idPrefix}Column component must have at least one child")
        } else {
            // Recursively validate children
            component.children.forEachIndexed { index, child ->
                val childErrors = validate(child)
                childErrors.forEach { error ->
                    errors.add("${idPrefix}Child[$index]: $error")
                }
            }
        }
    }

    private fun validateRowComponent(
        component: SduiComponent.RowComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        if (component.children.isEmpty()) {
            errors.add("${idPrefix}Row component must have at least one child")
        } else {
            // Recursively validate children
            component.children.forEachIndexed { index, child ->
                val childErrors = validate(child)
                childErrors.forEach { error ->
                    errors.add("${idPrefix}Child[$index]: $error")
                }
            }
        }
    }

    private fun validateImageComponent(
        component: SduiComponent.ImageComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        if (component.url.isBlank()) {
            errors.add("${idPrefix}Image component URL must not be blank")
        } else if (!isValidUrl(component.url)) {
            errors.add("${idPrefix}Image component URL must be a valid HTTP(S) URL, got: ${component.url}")
        }
    }

    private fun validateTextFieldComponent(
        component: SduiComponent.TextFieldComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        // If isPassword is true, value should be null or blank for security
        if (component.isPassword == true && !component.value.isNullOrBlank()) {
            errors.add("${idPrefix}TextField with isPassword=true should not have a pre-filled value for security")
        }
    }

    private fun validateSpacerComponent(
        component: SduiComponent.SpacerComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        if (component.width == null && component.height == null) {
            errors.add("${idPrefix}Spacer component must have at least one of width or height set")
        }
    }

    private fun validateDividerComponent(
        component: SduiComponent.DividerComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        component.thickness?.let { thickness ->
            if (thickness <= 0) {
                errors.add("${idPrefix}Divider thickness must be > 0, got: $thickness")
            }
        }
    }

    private fun validateBoxComponent(
        component: SduiComponent.BoxComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        if (component.children.isEmpty()) {
            errors.add("${idPrefix}Box component must have at least one child")
        } else {
            // Recursively validate children
            component.children.forEachIndexed { index, child ->
                val childErrors = validate(child)
                childErrors.forEach { error ->
                    errors.add("${idPrefix}Child[$index]: $error")
                }
            }
        }
    }

    private fun validateCardComponent(
        component: SduiComponent.CardComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        if (component.children.isEmpty()) {
            errors.add("${idPrefix}Card component must have at least one child")
        } else {
            // Recursively validate children
            component.children.forEachIndexed { index, child ->
                val childErrors = validate(child)
                childErrors.forEach { error ->
                    errors.add("${idPrefix}Child[$index]: $error")
                }
            }
        }
    }

    private fun validateListComponent(
        component: SduiComponent.ListComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        if (component.items.isEmpty()) {
            errors.add("${idPrefix}List component must have at least one item")
        } else {
            // Recursively validate items
            component.items.forEachIndexed { index, item ->
                val itemErrors = validate(item)
                itemErrors.forEach { error ->
                    errors.add("${idPrefix}Item[$index]: $error")
                }
            }
        }
    }

    private fun validateGridComponent(
        component: SduiComponent.GridComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        if (component.columns <= 0) {
            errors.add("${idPrefix}Grid component columns must be > 0, got: ${component.columns}")
        }

        if (component.items.isEmpty()) {
            errors.add("${idPrefix}Grid component must have at least one item")
        } else {
            // Recursively validate items
            component.items.forEachIndexed { index, item ->
                val itemErrors = validate(item)
                itemErrors.forEach { error ->
                    errors.add("${idPrefix}Item[$index]: $error")
                }
            }
        }
    }

    private fun validateSwitchComponent(
        component: SduiComponent.SwitchComponent,
        errors: MutableList<String>,
    ) {
        // No specific validation rules for Switch component
    }

    private fun validateCheckboxComponent(
        component: SduiComponent.CheckboxComponent,
        errors: MutableList<String>,
    ) {
        // No specific validation rules for Checkbox component
    }

    private fun validateRadioButtonComponent(
        component: SduiComponent.RadioButtonComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        if (component.group.isNullOrBlank()) {
            errors.add("${idPrefix}RadioButton component should have a group name for proper selection behavior")
        }
    }

    private fun validateProgressBarComponent(
        component: SduiComponent.ProgressBarComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        if (component.progress < 0 || component.progress > 1) {
            errors.add("${idPrefix}ProgressBar progress must be between 0 and 1, got: ${component.progress}")
        }
    }

    private fun validateSliderComponent(
        component: SduiComponent.SliderComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        if (component.minValue >= component.maxValue) {
            errors.add("${idPrefix}Slider minValue must be < maxValue, got: min=${component.minValue}, max=${component.maxValue}")
        } else if (component.value < component.minValue || component.value > component.maxValue) {
            errors.add(
                "${idPrefix}Slider value must be between minValue and maxValue, got: value=${component.value}, min=${component.minValue}, max=${component.maxValue}",
            )
        }

        component.step?.let { step ->
            if (step <= 0) {
                errors.add("${idPrefix}Slider step must be > 0, got: $step")
            }
        }
    }

    private fun validateChipComponent(
        component: SduiComponent.ChipComponent,
        errors: MutableList<String>,
    ) {
        val idPrefix = component.id?.let { "[$it] " } ?: ""

        if (component.text.isBlank()) {
            errors.add("${idPrefix}Chip component text must not be blank")
        }
    }

    private fun isValidUrl(url: String): Boolean = url.startsWith("http://") || url.startsWith("https://") || url.startsWith("data:")

    private fun isValidDimension(dimension: String): Boolean =
        when {
            dimension == "100%" -> true
            dimension.endsWith("%") -> {
                val percentage = dimension.removeSuffix("%").toFloatOrNull()
                percentage != null && percentage >= 0 && percentage <= 100
            }
            dimension.endsWith("dp") -> {
                val dpValue = dimension.removeSuffix("dp").toFloatOrNull()
                dpValue != null && dpValue >= 0
            }
            else -> {
                val numericValue = dimension.toFloatOrNull()
                numericValue != null && numericValue >= 0
            }
        }
}

/**
 * Extension function to validate a component using the default validator
 */
fun SduiComponent.isValid(validator: SduiValidator = DefaultSduiValidator()): Boolean = validator.validate(this).isEmpty()

/**
 * Extension function to get validation errors for a component
 */
fun SduiComponent.getValidationErrors(validator: SduiValidator = DefaultSduiValidator()): List<String> = validator.validate(this) 

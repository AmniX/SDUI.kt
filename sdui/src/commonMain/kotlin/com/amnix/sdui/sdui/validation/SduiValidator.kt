package com.amnix.sdui.sdui.validation

import com.amnix.sdui.sdui.components.SduiComponent
import com.amnix.sdui.sdui.model.Style

/**
 * Validation result with detailed information
 */
data class ValidationResult(
    val isValid: Boolean,
    val errors: List<ValidationError>,
    val warnings: List<ValidationWarning> = emptyList()
) {
    companion object {
        fun success() = ValidationResult(true, emptyList())
        fun failure(errors: List<ValidationError>) = ValidationResult(false, errors)
        fun failure(vararg errors: ValidationError) = ValidationResult(false, errors.toList())
    }
}

/**
 * Detailed validation error
 */
data class ValidationError(
    val message: String,
    val componentId: String? = null,
    val field: String? = null,
    val value: String? = null,
    val severity: ErrorSeverity = ErrorSeverity.ERROR
)

/**
 * Validation warning
 */
data class ValidationWarning(
    val message: String,
    val componentId: String? = null,
    val field: String? = null,
    val suggestion: String? = null
)

/**
 * Error severity levels
 */
enum class ErrorSeverity {
    ERROR, WARNING, INFO
}

/**
 * Default implementation of SDUI validator with comprehensive validation rules
 */
object SduiValidator {
    
    fun validate(component: SduiComponent): ValidationResult {
        val errors = mutableListOf<ValidationError>()
        val warnings = mutableListOf<ValidationWarning>()

        // Common validation for all components
        validateCommonFields(component, errors, warnings)

        // Component-specific validation
        when (component) {
            is SduiComponent.TextComponent -> validateTextComponent(component, errors, warnings)
            is SduiComponent.ButtonComponent -> validateButtonComponent(component, errors, warnings)
            is SduiComponent.ColumnComponent -> validateColumnComponent(component, errors, warnings)
            is SduiComponent.RowComponent -> validateRowComponent(component, errors, warnings)
            is SduiComponent.ImageComponent -> validateImageComponent(component, errors, warnings)
            is SduiComponent.TextFieldComponent -> validateTextFieldComponent(component, errors, warnings)
            is SduiComponent.SpacerComponent -> validateSpacerComponent(component, errors, warnings)
            is SduiComponent.DividerComponent -> validateDividerComponent(component, errors, warnings)
            is SduiComponent.BoxComponent -> validateBoxComponent(component, errors, warnings)
            is SduiComponent.CardComponent -> validateCardComponent(component, errors, warnings)
            is SduiComponent.ListComponent -> validateListComponent(component, errors, warnings)
            is SduiComponent.GridComponent -> validateGridComponent(component, errors, warnings)
            is SduiComponent.SwitchComponent -> validateSwitchComponent(component, errors, warnings)
            is SduiComponent.CheckboxComponent -> validateCheckboxComponent(component, errors, warnings)
            is SduiComponent.RadioButtonComponent -> validateRadioButtonComponent(component, errors, warnings)
            is SduiComponent.ProgressBarComponent -> validateProgressBarComponent(component, errors, warnings)
            is SduiComponent.SliderComponent -> validateSliderComponent(component, errors, warnings)
            is SduiComponent.ChipComponent -> validateChipComponent(component, errors, warnings)
        }

        return if (errors.isEmpty()) {
            ValidationResult.success()
        } else {
            ValidationResult.failure(errors)
        }
    }

    fun validate(components: List<SduiComponent>): ValidationResult {
        val allErrors = mutableListOf<ValidationError>()
        val allWarnings = mutableListOf<ValidationWarning>()
        
        components.forEach { component ->
            val result = validate(component)
            allErrors.addAll(result.errors)
            allWarnings.addAll(result.warnings)
        }
        
        return if (allErrors.isEmpty()) {
            ValidationResult.success()
        } else {
            ValidationResult.failure(allErrors)
        }
    }

    private fun validateCommonFields(component: SduiComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {
        // Validate ID
        if (component.id.isBlank()) {
            warnings.add(ValidationWarning(
                message = "Component has empty ID",
                componentId = component.id,
                field = "id",
                suggestion = "Consider adding a meaningful ID for better debugging"
            ))
        }

        // Validate style properties
        component.style?.let { style ->
            validateStyle(style, component.id, errors, warnings)
        }
    }

    private fun validateStyle(style: Style, componentId: String?, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {
        val idPrefix = componentId?.let { "[$it] " } ?: ""

        // Validate width and height values
        style.width?.let { width ->
            if (!isValidDimension(width)) {
                errors.add(ValidationError(
                    message = "${idPrefix}Style width must be a valid dimension",
                    componentId = componentId,
                    field = "style.width",
                    value = width
                ))
            }
        }

        style.height?.let { height ->
            if (!isValidDimension(height)) {
                errors.add(ValidationError(
                    message = "${idPrefix}Style height must be a valid dimension",
                    componentId = componentId,
                    field = "style.height",
                    value = height
                ))
            }
        }

        // Validate color values
        style.backgroundColor?.let { color ->
            if (!isValidColor(color)) {
                errors.add(ValidationError(
                    message = "${idPrefix}Style backgroundColor must be a valid color",
                    componentId = componentId,
                    field = "style.backgroundColor",
                    value = color
                ))
            }
        }

        style.textColor?.let { color ->
            if (!isValidColor(color)) {
                errors.add(ValidationError(
                    message = "${idPrefix}Style textColor must be a valid color",
                    componentId = componentId,
                    field = "style.textColor",
                    value = color
                ))
            }
        }

        // Validate fontWeight values
        style.fontWeight?.let { fontWeight ->
            if (!isValidFontWeight(fontWeight)) {
                errors.add(ValidationError(
                    message = "${idPrefix}Style fontWeight must be a valid font weight",
                    componentId = componentId,
                    field = "style.fontWeight",
                    value = fontWeight,
                    severity = ErrorSeverity.ERROR
                ))
            }
        }
    }

    private fun validateTextComponent(component: SduiComponent.TextComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {
        if (component.text.isBlank()) {
            warnings.add(ValidationWarning(
                message = "Text component has empty text content",
                componentId = component.id,
                field = "text",
                suggestion = "Consider adding meaningful text or removing the component"
            ))
        }

        component.style?.fontSize?.let { fontSize ->
            if (fontSize <= 0) {
                errors.add(ValidationError(
                    message = "Font size must be positive",
                    componentId = component.id,
                    field = "style.fontSize",
                    value = fontSize.toString()
                ))
            }
        }
    }

    private fun validateButtonComponent(component: SduiComponent.ButtonComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {
        if (component.text.isBlank()) {
            errors.add(ValidationError(
                message = "Button must have text content",
                componentId = component.id,
                field = "text"
            ))
        }
    }

    private fun validateColumnComponent(component: SduiComponent.ColumnComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {
        if (component.children.isEmpty()) {
            warnings.add(ValidationWarning(
                message = "Column component has no children",
                componentId = component.id,
                field = "children",
                suggestion = "Consider adding child components or removing the empty column"
            ))
        }
    }

    private fun validateRowComponent(component: SduiComponent.RowComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {
        if (component.children.isEmpty()) {
            warnings.add(ValidationWarning(
                message = "Row component has no children",
                componentId = component.id,
                field = "children",
                suggestion = "Consider adding child components or removing the empty row"
            ))
        }
    }

    // Placeholder methods for other component types
    private fun validateImageComponent(component: SduiComponent.ImageComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {}
    private fun validateTextFieldComponent(component: SduiComponent.TextFieldComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {}
    private fun validateSpacerComponent(component: SduiComponent.SpacerComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {}
    private fun validateDividerComponent(component: SduiComponent.DividerComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {}
    private fun validateBoxComponent(component: SduiComponent.BoxComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {}
    private fun validateCardComponent(component: SduiComponent.CardComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {}
    private fun validateListComponent(component: SduiComponent.ListComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {}
    private fun validateGridComponent(component: SduiComponent.GridComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {}
    private fun validateSwitchComponent(component: SduiComponent.SwitchComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {}
    private fun validateCheckboxComponent(component: SduiComponent.CheckboxComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {}
    private fun validateRadioButtonComponent(component: SduiComponent.RadioButtonComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {}
    private fun validateProgressBarComponent(component: SduiComponent.ProgressBarComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {}
    private fun validateSliderComponent(component: SduiComponent.SliderComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {}
    private fun validateChipComponent(component: SduiComponent.ChipComponent, errors: MutableList<ValidationError>, warnings: MutableList<ValidationWarning>) {}

    private fun isValidDimension(value: String): Boolean {
        return when {
            value == "100%" -> true
            value.endsWith("%") -> {
                val percentage = value.removeSuffix("%").toFloatOrNull()
                percentage != null && percentage >= 0 && percentage <= 100
            }
            value.endsWith("dp") -> {
                val dpValue = value.removeSuffix("dp").toFloatOrNull()
                dpValue != null && dpValue >= 0
            }
            else -> {
                val numericValue = value.toFloatOrNull()
                numericValue != null && numericValue >= 0
            }
        }
    }
    
    private fun isValidColor(color: String): Boolean = color.matches(Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"))
    
    private fun isValidFontWeight(fontWeight: String): Boolean {
        return when (fontWeight.lowercase()) {
            "normal", "bold", "light", "medium", "thin", "ultralight", "semibold", "extrabold", "black" -> true
            else -> false
        }
    }
}

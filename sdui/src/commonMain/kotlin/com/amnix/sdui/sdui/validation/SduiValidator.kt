package com.amnix.sdui.sdui.validation

import com.amnix.sdui.sdui.components.SduiComponent
import com.amnix.sdui.sdui.model.Style
import com.amnix.sdui.sdui.model.Padding
import com.amnix.sdui.sdui.model.Margin

/**
 * Validation result with detailed information
 */
data class ValidationResult(
    val isValid: Boolean,
    val issues: List<ValidationIssue> = emptyList()
) {
    companion object {
        fun success() = ValidationResult(true)
        fun failure(issues: List<ValidationIssue>) = ValidationResult(false, issues)
        fun failure(vararg issues: ValidationIssue) = ValidationResult(false, issues.toList())
    }
}

/**
 * Unified validation issue that combines errors and warnings
 */
data class ValidationIssue(
    val message: String,
    val componentId: String? = null,
    val field: String? = null,
    val value: String? = null,
    val severity: IssueSeverity = IssueSeverity.ERROR,
    val suggestion: String? = null
)

/**
 * Issue severity levels
 */
enum class IssueSeverity {
    ERROR, WARNING, INFO
}

/**
 * Default implementation of SDUI validator with comprehensive validation rules
 */
object SduiValidator {
    
    fun validate(component: SduiComponent): ValidationResult {
        val issues = mutableListOf<ValidationIssue>()

        // Common validation for all components
        validateCommonFields(component, issues)

        // Component-specific validation
        when (component) {
            is SduiComponent.TextComponent -> validateTextComponent(component, issues)
            is SduiComponent.ButtonComponent -> validateButtonComponent(component, issues)
            is SduiComponent.ColumnComponent -> validateColumnComponent(component, issues)
            is SduiComponent.RowComponent -> validateRowComponent(component, issues)
            is SduiComponent.ImageComponent -> validateImageComponent(component, issues)
            is SduiComponent.TextFieldComponent -> validateTextFieldComponent(component, issues)
            is SduiComponent.SpacerComponent -> validateSpacerComponent(component, issues)
            is SduiComponent.DividerComponent -> validateDividerComponent(component, issues)
            is SduiComponent.BoxComponent -> validateBoxComponent(component, issues)
            is SduiComponent.CardComponent -> validateCardComponent(component, issues)
            is SduiComponent.ListComponent -> validateListComponent(component, issues)
            is SduiComponent.GridComponent -> validateGridComponent(component, issues)
            is SduiComponent.SwitchComponent -> validateSwitchComponent(component, issues)
            is SduiComponent.CheckboxComponent -> validateCheckboxComponent(component, issues)
            is SduiComponent.RadioButtonComponent -> validateRadioButtonComponent(component, issues)
            is SduiComponent.ProgressBarComponent -> validateProgressBarComponent(component, issues)
            is SduiComponent.SliderComponent -> validateSliderComponent(component, issues)
            is SduiComponent.ChipComponent -> validateChipComponent(component, issues)
        }

        val hasErrors = issues.any { it.severity == IssueSeverity.ERROR }
        return if (!hasErrors) {
            ValidationResult.success()
        } else {
            ValidationResult.failure(issues)
        }
    }

    private fun validateCommonFields(component: SduiComponent, issues: MutableList<ValidationIssue>) {
        // Validate ID
        if (component.id.isBlank()) {
            issues.add(ValidationIssue(
                message = "Component has empty ID",
                componentId = component.id,
                field = "id",
                severity = IssueSeverity.WARNING,
                suggestion = "Consider adding a meaningful ID for better debugging"
            ))
        }

        // Validate style properties
        component.style?.let { style ->
            validateStyle(style, component.id, issues)
            
            // Validate numeric properties that should be positive
            style.fontSize?.let { fontSize ->
                if (fontSize <= 0) {
                    issues.add(ValidationIssue(
                        message = "Font size must be positive",
                        componentId = component.id,
                        field = "style.fontSize",
                        value = fontSize.toString()
                    ))
                }
            }
            
            style.cornerRadius?.let { radius ->
                if (radius < 0) {
                    issues.add(ValidationIssue(
                        message = "Corner radius cannot be negative",
                        componentId = component.id,
                        field = "style.cornerRadius",
                        value = radius.toString()
                    ))
                }
            }
            
            style.borderWidth?.let { width ->
                if (width < 0) {
                    issues.add(ValidationIssue(
                        message = "Border width cannot be negative",
                        componentId = component.id,
                        field = "style.borderWidth",
                        value = width.toString()
                    ))
                }
            }
            
            style.shadowRadius?.let { radius ->
                if (radius < 0) {
                    issues.add(ValidationIssue(
                        message = "Shadow radius cannot be negative",
                        componentId = component.id,
                        field = "style.shadowRadius",
                        value = radius.toString()
                    ))
                }
            }
            
            style.opacity?.let { opacity ->
                if (opacity < 0 || opacity > 1) {
                    issues.add(ValidationIssue(
                        message = "Opacity must be between 0 and 1",
                        componentId = component.id,
                        field = "style.opacity",
                        value = opacity.toString()
                    ))
                }
            }
            
            style.scale?.let { scale ->
                if (scale <= 0) {
                    issues.add(ValidationIssue(
                        message = "Scale must be positive",
                        componentId = component.id,
                        field = "style.scale",
                        value = scale.toString()
                    ))
                }
            }
        }
    }

    private fun validateStyle(style: Style, componentId: String?, issues: MutableList<ValidationIssue>) {
        val idPrefix = componentId?.let { "[$it] " } ?: ""

        // Validate width and height values
        style.width?.let { width ->
            if (!isValidDimension(width)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style width must be a valid dimension",
                    componentId = componentId,
                    field = "style.width",
                    value = width
                ))
            }
        }

        style.height?.let { height ->
            if (!isValidDimension(height)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style height must be a valid dimension",
                    componentId = componentId,
                    field = "style.height",
                    value = height
                ))
            }
        }

        // Validate max/min width and height values
        style.maxWidth?.let { maxWidth ->
            if (!isValidDimension(maxWidth)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style maxWidth must be a valid dimension",
                    componentId = componentId,
                    field = "style.maxWidth",
                    value = maxWidth
                ))
            }
        }

        style.maxHeight?.let { maxHeight ->
            if (!isValidDimension(maxHeight)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style maxHeight must be a valid dimension",
                    componentId = componentId,
                    field = "style.maxHeight",
                    value = maxHeight
                ))
            }
        }

        style.minWidth?.let { minWidth ->
            if (!isValidDimension(minWidth)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style minWidth must be a valid dimension",
                    componentId = componentId,
                    field = "style.minWidth",
                    value = minWidth
                ))
            }
        }

        style.minHeight?.let { minHeight ->
            if (!isValidDimension(minHeight)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style minHeight must be a valid dimension",
                    componentId = componentId,
                    field = "style.minHeight",
                    value = minHeight
                ))
            }
        }

        // Validate color values
        style.backgroundColor?.let { color ->
            if (!isValidColor(color)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style backgroundColor must be a valid color",
                    componentId = componentId,
                    field = "style.backgroundColor",
                    value = color
                ))
            }
        }

        style.textColor?.let { color ->
            if (!isValidColor(color)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style textColor must be a valid color",
                    componentId = componentId,
                    field = "style.textColor",
                    value = color
                ))
            }
        }

        style.borderColor?.let { color ->
            if (!isValidColor(color)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style borderColor must be a valid color",
                    componentId = componentId,
                    field = "style.borderColor",
                    value = color
                ))
            }
        }

        style.shadowColor?.let { color ->
            if (!isValidColor(color)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style shadowColor must be a valid color",
                    componentId = componentId,
                    field = "style.shadowColor",
                    value = color
                ))
            }
        }

        // Validate fontWeight values
        style.fontWeight?.let { fontWeight ->
            if (!isValidFontWeight(fontWeight)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style fontWeight must be a valid font weight",
                    componentId = componentId,
                    field = "style.fontWeight",
                    value = fontWeight
                ))
            }
        }

        // Validate alignment values
        style.alignment?.let { alignment ->
            if (!isValidAlignment(alignment)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style alignment must be a valid alignment value",
                    componentId = componentId,
                    field = "style.alignment",
                    value = alignment
                ))
            }
        }

        // Validate flexDirection values
        style.flexDirection?.let { flexDirection ->
            if (!isValidFlexDirection(flexDirection)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style flexDirection must be a valid flex direction value",
                    componentId = componentId,
                    field = "style.flexDirection",
                    value = flexDirection
                ))
            }
        }

        // Validate justifyContent values
        style.justifyContent?.let { justifyContent ->
            if (!isValidJustifyContent(justifyContent)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style justifyContent must be a valid justify content value",
                    componentId = componentId,
                    field = "style.justifyContent",
                    value = justifyContent
                ))
            }
        }

        // Validate alignItems values
        style.alignItems?.let { alignItems ->
            if (!isValidAlignItems(alignItems)) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style alignItems must be a valid align items value",
                    componentId = componentId,
                    field = "style.alignItems",
                    value = alignItems
                ))
            }
        }

        // Validate numeric properties
        style.flex?.let { flex ->
            if (flex < 0) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style flex cannot be negative",
                    componentId = componentId,
                    field = "style.flex",
                    value = flex.toString()
                ))
            }
        }

        style.zIndex?.let { zIndex ->
            if (zIndex < 0) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style zIndex cannot be negative",
                    componentId = componentId,
                    field = "style.zIndex",
                    value = zIndex.toString()
                ))
            }
        }

        style.rotation?.let { rotation ->
            if (rotation < -360 || rotation > 360) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style rotation should be between -360 and 360 degrees",
                    componentId = componentId,
                    field = "style.rotation",
                    severity = IssueSeverity.WARNING,
                    suggestion = "Consider using values between -360 and 360 for better UX"
                ))
            }
        }

        // Validate shadow offset values
        style.shadowOffsetX?.let { offsetX ->
            if (offsetX < -100 || offsetX > 100) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style shadowOffsetX should be between -100 and 100",
                    componentId = componentId,
                    field = "style.shadowOffsetX",
                    severity = IssueSeverity.WARNING,
                    suggestion = "Consider using values between -100 and 100 for realistic shadows"
                ))
            }
        }

        style.shadowOffsetY?.let { offsetY ->
            if (offsetY < -100 || offsetY > 100) {
                issues.add(ValidationIssue(
                    message = "${idPrefix}Style shadowOffsetY should be between -100 and 100",
                    componentId = componentId,
                    field = "style.shadowOffsetY",
                    severity = IssueSeverity.WARNING,
                    suggestion = "Consider using values between -100 and 100 for realistic shadows"
                ))
            }
        }

        // Validate padding and margin
        style.padding?.let { padding ->
            validatePadding(padding, componentId, issues)
        }

        style.margin?.let { margin ->
            validateMargin(margin, componentId, issues)
        }
    }

    private fun validateTextComponent(component: SduiComponent.TextComponent, issues: MutableList<ValidationIssue>) {
        if (component.text.isBlank()) {
            issues.add(ValidationIssue(
                message = "Text component has empty text content",
                componentId = component.id,
                field = "text",
                severity = IssueSeverity.WARNING,
                suggestion = "Consider adding meaningful text or removing the component"
            ))
        }
        
        // Validate maxLines
        component.maxLines?.let { maxLines ->
            if (maxLines <= 0) {
                issues.add(ValidationIssue(
                    message = "Text maxLines must be positive",
                    componentId = component.id,
                    field = "maxLines",
                    value = maxLines.toString()
                ))
            }
        }
        
        // Validate textOverflow
        component.textOverflow?.let { textOverflow ->
            if (!isValidTextOverflow(textOverflow)) {
                issues.add(ValidationIssue(
                    message = "Text textOverflow must be a valid overflow value",
                    componentId = component.id,
                    field = "textOverflow",
                    value = textOverflow
                ))
            }
        }
    }

    private fun validateButtonComponent(component: SduiComponent.ButtonComponent, issues: MutableList<ValidationIssue>) {
        if (component.text.isBlank()) {
            issues.add(ValidationIssue(
                message = "Button must have text content",
                componentId = component.id,
                field = "text"
            ))
        }
    }

    private fun validateColumnComponent(component: SduiComponent.ColumnComponent, issues: MutableList<ValidationIssue>) {
        if (component.children.isEmpty()) {
            issues.add(ValidationIssue(
                message = "Column component has no children",
                componentId = component.id,
                field = "children",
                severity = IssueSeverity.WARNING,
                suggestion = "Consider adding child components or removing the empty column"
            ))
        } else {
            // Recursively validate all children
            component.children.forEach { child ->
                val childResult = validate(child)
                issues.addAll(childResult.issues)
            }
        }
        
        // Validate spacing
        component.spacing?.let { spacing ->
            if (spacing < 0) {
                issues.add(ValidationIssue(
                    message = "Column spacing cannot be negative",
                    componentId = component.id,
                    field = "spacing",
                    value = spacing.toString()
                ))
            }
        }
    }

    private fun validateRowComponent(component: SduiComponent.RowComponent, issues: MutableList<ValidationIssue>) {
        if (component.children.isEmpty()) {
            issues.add(ValidationIssue(
                message = "Row component has no children",
                componentId = component.id,
                field = "children",
                severity = IssueSeverity.WARNING,
                suggestion = "Consider adding child components or removing the empty row"
            ))
        } else {
            // Recursively validate all children
            component.children.forEach { child ->
                val childResult = validate(child)
                issues.addAll(childResult.issues)
            }
        }
        
        // Validate spacing
        component.spacing?.let { spacing ->
            if (spacing < 0) {
                issues.add(ValidationIssue(
                    message = "Row spacing cannot be negative",
                    componentId = component.id,
                    field = "spacing",
                    value = spacing.toString()
                ))
            }
        }
    }

    // Component validation methods with recursive validation for children
    private fun validateImageComponent(component: SduiComponent.ImageComponent, issues: MutableList<ValidationIssue>) {
        // Validate image-specific properties
        if (component.url.isBlank()) {
            issues.add(ValidationIssue(
                message = "Image component must have a valid URL",
                componentId = component.id,
                field = "url"
            ))
        }
    }
    
    private fun validateTextFieldComponent(component: SduiComponent.TextFieldComponent, issues: MutableList<ValidationIssue>) {
        // Validate text field-specific properties
        component.maxLines?.let { maxLines ->
            if (maxLines <= 0) {
                issues.add(ValidationIssue(
                    message = "Max lines must be positive",
                    componentId = component.id,
                    field = "maxLines",
                    value = maxLines.toString()
                ))
            }
        }
        
        // Validate keyboardType
        component.keyboardType?.let { keyboardType ->
            if (!isValidKeyboardType(keyboardType)) {
                issues.add(ValidationIssue(
                    message = "TextField keyboardType must be a valid keyboard type",
                    componentId = component.id,
                    field = "keyboardType",
                    value = keyboardType
                ))
            }
        }
        
        // Validate placeholder
        if (component.placeholder.isNullOrBlank()) {
            issues.add(ValidationIssue(
                message = "TextField has no placeholder text",
                componentId = component.id,
                field = "placeholder",
                severity = IssueSeverity.WARNING,
                suggestion = "Consider adding placeholder text for better UX"
            ))
        }
    }
    
    private fun validateSpacerComponent(component: SduiComponent.SpacerComponent, issues: MutableList<ValidationIssue>) {
        // Validate spacer-specific properties
        component.width?.let { width ->
            if (width < 0) {
                issues.add(ValidationIssue(
                    message = "Spacer width cannot be negative",
                    componentId = component.id,
                    field = "width",
                    value = width.toString()
                ))
            }
        }
        
        component.height?.let { height ->
            if (height < 0) {
                issues.add(ValidationIssue(
                    message = "Spacer height cannot be negative",
                    componentId = component.id,
                    field = "height",
                    value = height.toString()
                ))
            }
        }
    }
    
    private fun validateDividerComponent(component: SduiComponent.DividerComponent, issues: MutableList<ValidationIssue>) {
        // Validate divider-specific properties
        component.thickness?.let { thickness ->
            if (thickness < 0) {
                issues.add(ValidationIssue(
                    message = "Divider thickness cannot be negative",
                    componentId = component.id,
                    field = "thickness",
                    value = thickness.toString()
                ))
            }
        }
        
        // Validate color
        component.color?.let { color ->
            if (!isValidColor(color)) {
                issues.add(ValidationIssue(
                    message = "Divider color must be a valid color",
                    componentId = component.id,
                    field = "color",
                    value = color
                ))
            }
        }
    }
    
    private fun validateBoxComponent(component: SduiComponent.BoxComponent, issues: MutableList<ValidationIssue>) {
        if (component.children.isEmpty()) {
            issues.add(ValidationIssue(
                message = "Box component has no children",
                componentId = component.id,
                field = "children",
                severity = IssueSeverity.WARNING,
                suggestion = "Consider adding child components or removing the empty box"
            ))
        } else {
            // Recursively validate all children
            component.children.forEach { child ->
                val childResult = validate(child)
                issues.addAll(childResult.issues)
            }
        }
    }
    
    private fun validateCardComponent(component: SduiComponent.CardComponent, issues: MutableList<ValidationIssue>) {
        if (component.children.isEmpty()) {
            issues.add(ValidationIssue(
                message = "Card component has no children",
                componentId = component.id,
                field = "children",
                severity = IssueSeverity.WARNING,
                suggestion = "Consider adding child components or removing the empty card"
            ))
        } else {
            // Recursively validate all children
            component.children.forEach { child ->
                val childResult = validate(child)
                issues.addAll(childResult.issues)
            }
        }
        
        // Validate card-specific properties
        component.elevation?.let { elevation ->
            if (elevation < 0) {
                issues.add(ValidationIssue(
                    message = "Card elevation cannot be negative",
                    componentId = component.id,
                    field = "elevation",
                    value = elevation.toString()
                ))
            }
        }
    }
    
    private fun validateListComponent(component: SduiComponent.ListComponent, issues: MutableList<ValidationIssue>) {
        if (component.items.isEmpty()) {
            issues.add(ValidationIssue(
                message = "List component has no items",
                componentId = component.id,
                field = "items",
                severity = IssueSeverity.WARNING,
                suggestion = "Consider adding items or removing the empty list"
            ))
        } else {
            // Recursively validate all items
            component.items.forEach { item ->
                val itemResult = validate(item)
                issues.addAll(itemResult.issues)
            }
        }
        
        // Validate list-specific properties
        component.itemSpacing?.let { spacing ->
            if (spacing < 0) {
                issues.add(ValidationIssue(
                    message = "List item spacing cannot be negative",
                    componentId = component.id,
                    field = "itemSpacing",
                    value = spacing.toString()
                ))
            }
        }
    }
    
    private fun validateGridComponent(component: SduiComponent.GridComponent, issues: MutableList<ValidationIssue>) {
        if (component.items.isEmpty()) {
            issues.add(ValidationIssue(
                message = "Grid component has no items",
                componentId = component.id,
                field = "items",
                severity = IssueSeverity.WARNING,
                suggestion = "Consider adding items or removing the empty grid"
            ))
        } else {
            // Recursively validate all items
            component.items.forEach { item ->
                val itemResult = validate(item)
                issues.addAll(itemResult.issues)
            }
        }
        
        // Validate grid-specific properties
        if (component.columns <= 0) {
            issues.add(ValidationIssue(
                message = "Grid columns must be positive",
                componentId = component.id,
                field = "columns",
                value = component.columns.toString()
            ))
        }
        
        component.itemSpacing?.let { spacing ->
            if (spacing < 0) {
                issues.add(ValidationIssue(
                    message = "Grid item spacing cannot be negative",
                    componentId = component.id,
                    field = "itemSpacing",
                    value = spacing.toString()
                ))
            }
        }
    }
    
    private fun validateSwitchComponent(component: SduiComponent.SwitchComponent, issues: MutableList<ValidationIssue>) {
        // Validate switch-specific properties
        if (component.label.isNullOrBlank()) {
            issues.add(ValidationIssue(
                message = "Switch component has no label",
                componentId = component.id,
                field = "label",
                severity = IssueSeverity.WARNING,
                suggestion = "Consider adding a label for better accessibility"
            ))
        }
    }
    
    private fun validateCheckboxComponent(component: SduiComponent.CheckboxComponent, issues: MutableList<ValidationIssue>) {
        // Validate checkbox-specific properties
        if (component.label.isNullOrBlank()) {
            issues.add(ValidationIssue(
                message = "Checkbox component has no label",
                componentId = component.id,
                field = "label",
                severity = IssueSeverity.WARNING,
                suggestion = "Consider adding a label for better accessibility"
            ))
        }
    }
    
    private fun validateRadioButtonComponent(component: SduiComponent.RadioButtonComponent, issues: MutableList<ValidationIssue>) {
        // Validate radio button-specific properties
        if (component.label.isNullOrBlank()) {
            issues.add(ValidationIssue(
                message = "Radio button component has no label",
                componentId = component.id,
                field = "label",
                severity = IssueSeverity.WARNING,
                suggestion = "Consider adding a label for better accessibility"
            ))
        }
        
        if (component.group.isNullOrBlank()) {
            issues.add(ValidationIssue(
                message = "Radio button component has no group",
                componentId = component.id,
                field = "group",
                severity = IssueSeverity.WARNING,
                suggestion = "Consider adding a group for proper radio button behavior"
            ))
        }
    }
    
    private fun validateProgressBarComponent(component: SduiComponent.ProgressBarComponent, issues: MutableList<ValidationIssue>) {
        // Validate progress bar-specific properties
        if (component.progress < 0 || component.progress > 1) {
            issues.add(ValidationIssue(
                message = "Progress must be between 0 and 1",
                componentId = component.id,
                field = "progress",
                value = component.progress.toString()
            ))
        }
    }
    
    private fun validateSliderComponent(component: SduiComponent.SliderComponent, issues: MutableList<ValidationIssue>) {
        // Validate slider-specific properties
        if (component.minValue >= component.maxValue) {
            issues.add(ValidationIssue(
                message = "Slider minValue must be less than maxValue",
                componentId = component.id,
                field = "minValue/maxValue",
                value = "${component.minValue}/${component.maxValue}"
            ))
        }
        
        if (component.value < component.minValue || component.value > component.maxValue) {
            issues.add(ValidationIssue(
                message = "Slider value must be between minValue and maxValue",
                componentId = component.id,
                field = "value",
                value = component.value.toString()
            ))
        }
        
        component.step?.let { step ->
            if (step <= 0) {
                issues.add(ValidationIssue(
                    message = "Slider step must be positive",
                    componentId = component.id,
                    field = "step",
                    value = step.toString()
                ))
            }
        }
    }
    
    private fun validateChipComponent(component: SduiComponent.ChipComponent, issues: MutableList<ValidationIssue>) {
        // Validate chip-specific properties
        if (component.text.isBlank()) {
            issues.add(ValidationIssue(
                message = "Chip component must have text content",
                componentId = component.id,
                field = "text"
            ))
        }
    }

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
    
    private fun isValidAlignment(alignment: String): Boolean {
        return when (alignment.lowercase()) {
            "start", "end", "center", "justify", "left", "right" -> true
            else -> false
        }
    }
    
    private fun isValidFlexDirection(flexDirection: String): Boolean {
        return when (flexDirection.lowercase()) {
            "row", "column", "row-reverse", "column-reverse" -> true
            else -> false
        }
    }
    
    private fun isValidJustifyContent(justifyContent: String): Boolean {
        return when (justifyContent.lowercase()) {
            "start", "end", "center", "space-between", "space-around", "space-evenly", "flex-start", "flex-end" -> true
            else -> false
        }
    }
    
    private fun isValidAlignItems(alignItems: String): Boolean {
        return when (alignItems.lowercase()) {
            "start", "end", "center", "stretch", "baseline", "flex-start", "flex-end" -> true
            else -> false
        }
    }
    
    private fun isValidTextOverflow(textOverflow: String): Boolean {
        return when (textOverflow.lowercase()) {
            "clip", "ellipsis", "fade", "visible" -> true
            else -> false
        }
    }
    
    private fun isValidKeyboardType(keyboardType: String): Boolean {
        return when (keyboardType.lowercase()) {
            "default", "number", "email", "phone", "url", "decimal", "numeric", "text" -> true
            else -> false
        }
    }
    
    private fun validatePadding(padding: Padding, componentId: String?, issues: MutableList<ValidationIssue>) {
        val idPrefix = componentId?.let { "[$it] " } ?: ""
        
        // Validate all padding values are non-negative
        listOf(
            padding.top to "top",
            padding.bottom to "bottom",
            padding.start to "start",
            padding.end to "end",
            padding.horizontal to "horizontal",
            padding.vertical to "vertical",
            padding.all to "all"
        ).forEach { (value, field) ->
            value?.let { paddingValue ->
                if (paddingValue < 0) {
                    issues.add(ValidationIssue(
                        message = "${idPrefix}Padding $field cannot be negative",
                        componentId = componentId,
                        field = "style.padding.$field",
                        value = paddingValue.toString()
                    ))
                }
            }
        }
    }
    
    private fun validateMargin(margin: Margin, componentId: String?, issues: MutableList<ValidationIssue>) {
        val idPrefix = componentId?.let { "[$it] " } ?: ""
        
        // Validate all margin values are non-negative
        listOf(
            margin.top to "top",
            margin.bottom to "bottom",
            margin.start to "start",
            margin.end to "end",
            margin.horizontal to "horizontal",
            margin.vertical to "vertical",
            margin.all to "all"
        ).forEach { (value, field) ->
            value?.let { marginValue ->
                if (marginValue < 0) {
                    issues.add(ValidationIssue(
                        message = "${idPrefix}Margin $field cannot be negative",
                        componentId = componentId,
                        field = "style.margin.$field",
                        value = marginValue.toString()
                    ))
                }
            }
        }
    }
}

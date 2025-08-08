package com.amnix.sdui.sdui.validation

import com.amnix.sdui.sdui.components.SduiComponent
import com.amnix.sdui.sdui.model.Style
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SduiValidatorTest {
    
    private val validator = DefaultSduiValidator()
    
    @Test
    fun `test valid text component`() {
        val component = SduiComponent.TextComponent(
            id = "test_text",
            text = "Hello World"
        )
        
        val errors = validator.validate(component)
        assertTrue(errors.isEmpty(), "Valid text component should have no errors")
        assertTrue(component.isValid())
    }
    
    @Test
    fun `test invalid text component with blank text`() {
        val component = SduiComponent.TextComponent(
            id = "test_text",
            text = ""
        )
        
        val errors = validator.validate(component)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("Text component text must not be blank"))
        assertFalse(component.isValid())
    }
    
    @Test
    fun `test valid button component`() {
        val component = SduiComponent.ButtonComponent(
            id = "test_button",
            text = "Click me"
        )
        
        val errors = validator.validate(component)
        assertTrue(errors.isEmpty(), "Valid button component should have no errors")
        assertTrue(component.isValid())
    }
    
    @Test
    fun `test invalid button component with blank text`() {
        val component = SduiComponent.ButtonComponent(
            id = "test_button",
            text = "   "
        )
        
        val errors = validator.validate(component)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("Button component text must not be blank"))
        assertFalse(component.isValid())
    }
    
    @Test
    fun `test valid image component with http url`() {
        val component = SduiComponent.ImageComponent(
            id = "test_image",
            url = "https://example.com/image.jpg"
        )
        
        val errors = validator.validate(component)
        assertTrue(errors.isEmpty(), "Valid image component should have no errors")
        assertTrue(component.isValid())
    }
    
    @Test
    fun `test valid image component with data url`() {
        val component = SduiComponent.ImageComponent(
            id = "test_image",
            url = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=="
        )
        
        val errors = validator.validate(component)
        assertTrue(errors.isEmpty(), "Valid image component with data URL should have no errors")
        assertTrue(component.isValid())
    }
    
    @Test
    fun `test invalid image component with invalid url`() {
        val component = SduiComponent.ImageComponent(
            id = "test_image",
            url = "ftp://example.com/image.jpg"
        )
        
        val errors = validator.validate(component)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("Image component URL must be a valid HTTP(S) URL"))
        assertFalse(component.isValid())
    }
    
    @Test
    fun `test invalid image component with blank url`() {
        val component = SduiComponent.ImageComponent(
            id = "test_image",
            url = ""
        )
        
        val errors = validator.validate(component)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("Image component URL must not be blank"))
        assertFalse(component.isValid())
    }
    
    @Test
    fun `test valid text field component`() {
        val component = SduiComponent.TextFieldComponent(
            id = "test_field",
            placeholder = "Enter text"
        )
        
        val errors = validator.validate(component)
        assertTrue(errors.isEmpty(), "Valid text field component should have no errors")
        assertTrue(component.isValid())
    }
    
    @Test
    fun `test invalid text field component with password and pre-filled value`() {
        val component = SduiComponent.TextFieldComponent(
            id = "test_field",
            isPassword = true,
            value = "secret123"
        )
        
        val errors = validator.validate(component)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("TextField with isPassword=true should not have a pre-filled value for security"))
        assertFalse(component.isValid())
    }
    
    @Test
    fun `test valid text field component with password and no value`() {
        val component = SduiComponent.TextFieldComponent(
            id = "test_field",
            isPassword = true,
            value = null
        )
        
        val errors = validator.validate(component)
        assertTrue(errors.isEmpty(), "Valid password field with no value should have no errors")
        assertTrue(component.isValid())
    }
    
    @Test
    fun `test valid spacer component with width`() {
        val component = SduiComponent.SpacerComponent(
            id = "test_spacer",
            width = 100f
        )
        
        val errors = validator.validate(component)
        assertTrue(errors.isEmpty(), "Valid spacer component with width should have no errors")
        assertTrue(component.isValid())
    }
    
    @Test
    fun `test valid spacer component with height`() {
        val component = SduiComponent.SpacerComponent(
            id = "test_spacer",
            height = 50f
        )
        
        val errors = validator.validate(component)
        assertTrue(errors.isEmpty(), "Valid spacer component with height should have no errors")
        assertTrue(component.isValid())
    }
    
    @Test
    fun `test invalid spacer component with no dimensions`() {
        val component = SduiComponent.SpacerComponent(
            id = "test_spacer"
        )
        
        val errors = validator.validate(component)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("Spacer component must have at least one of width or height set"))
        assertFalse(component.isValid())
    }
    
    @Test
    fun `test valid divider component`() {
        val component = SduiComponent.DividerComponent(
            id = "test_divider",
            thickness = 2f
        )
        
        val errors = validator.validate(component)
        assertTrue(errors.isEmpty(), "Valid divider component should have no errors")
        assertTrue(component.isValid())
    }
    
    @Test
    fun `test invalid divider component with zero thickness`() {
        val component = SduiComponent.DividerComponent(
            id = "test_divider",
            thickness = 0f
        )
        
        val errors = validator.validate(component)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("Divider thickness must be > 0"))
        assertFalse(component.isValid())
    }
    
    @Test
    fun `test invalid divider component with negative thickness`() {
        val component = SduiComponent.DividerComponent(
            id = "test_divider",
            thickness = -1f
        )
        
        val errors = validator.validate(component)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("Divider thickness must be > 0"))
        assertFalse(component.isValid())
    }
    
    @Test
    fun `test valid layout components with children`() {
        val child = SduiComponent.TextComponent(
            id = "child_text",
            text = "Child text"
        )
        
        val column = SduiComponent.ColumnComponent(
            id = "test_column",
            children = listOf(child)
        )
        
        val row = SduiComponent.RowComponent(
            id = "test_row",
            children = listOf(child)
        )
        
        val box = SduiComponent.BoxComponent(
            id = "test_box",
            children = listOf(child)
        )
        
        val card = SduiComponent.CardComponent(
            id = "test_card",
            children = listOf(child)
        )
        
        assertTrue(column.isValid())
        assertTrue(row.isValid())
        assertTrue(box.isValid())
        assertTrue(card.isValid())
    }
    
    @Test
    fun `test invalid layout components without children`() {
        val column = SduiComponent.ColumnComponent(id = "test_column")
        val row = SduiComponent.RowComponent(id = "test_row")
        val box = SduiComponent.BoxComponent(id = "test_box")
        val card = SduiComponent.CardComponent(id = "test_card")
        
        assertFalse(column.isValid())
        assertFalse(row.isValid())
        assertFalse(box.isValid())
        assertFalse(card.isValid())
        
        assertEquals(1, column.getValidationErrors().size)
        assertEquals(1, row.getValidationErrors().size)
        assertEquals(1, box.getValidationErrors().size)
        assertEquals(1, card.getValidationErrors().size)
    }
    
    @Test
    fun `test recursive validation with nested invalid components`() {
        val invalidChild = SduiComponent.TextComponent(
            id = "invalid_child",
            text = ""
        )
        val column = SduiComponent.ColumnComponent(
            id = "test_column",
            children = listOf(invalidChild)
        )
        
        val errors = validator.validate(column)
        assertEquals(1, errors.size) // One for invalid child
        assertTrue(errors.any { it.contains("Child[0]:") && it.contains("Text component text must not be blank") })
        assertFalse(column.isValid())
    }
    
    @Test
    fun `test valid list component`() {
        val item = SduiComponent.TextComponent(
            id = "list_item",
            text = "List item"
        )
        val list = SduiComponent.ListComponent(
            id = "test_list",
            items = listOf(item)
        )
        
        val errors = validator.validate(list)
        assertTrue(errors.isEmpty(), "Valid list component should have no errors")
        assertTrue(list.isValid())
    }
    
    @Test
    fun `test invalid list component without items`() {
        val list = SduiComponent.ListComponent(id = "test_list")
        
        val errors = validator.validate(list)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("List component must have at least one item"))
        assertFalse(list.isValid())
    }
    
    @Test
    fun `test valid grid component`() {
        val item = SduiComponent.TextComponent(
            id = "grid_item",
            text = "Grid item"
        )
        val grid = SduiComponent.GridComponent(
            id = "test_grid",
            columns = 2,
            items = listOf(item)
        )
        
        val errors = validator.validate(grid)
        assertTrue(errors.isEmpty(), "Valid grid component should have no errors")
        assertTrue(grid.isValid())
    }
    
    @Test
    fun `test invalid grid component with zero columns`() {
        val item = SduiComponent.TextComponent(
            id = "grid_item",
            text = "Grid item"
        )
        val grid = SduiComponent.GridComponent(
            id = "test_grid",
            columns = 0,
            items = listOf(item)
        )
        
        val errors = validator.validate(grid)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("Grid component columns must be > 0"))
        assertFalse(grid.isValid())
    }
    
    @Test
    fun `test valid switch component`() {
        val switch = SduiComponent.SwitchComponent(
            id = "test_switch",
            checked = true,
            label = "Toggle me"
        )
        
        val errors = validator.validate(switch)
        assertTrue(errors.isEmpty(), "Valid switch component should have no errors")
        assertTrue(switch.isValid())
    }
    
    @Test
    fun `test valid checkbox component`() {
        val checkbox = SduiComponent.CheckboxComponent(
            id = "test_checkbox",
            checked = false,
            label = "Check me"
        )
        
        val errors = validator.validate(checkbox)
        assertTrue(errors.isEmpty(), "Valid checkbox component should have no errors")
        assertTrue(checkbox.isValid())
    }
    
    @Test
    fun `test valid radio button component with group`() {
        val radio = SduiComponent.RadioButtonComponent(
            id = "test_radio",
            selected = true,
            label = "Option A",
            group = "test_group"
        )
        
        val errors = validator.validate(radio)
        assertTrue(errors.isEmpty(), "Valid radio button component should have no errors")
        assertTrue(radio.isValid())
    }
    
    @Test
    fun `test invalid radio button component without group`() {
        val radio = SduiComponent.RadioButtonComponent(
            id = "test_radio",
            selected = true,
            label = "Option A"
        )
        
        val errors = validator.validate(radio)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("RadioButton component should have a group name for proper selection behavior"))
        assertFalse(radio.isValid())
    }
    
    @Test
    fun `test valid progress bar component`() {
        val progressBar = SduiComponent.ProgressBarComponent(
            id = "test_progress",
            progress = 0.5f,
            label = "Loading..."
        )
        
        val errors = validator.validate(progressBar)
        assertTrue(errors.isEmpty(), "Valid progress bar component should have no errors")
        assertTrue(progressBar.isValid())
    }
    
    @Test
    fun `test invalid progress bar component with invalid progress`() {
        val progressBar = SduiComponent.ProgressBarComponent(
            id = "test_progress",
            progress = 1.5f
        )
        
        val errors = validator.validate(progressBar)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("ProgressBar progress must be between 0 and 1"))
        assertFalse(progressBar.isValid())
    }
    
    @Test
    fun `test valid slider component`() {
        val slider = SduiComponent.SliderComponent(
            id = "test_slider",
            value = 50f,
            minValue = 0f,
            maxValue = 100f,
            step = 5f,
            label = "Volume"
        )
        
        val errors = validator.validate(slider)
        assertTrue(errors.isEmpty(), "Valid slider component should have no errors")
        assertTrue(slider.isValid())
    }
    
    @Test
    fun `test invalid slider component with invalid range`() {
        val slider = SduiComponent.SliderComponent(
            id = "test_slider",
            value = 50f,
            minValue = 100f,
            maxValue = 0f
        )
        
        val errors = validator.validate(slider)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("Slider minValue must be < maxValue"))
        assertFalse(slider.isValid())
    }
    
    @Test
    fun `test invalid slider component with value out of range`() {
        val slider = SduiComponent.SliderComponent(
            id = "test_slider",
            value = 150f,
            minValue = 0f,
            maxValue = 100f
        )
        
        val errors = validator.validate(slider)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("Slider value must be between minValue and maxValue"))
        assertFalse(slider.isValid())
    }
    
    @Test
    fun `test invalid slider component with invalid step`() {
        val slider = SduiComponent.SliderComponent(
            id = "test_slider",
            value = 50f,
            minValue = 0f,
            maxValue = 100f,
            step = 0f
        )
        
        val errors = validator.validate(slider)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("Slider step must be > 0"))
        assertFalse(slider.isValid())
    }
    
    @Test
    fun `test valid chip component`() {
        val chip = SduiComponent.ChipComponent(
            id = "test_chip",
            text = "Technology",
            selected = true,
            icon = "🔧"
        )
        
        val errors = validator.validate(chip)
        assertTrue(errors.isEmpty(), "Valid chip component should have no errors")
        assertTrue(chip.isValid())
    }
    
    @Test
    fun `test invalid chip component with blank text`() {
        val chip = SduiComponent.ChipComponent(
            id = "test_chip",
            text = ""
        )
        
        val errors = validator.validate(chip)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("Chip component text must not be blank"))
        assertFalse(chip.isValid())
    }
    
    @Test
    fun `test style validation with negative dimensions`() {
        val style = Style(
            width = -10f,
            height = -5f,
            maxWidth = -20f,
            maxHeight = -15f,
            minWidth = -30f,
            minHeight = -25f,
            opacity = 1.5f,
            zIndex = -1
        )
        
        val component = SduiComponent.TextComponent(
            id = "test_style",
            text = "Test",
            style = style
        )
        
        val errors = validator.validate(component)
        assertEquals(8, errors.size) // All style validation errors
        assertTrue(errors.all { it.contains("Style") })
        assertFalse(component.isValid())
    }
    
    @Test
    fun `test multiple components validation`() {
        val components = listOf(
            SduiComponent.TextComponent(
                id = "valid_text",
                text = "Valid text"
            ),
            SduiComponent.TextComponent(
                id = "invalid_text",
                text = ""
            ), // Invalid
            SduiComponent.ButtonComponent(
                id = "valid_button",
                text = "Valid button"
            ),
            SduiComponent.ButtonComponent(
                id = "invalid_button",
                text = ""
            ) // Invalid
        )
        
        val errors = validator.validate(components)
        assertEquals(2, errors.size)
        assertTrue(errors.any { it.contains("Text component text must not be blank") })
        assertTrue(errors.any { it.contains("Button component text must not be blank") })
    }
    
    @Test
    fun `test component with id in error messages`() {
        val component = SduiComponent.TextComponent(
            id = "my_text_component",
            text = ""
        )
        
        val errors = validator.validate(component)
        assertEquals(1, errors.size)
        assertTrue(errors[0].contains("[my_text_component]"))
        assertTrue(errors[0].contains("Text component text must not be blank"))
    }
} 
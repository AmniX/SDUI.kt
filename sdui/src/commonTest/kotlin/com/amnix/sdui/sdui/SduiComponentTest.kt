package com.amnix.sdui.sdui

import com.amnix.sdui.sdui.components.SduiComponent
import com.amnix.sdui.sdui.model.SduiAction
import com.amnix.sdui.sdui.model.Style
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SduiComponentTest {
    @Test
    fun testTextComponentSerialization() {
        val json =
            """
            {
              "type": "Text",
              "id": "welcome_text",
              "text": "Welcome to SDUI",
              "style": {
                "fontSize": 18.0,
                "textColor": "#000000"
              },
              "visible": true
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("welcome_text", component.id)
        assertEquals(true, component.visible)

        when (component) {
            is SduiComponent.TextComponent -> {
                assertEquals("Welcome to SDUI", component.text)
                assertEquals(18.0f, component.style?.fontSize)
                assertEquals("#000000", component.style?.textColor)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }

    @Test
    fun testButtonComponentSerialization() {
        val json =
            """
            {
              "type": "Button",
              "id": "submit_button",
              "text": "Submit",
              "style": {
                "backgroundColor": "#007AFF",
                "textColor": "#FFFFFF",
                "cornerRadius": 8.0
              },
              "action": {
                "type": "navigate",
                "route": "/success"
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("submit_button", component.id)

        when (component) {
            is SduiComponent.ButtonComponent -> {
                assertEquals("Submit", component.text)
                assertEquals("#007AFF", component.style?.backgroundColor)
                assertEquals("#FFFFFF", component.style?.textColor)
                assertEquals(8.0f, component.style?.cornerRadius)

                when (val action = component.action) {
                    is SduiAction.Navigate -> {
                        assertEquals("/success", action.route)
                    }
                    else -> throw AssertionError("Expected Navigate action")
                }
            }
            else -> throw AssertionError("Expected ButtonComponent")
        }
    }

    @Test
    fun testColumnComponentSerialization() {
        val json =
            """
            {
              "type": "Column",
              "id": "main_column",
              "children": [
                {
                  "type": "Text",
                  "text": "Header"
                },
                {
                  "type": "Button",
                  "text": "Click me"
                }
              ],
              "spacing": 16.0
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("main_column", component.id)

        when (component) {
            is SduiComponent.ColumnComponent -> {
                assertEquals(2, component.children.size)
                assertEquals(16.0f, component.spacing)

                val textComponent = component.children[0]
                when (textComponent) {
                    is SduiComponent.TextComponent -> {
                        assertEquals("Header", textComponent.text)
                    }
                    else -> throw AssertionError("Expected TextComponent as first child")
                }

                val buttonComponent = component.children[1]
                when (buttonComponent) {
                    is SduiComponent.ButtonComponent -> {
                        assertEquals("Click me", buttonComponent.text)
                    }
                    else -> throw AssertionError("Expected ButtonComponent as second child")
                }
            }
            else -> throw AssertionError("Expected ColumnComponent")
        }
    }

    @Test
    fun testRowComponentSerialization() {
        val json =
            """
            {
              "type": "Row",
              "id": "header_row",
              "children": [
                {
                  "type": "Text",
                  "text": "Title"
                },
                {
                  "type": "Button",
                  "text": "Menu"
                }
              ],
              "spacing": 12.0
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("header_row", component.id)

        when (component) {
            is SduiComponent.RowComponent -> {
                assertEquals(2, component.children.size)
                assertEquals(12.0f, component.spacing)

                val textComponent = component.children[0]
                when (textComponent) {
                    is SduiComponent.TextComponent -> {
                        assertEquals("Title", textComponent.text)
                    }
                    else -> throw AssertionError("Expected TextComponent as first child")
                }

                val buttonComponent = component.children[1]
                when (buttonComponent) {
                    is SduiComponent.ButtonComponent -> {
                        assertEquals("Menu", buttonComponent.text)
                    }
                    else -> throw AssertionError("Expected ButtonComponent as second child")
                }
            }
            else -> throw AssertionError("Expected RowComponent")
        }
    }

    @Test
    fun testImageComponentSerialization() {
        val json =
            """
            {
              "type": "Image",
              "id": "profile_image",
              "url": "https://example.com/image.jpg",
              "altText": "Profile picture",
              "contentDescription": "User profile image",
              "style": {
                "width": 100.0,
                "height": 100.0,
                "cornerRadius": 50.0
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("profile_image", component.id)

        when (component) {
            is SduiComponent.ImageComponent -> {
                assertEquals("https://example.com/image.jpg", component.url)
                assertEquals("Profile picture", component.altText)
                assertEquals("User profile image", component.contentDescription)
                assertEquals(100.0f, component.style?.width)
                assertEquals(100.0f, component.style?.height)
                assertEquals(50.0f, component.style?.cornerRadius)
            }
            else -> throw AssertionError("Expected ImageComponent")
        }
    }

    @Test
    fun testTextFieldComponentSerialization() {
        val json =
            """
            {
              "type": "TextField",
              "id": "email_input",
              "placeholder": "Enter your email",
              "value": "user@example.com",
              "enabled": true,
              "keyboardType": "email",
              "maxLines": 1,
              "isPassword": false,
              "style": {
                "borderWidth": 1.0,
                "borderColor": "#CCCCCC"
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("email_input", component.id)

        when (component) {
            is SduiComponent.TextFieldComponent -> {
                assertEquals("Enter your email", component.placeholder)
                assertEquals("user@example.com", component.value)
                assertEquals(true, component.enabled)
                assertEquals("email", component.keyboardType)
                assertEquals(1, component.maxLines)
                assertEquals(false, component.isPassword)
                assertEquals(1.0f, component.style?.borderWidth)
                assertEquals("#CCCCCC", component.style?.borderColor)
            }
            else -> throw AssertionError("Expected TextFieldComponent")
        }
    }

    @Test
    fun testSpacerComponentSerialization() {
        val json =
            """
            {
              "type": "Spacer",
              "id": "vertical_spacer",
              "width": 0.0,
              "height": 20.0
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("vertical_spacer", component.id)

        when (component) {
            is SduiComponent.SpacerComponent -> {
                assertEquals(0.0f, component.width)
                assertEquals(20.0f, component.height)
            }
            else -> throw AssertionError("Expected SpacerComponent")
        }
    }

    @Test
    fun testDividerComponentSerialization() {
        val json =
            """
            {
              "type": "Divider",
              "id": "section_divider",
              "color": "#E0E0E0",
              "thickness": 1.0
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("section_divider", component.id)

        when (component) {
            is SduiComponent.DividerComponent -> {
                assertEquals("#E0E0E0", component.color)
                assertEquals(1.0f, component.thickness)
            }
            else -> throw AssertionError("Expected DividerComponent")
        }
    }

    @Test
    fun testBoxComponentSerialization() {
        val json =
            """
            {
              "type": "Box",
              "id": "container_box",
              "children": [
                {
                  "type": "Text",
                  "text": "Centered content"
                }
              ],
              "style": {
                "backgroundColor": "#F5F5F5",
                "padding": {
                  "all": 16.0
                }
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("container_box", component.id)

        when (component) {
            is SduiComponent.BoxComponent -> {
                assertEquals(1, component.children.size)
                assertEquals("#F5F5F5", component.style?.backgroundColor)
                assertEquals(16.0f, component.style?.padding?.all)

                val textComponent = component.children[0]
                when (textComponent) {
                    is SduiComponent.TextComponent -> {
                        assertEquals("Centered content", textComponent.text)
                    }
                    else -> throw AssertionError("Expected TextComponent as child")
                }
            }
            else -> throw AssertionError("Expected BoxComponent")
        }
    }

    @Test
    fun testCardComponentSerialization() {
        val json =
            """
            {
              "type": "Card",
              "id": "info_card",
              "children": [
                {
                  "type": "Text",
                  "text": "Card content"
                }
              ],
              "elevation": 4.0,
              "style": {
                "backgroundColor": "#FFFFFF",
                "cornerRadius": 8.0,
                "padding": {
                  "all": 16.0
                }
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("info_card", component.id)

        when (component) {
            is SduiComponent.CardComponent -> {
                assertEquals(1, component.children.size)
                assertEquals(4.0f, component.elevation)
                assertEquals("#FFFFFF", component.style?.backgroundColor)
                assertEquals(8.0f, component.style?.cornerRadius)
                assertEquals(16.0f, component.style?.padding?.all)

                val textComponent = component.children[0]
                when (textComponent) {
                    is SduiComponent.TextComponent -> {
                        assertEquals("Card content", textComponent.text)
                    }
                    else -> throw AssertionError("Expected TextComponent as child")
                }
            }
            else -> throw AssertionError("Expected CardComponent")
        }
    }

    @Test
    fun testApiCallActionSerialization() {
        val json =
            """
            {
              "type": "Button",
              "id": "api_button",
              "text": "Submit",
              "action": {
                "type": "api_call",
                "url": "/api/submit",
                "method": "POST",
                "headers": {
                  "Content-Type": "application/json"
                },
                "body": {
                  "key": "value"
                },
                "onSuccess": "navigate:/success",
                "onError": "show_dialog:Error occurred"
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.ButtonComponent -> {
                when (val action = component.action) {
                    is SduiAction.ApiCall -> {
                        assertEquals("/api/submit", action.url)
                        assertEquals("POST", action.method)
                        assertEquals("application/json", action.headers?.get("Content-Type"))
                        assertEquals("value", action.body?.get("key"))
                        assertEquals("navigate:/success", action.onSuccess)
                        assertEquals("show_dialog:Error occurred", action.onError)
                    }
                    else -> throw AssertionError("Expected ApiCall action")
                }
            }
            else -> throw AssertionError("Expected ButtonComponent")
        }
    }

    @Test
    fun testShowDialogActionSerialization() {
        val json =
            """
            {
              "type": "Button",
              "id": "info_button",
              "text": "Show Info",
              "action": {
                "type": "show_dialog",
                "title": "Information",
                "message": "This is an informational dialog",
                "type": "info"
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.ButtonComponent -> {
                when (val action = component.action) {
                    is SduiAction.ShowDialog -> {
                        assertEquals("Information", action.title)
                        assertEquals("This is an informational dialog", action.message)
                        assertEquals("info", action.type)
                    }
                    else -> throw AssertionError("Expected ShowDialog action")
                }
            }
            else -> throw AssertionError("Expected ButtonComponent")
        }
    }

    @Test
    fun testUpdateStateActionSerialization() {
        val json =
            """
            {
              "type": "Button",
              "id": "toggle_button",
              "text": "Toggle",
              "action": {
                "type": "update_state",
                "key": "isEnabled",
                "value": "true"
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.ButtonComponent -> {
                when (val action = component.action) {
                    is SduiAction.UpdateState -> {
                        assertEquals("isEnabled", action.key)
                        assertEquals("true", action.value)
                    }
                    else -> throw AssertionError("Expected UpdateState action")
                }
            }
            else -> throw AssertionError("Expected ButtonComponent")
        }
    }

    @Test
    fun testCustomActionSerialization() {
        val json =
            """
            {
              "type": "Button",
              "id": "custom_button",
              "text": "Custom Action",
              "action": {
                "type": "custom",
                "action": "analytics_track",
                "data": {
                  "event": "button_click",
                  "screen": "home"
                }
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.ButtonComponent -> {
                when (val action = component.action) {
                    is SduiAction.Custom -> {
                        assertEquals("analytics_track", action.action)
                        assertEquals("button_click", action.data?.get("event"))
                        assertEquals("home", action.data?.get("screen"))
                    }
                    else -> throw AssertionError("Expected Custom action")
                }
            }
            else -> throw AssertionError("Expected ButtonComponent")
        }
    }

    @Test
    fun testStyleSerialization() {
        val json =
            """
            {
              "type": "Text",
              "id": "styled_text",
              "text": "Styled text",
              "style": {
                "padding": {
                  "top": 8.0,
                  "bottom": 8.0,
                  "start": 16.0,
                  "end": 16.0,
                  "horizontal": 16.0,
                  "vertical": 8.0,
                  "all": 16.0
                },
                "margin": {
                  "top": 4.0,
                  "bottom": 4.0,
                  "start": 8.0,
                  "end": 8.0,
                  "horizontal": 8.0,
                  "vertical": 4.0,
                  "all": 8.0
                },
                "backgroundColor": "#F0F0F0",
                "cornerRadius": 4.0,
                "fontSize": 16.0,
                "fontWeight": "medium",
                "textColor": "#333333",
                "alignment": "center",
                "width": 200.0,
                "height": 40.0,
                "maxWidth": 300.0,
                "maxHeight": 100.0,
                "minWidth": 100.0,
                "minHeight": 20.0,
                "flex": 1.0,
                "flexDirection": "row",
                "justifyContent": "space-between",
                "alignItems": "center",
                "borderWidth": 1.0,
                "borderColor": "#CCCCCC",
                "shadowRadius": 2.0,
                "shadowColor": "#000000",
                "shadowOffsetX": 1.0,
                "shadowOffsetY": 1.0,
                "opacity": 0.9,
                "rotation": 0.0,
                "scale": 1.0,
                "zIndex": 1
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.TextComponent -> {
                val style = component.style
                assertNotNull(style)

                // Test padding
                assertEquals(8.0f, style.padding?.top)
                assertEquals(8.0f, style.padding?.bottom)
                assertEquals(16.0f, style.padding?.start)
                assertEquals(16.0f, style.padding?.end)
                assertEquals(16.0f, style.padding?.horizontal)
                assertEquals(8.0f, style.padding?.vertical)
                assertEquals(16.0f, style.padding?.all)

                // Test margin
                assertEquals(4.0f, style.margin?.top)
                assertEquals(4.0f, style.margin?.bottom)
                assertEquals(8.0f, style.margin?.start)
                assertEquals(8.0f, style.margin?.end)
                assertEquals(8.0f, style.margin?.horizontal)
                assertEquals(4.0f, style.margin?.vertical)
                assertEquals(8.0f, style.margin?.all)

                // Test other style properties
                assertEquals("#F0F0F0", style.backgroundColor)
                assertEquals(4.0f, style.cornerRadius)
                assertEquals(16.0f, style.fontSize)
                assertEquals("medium", style.fontWeight)
                assertEquals("#333333", style.textColor)
                assertEquals("center", style.alignment)
                assertEquals(200.0f, style.width)
                assertEquals(40.0f, style.height)
                assertEquals(300.0f, style.maxWidth)
                assertEquals(100.0f, style.maxHeight)
                assertEquals(100.0f, style.minWidth)
                assertEquals(20.0f, style.minHeight)
                assertEquals(1.0f, style.flex)
                assertEquals("row", style.flexDirection)
                assertEquals("space-between", style.justifyContent)
                assertEquals("center", style.alignItems)
                assertEquals(1.0f, style.borderWidth)
                assertEquals("#CCCCCC", style.borderColor)
                assertEquals(2.0f, style.shadowRadius)
                assertEquals("#000000", style.shadowColor)
                assertEquals(1.0f, style.shadowOffsetX)
                assertEquals(1.0f, style.shadowOffsetY)
                assertEquals(0.9f, style.opacity)
                assertEquals(0.0f, style.rotation)
                assertEquals(1.0f, style.scale)
                assertEquals(1, style.zIndex)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }

    @Test
    fun testComponentWithMetaData() {
        val json =
            """
            {
              "type": "Text",
              "id": "meta_text",
              "text": "Text with metadata",
              "meta": {
                "analytics_id": "welcome_text",
                "accessibility_label": "Welcome message",
                "test_id": "welcome_text_component"
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("meta_text", component.id)

        val meta = component.meta
        assertNotNull(meta)
        assertEquals("welcome_text", meta["analytics_id"])
        assertEquals("Welcome message", meta["accessibility_label"])
        assertEquals("welcome_text_component", meta["test_id"])
    }

    @Test
    fun testComponentWithNullValues() {
        val json =
            """
            {
              "type": "Text",
              "text": "Text with default values",
              "style": null,
              "action": null,
              "visible": null,
              "meta": null
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        // id will have a default value, not null
        assertNotNull(component.id)
        assertEquals(null, component.style)
        assertEquals(null, component.action)
        assertEquals(null, component.visible)
        assertEquals(null, component.meta)

        when (component) {
            is SduiComponent.TextComponent -> {
                assertEquals("Text with default values", component.text)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }

    @Test
    fun testComplexNestedStructure() {
        val json =
            """
            {
              "type": "Column",
              "id": "complex_layout",
              "children": [
                {
                  "type": "Row",
                  "id": "header_row",
                  "children": [
                    {
                      "type": "Text",
                      "text": "Header"
                    },
                    {
                      "type": "Button",
                      "text": "Action"
                    }
                  ]
                },
                {
                  "type": "Card",
                  "id": "content_card",
                  "children": [
                    {
                      "type": "Text",
                      "text": "Card content"
                    },
                    {
                      "type": "Image",
                      "url": "https://example.com/image.jpg"
                    }
                  ]
                }
              ]
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("complex_layout", component.id)

        when (component) {
            is SduiComponent.ColumnComponent -> {
                assertEquals(2, component.children.size)

                // Test first child (Row)
                val rowComponent = component.children[0]
                when (rowComponent) {
                    is SduiComponent.RowComponent -> {
                        assertEquals("header_row", rowComponent.id)
                        assertEquals(2, rowComponent.children.size)

                        val headerText = rowComponent.children[0]
                        when (headerText) {
                            is SduiComponent.TextComponent -> {
                                assertEquals("Header", headerText.text)
                            }
                            else -> throw AssertionError("Expected TextComponent")
                        }

                        val actionButton = rowComponent.children[1]
                        when (actionButton) {
                            is SduiComponent.ButtonComponent -> {
                                assertEquals("Action", actionButton.text)
                            }
                            else -> throw AssertionError("Expected ButtonComponent")
                        }
                    }
                    else -> throw AssertionError("Expected RowComponent")
                }

                // Test second child (Card)
                val cardComponent = component.children[1]
                when (cardComponent) {
                    is SduiComponent.CardComponent -> {
                        assertEquals("content_card", cardComponent.id)
                        assertEquals(2, cardComponent.children.size)

                        val cardText = cardComponent.children[0]
                        when (cardText) {
                            is SduiComponent.TextComponent -> {
                                assertEquals("Card content", cardText.text)
                            }
                            else -> throw AssertionError("Expected TextComponent")
                        }

                        val cardImage = cardComponent.children[1]
                        when (cardImage) {
                            is SduiComponent.ImageComponent -> {
                                assertEquals("https://example.com/image.jpg", cardImage.url)
                            }
                            else -> throw AssertionError("Expected ImageComponent")
                        }
                    }
                    else -> throw AssertionError("Expected CardComponent")
                }
            }
            else -> throw AssertionError("Expected ColumnComponent")
        }
    }

    @Test
    fun testSerializerMethods() {
        val originalComponent =
            SduiComponent.TextComponent(
                id = "test_text",
                text = "Test text",
                style =
                Style(
                    fontSize = 16.0f,
                    textColor = "#000000",
                ),
            )

        // Test serialize
        val json = SduiSerializer.serialize(originalComponent)
        assertTrue(json.contains("Test text"))
        assertTrue(json.contains("test_text"))

        // Test deserialize
        val deserializedComponent = SduiSerializer.deserialize(json)
        assertEquals(originalComponent.id, deserializedComponent.id)

        when (deserializedComponent) {
            is SduiComponent.TextComponent -> {
                assertEquals(originalComponent.text, deserializedComponent.text)
                assertEquals(originalComponent.style?.fontSize, deserializedComponent.style?.fontSize)
                assertEquals(originalComponent.style?.textColor, deserializedComponent.style?.textColor)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }

    @Test
    fun testListSerialization() {
        val components =
            listOf(
                SduiComponent.TextComponent(id = "text1", text = "First text"),
                SduiComponent.ButtonComponent(id = "button1", text = "First button"),
                SduiComponent.TextComponent(id = "text2", text = "Second text"),
            )

        // Test serializeList
        val json = SduiSerializer.serializeList(components)
        assertTrue(json.contains("First text"))
        assertTrue(json.contains("First button"))
        assertTrue(json.contains("Second text"))

        // Test deserializeList
        val deserializedComponents = SduiSerializer.deserializeList(json)
        assertEquals(3, deserializedComponents.size)

        when (val firstComponent = deserializedComponents[0]) {
            is SduiComponent.TextComponent -> {
                assertEquals("text1", firstComponent.id)
                assertEquals("First text", firstComponent.text)
            }
            else -> throw AssertionError("Expected TextComponent")
        }

        when (val secondComponent = deserializedComponents[1]) {
            is SduiComponent.ButtonComponent -> {
                assertEquals("button1", secondComponent.id)
                assertEquals("First button", secondComponent.text)
            }
            else -> throw AssertionError("Expected ButtonComponent")
        }

        when (val thirdComponent = deserializedComponents[2]) {
            is SduiComponent.TextComponent -> {
                assertEquals("text2", thirdComponent.id)
                assertEquals("Second text", thirdComponent.text)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }

    @Test
    fun testTextComponentSpecificProperties() {
        val json =
            """
            {
              "type": "Text",
              "id": "multiline_text",
              "text": "This is a long text that should be truncated",
              "maxLines": 2,
              "textOverflow": "ellipsis",
              "style": {
                "fontSize": 14.0,
                "textColor": "#666666"
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.TextComponent -> {
                assertEquals("multiline_text", component.id)
                assertEquals("This is a long text that should be truncated", component.text)
                assertEquals(2, component.maxLines)
                assertEquals("ellipsis", component.textOverflow)
                assertEquals(14.0f, component.style?.fontSize)
                assertEquals("#666666", component.style?.textColor)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }

    @Test
    fun testButtonComponentSpecificProperties() {
        val json =
            """
            {
              "type": "Button",
              "id": "loading_button",
              "text": "Loading...",
              "enabled": false,
              "loading": true,
              "style": {
                "backgroundColor": "#CCCCCC",
                "textColor": "#999999"
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.ButtonComponent -> {
                assertEquals("loading_button", component.id)
                assertEquals("Loading...", component.text)
                assertEquals(false, component.enabled)
                assertEquals(true, component.loading)
                assertEquals("#CCCCCC", component.style?.backgroundColor)
                assertEquals("#999999", component.style?.textColor)
            }
            else -> throw AssertionError("Expected ButtonComponent")
        }
    }

    @Test
    fun testImageComponentSpecificProperties() {
        val json =
            """
            {
              "type": "Image",
              "id": "fallback_image",
              "url": "https://example.com/image.jpg",
              "altText": "Product image",
              "contentDescription": "Product photo",
              "placeholder": "https://example.com/placeholder.jpg",
              "errorPlaceholder": "https://example.com/error.jpg"
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.ImageComponent -> {
                assertEquals("fallback_image", component.id)
                assertEquals("https://example.com/image.jpg", component.url)
                assertEquals("Product image", component.altText)
                assertEquals("Product photo", component.contentDescription)
                assertEquals("https://example.com/placeholder.jpg", component.placeholder)
                assertEquals("https://example.com/error.jpg", component.errorPlaceholder)
            }
            else -> throw AssertionError("Expected ImageComponent")
        }
    }

    @Test
    fun testTextFieldComponentSpecificProperties() {
        val json =
            """
            {
              "type": "TextField",
              "id": "password_field",
              "placeholder": "Enter password",
              "value": "",
              "enabled": true,
              "keyboardType": "password",
              "maxLines": 1,
              "isPassword": true,
              "style": {
                "borderWidth": 2.0,
                "borderColor": "#FF0000"
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.TextFieldComponent -> {
                assertEquals("password_field", component.id)
                assertEquals("Enter password", component.placeholder)
                assertEquals("", component.value)
                assertEquals(true, component.enabled)
                assertEquals("password", component.keyboardType)
                assertEquals(1, component.maxLines)
                assertEquals(true, component.isPassword)
                assertEquals(2.0f, component.style?.borderWidth)
                assertEquals("#FF0000", component.style?.borderColor)
            }
            else -> throw AssertionError("Expected TextFieldComponent")
        }
    }

    @Test
    fun testNavigateActionWithArguments() {
        val json =
            """
            {
              "type": "Button",
              "id": "detail_button",
              "text": "View Details",
              "action": {
                "type": "navigate",
                "route": "/product",
                "arguments": {
                  "productId": "123",
                  "category": "electronics"
                }
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.ButtonComponent -> {
                when (val action = component.action) {
                    is SduiAction.Navigate -> {
                        assertEquals("/product", action.route)
                        assertEquals("123", action.arguments?.get("productId"))
                        assertEquals("electronics", action.arguments?.get("category"))
                    }
                    else -> throw AssertionError("Expected Navigate action")
                }
            }
            else -> throw AssertionError("Expected ButtonComponent")
        }
    }

    @Test
    fun testComponentVisibility() {
        val json =
            """
            {
              "type": "Text",
              "id": "hidden_text",
              "text": "This text is hidden",
              "visible": false
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("hidden_text", component.id)
        assertEquals(false, component.visible)

        when (component) {
            is SduiComponent.TextComponent -> {
                assertEquals("This text is hidden", component.text)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }

    @Test
    fun testEmptyChildrenList() {
        val json =
            """
            {
              "type": "Column",
              "id": "empty_column",
              "children": []
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("empty_column", component.id)

        when (component) {
            is SduiComponent.ColumnComponent -> {
                assertEquals(0, component.children.size)
                assertTrue(component.children.isEmpty())
            }
            else -> throw AssertionError("Expected ColumnComponent")
        }
    }

    @Test
    fun testComponentWithAllOptionalProperties() {
        val json =
            """
            {
              "type": "Text",
              "text": "Minimal text component"
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.TextComponent -> {
                assertEquals("Minimal text component", component.text)
                // id will have a default value, not null
                assertNotNull(component.id)
                assertEquals(null, component.style)
                assertEquals(null, component.action)
                assertEquals(true, component.visible)
                assertEquals(null, component.meta)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }

    @Test
    fun testCardComponentWithElevation() {
        val json =
            """
            {
              "type": "Card",
              "id": "elevated_card",
              "elevation": 8.0,
              "children": [
                {
                  "type": "Text",
                  "text": "Elevated card content"
                }
              ]
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("elevated_card", component.id)

        when (component) {
            is SduiComponent.CardComponent -> {
                assertEquals(8.0f, component.elevation)
                assertEquals(1, component.children.size)

                val textComponent = component.children[0]
                when (textComponent) {
                    is SduiComponent.TextComponent -> {
                        assertEquals("Elevated card content", textComponent.text)
                    }
                    else -> throw AssertionError("Expected TextComponent as child")
                }
            }
            else -> throw AssertionError("Expected CardComponent")
        }
    }

    @Test
    fun testSpacerComponentWithBothDimensions() {
        val json =
            """
            {
              "type": "Spacer",
              "id": "flexible_spacer",
              "width": 50.0,
              "height": 20.0
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("flexible_spacer", component.id)

        when (component) {
            is SduiComponent.SpacerComponent -> {
                assertEquals(50.0f, component.width)
                assertEquals(20.0f, component.height)
            }
            else -> throw AssertionError("Expected SpacerComponent")
        }
    }

    @Test
    fun testDividerComponentWithCustomProperties() {
        val json =
            """
            {
              "type": "Divider",
              "id": "thick_divider",
              "color": "#FF0000",
              "thickness": 3.0
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("thick_divider", component.id)

        when (component) {
            is SduiComponent.DividerComponent -> {
                assertEquals("#FF0000", component.color)
                assertEquals(3.0f, component.thickness)
            }
            else -> throw AssertionError("Expected DividerComponent")
        }
    }

    @Test
    fun testRowComponentWithSpacing() {
        val json =
            """
            {
              "type": "Row",
              "id": "spaced_row",
              "spacing": 24.0,
              "children": [
                {
                  "type": "Text",
                  "text": "Left"
                },
                {
                  "type": "Text",
                  "text": "Right"
                }
              ]
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("spaced_row", component.id)

        when (component) {
            is SduiComponent.RowComponent -> {
                assertEquals(24.0f, component.spacing)
                assertEquals(2, component.children.size)

                val leftText = component.children[0]
                when (leftText) {
                    is SduiComponent.TextComponent -> {
                        assertEquals("Left", leftText.text)
                    }
                    else -> throw AssertionError("Expected TextComponent")
                }

                val rightText = component.children[1]
                when (rightText) {
                    is SduiComponent.TextComponent -> {
                        assertEquals("Right", rightText.text)
                    }
                    else -> throw AssertionError("Expected TextComponent")
                }
            }
            else -> throw AssertionError("Expected RowComponent")
        }
    }

    @Test
    fun testColumnComponentWithSpacing() {
        val json =
            """
            {
              "type": "Column",
              "id": "spaced_column",
              "spacing": 16.0,
              "children": [
                {
                  "type": "Text",
                  "text": "Top"
                },
                {
                  "type": "Text",
                  "text": "Bottom"
                }
              ]
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)
        assertEquals("spaced_column", component.id)

        when (component) {
            is SduiComponent.ColumnComponent -> {
                assertEquals(16.0f, component.spacing)
                assertEquals(2, component.children.size)

                val topText = component.children[0]
                when (topText) {
                    is SduiComponent.TextComponent -> {
                        assertEquals("Top", topText.text)
                    }
                    else -> throw AssertionError("Expected TextComponent")
                }

                val bottomText = component.children[1]
                when (bottomText) {
                    is SduiComponent.TextComponent -> {
                        assertEquals("Bottom", bottomText.text)
                    }
                    else -> throw AssertionError("Expected TextComponent")
                }
            }
            else -> throw AssertionError("Expected ColumnComponent")
        }
    }

    @Test
    fun testPaddingSerialization() {
        val json =
            """
            {
              "type": "Text",
              "id": "padded_text",
              "text": "Text with padding",
              "style": {
                "padding": {
                  "top": 10.0,
                  "bottom": 10.0,
                  "start": 20.0,
                  "end": 20.0,
                  "horizontal": 20.0,
                  "vertical": 10.0,
                  "all": 15.0
                }
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.TextComponent -> {
                val padding = component.style?.padding
                assertNotNull(padding)

                assertEquals(10.0f, padding.top)
                assertEquals(10.0f, padding.bottom)
                assertEquals(20.0f, padding.start)
                assertEquals(20.0f, padding.end)
                assertEquals(20.0f, padding.horizontal)
                assertEquals(10.0f, padding.vertical)
                assertEquals(15.0f, padding.all)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }

    @Test
    fun testMarginSerialization() {
        val json =
            """
            {
              "type": "Text",
              "id": "margined_text",
              "text": "Text with margin",
              "style": {
                "margin": {
                  "top": 5.0,
                  "bottom": 5.0,
                  "start": 10.0,
                  "end": 10.0,
                  "horizontal": 10.0,
                  "vertical": 5.0,
                  "all": 8.0
                }
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.TextComponent -> {
                val margin = component.style?.margin
                assertNotNull(margin)

                assertEquals(5.0f, margin.top)
                assertEquals(5.0f, margin.bottom)
                assertEquals(10.0f, margin.start)
                assertEquals(10.0f, margin.end)
                assertEquals(10.0f, margin.horizontal)
                assertEquals(5.0f, margin.vertical)
                assertEquals(8.0f, margin.all)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }

    @Test
    fun testPaddingWithPartialValues() {
        val json =
            """
            {
              "type": "Text",
              "id": "partial_padding_text",
              "text": "Text with partial padding",
              "style": {
                "padding": {
                  "top": 8.0,
                  "bottom": 8.0
                }
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.TextComponent -> {
                val padding = component.style?.padding
                assertNotNull(padding)

                assertEquals(8.0f, padding.top)
                assertEquals(8.0f, padding.bottom)
                assertEquals(null, padding.start)
                assertEquals(null, padding.end)
                assertEquals(null, padding.horizontal)
                assertEquals(null, padding.vertical)
                assertEquals(null, padding.all)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }

    @Test
    fun testMarginWithPartialValues() {
        val json =
            """
            {
              "type": "Text",
              "id": "partial_margin_text",
              "text": "Text with partial margin",
              "style": {
                "margin": {
                  "start": 16.0,
                  "end": 16.0
                }
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.TextComponent -> {
                val margin = component.style?.margin
                assertNotNull(margin)

                assertEquals(null, margin.top)
                assertEquals(null, margin.bottom)
                assertEquals(16.0f, margin.start)
                assertEquals(16.0f, margin.end)
                assertEquals(null, margin.horizontal)
                assertEquals(null, margin.vertical)
                assertEquals(null, margin.all)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }

    @Test
    fun testPaddingWithOnlyAllValue() {
        val json =
            """
            {
              "type": "Text",
              "id": "all_padding_text",
              "text": "Text with all padding",
              "style": {
                "padding": {
                  "all": 12.0
                }
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.TextComponent -> {
                val padding = component.style?.padding
                assertNotNull(padding)

                assertEquals(null, padding.top)
                assertEquals(null, padding.bottom)
                assertEquals(null, padding.start)
                assertEquals(null, padding.end)
                assertEquals(null, padding.horizontal)
                assertEquals(null, padding.vertical)
                assertEquals(12.0f, padding.all)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }

    @Test
    fun testMarginWithOnlyAllValue() {
        val json =
            """
            {
              "type": "Text",
              "id": "all_margin_text",
              "text": "Text with all margin",
              "style": {
                "margin": {
                  "all": 6.0
                }
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.TextComponent -> {
                val margin = component.style?.margin
                assertNotNull(margin)

                assertEquals(null, margin.top)
                assertEquals(null, margin.bottom)
                assertEquals(null, margin.start)
                assertEquals(null, margin.end)
                assertEquals(null, margin.horizontal)
                assertEquals(null, margin.vertical)
                assertEquals(6.0f, margin.all)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }

    @Test
    fun testPaddingAndMarginTogether() {
        val json =
            """
            {
              "type": "Text",
              "id": "padded_margined_text",
              "text": "Text with both padding and margin",
              "style": {
                "padding": {
                  "horizontal": 16.0,
                  "vertical": 8.0
                },
                "margin": {
                  "top": 4.0,
                  "bottom": 4.0
                }
              }
            }
            """.trimIndent()

        val component = SduiSerializer.deserialize(json)

        assertNotNull(component)

        when (component) {
            is SduiComponent.TextComponent -> {
                val style = component.style
                assertNotNull(style)

                val padding = style.padding
                assertNotNull(padding)
                assertEquals(null, padding.top)
                assertEquals(null, padding.bottom)
                assertEquals(null, padding.start)
                assertEquals(null, padding.end)
                assertEquals(16.0f, padding.horizontal)
                assertEquals(8.0f, padding.vertical)
                assertEquals(null, padding.all)

                val margin = style.margin
                assertNotNull(margin)
                assertEquals(4.0f, margin.top)
                assertEquals(4.0f, margin.bottom)
                assertEquals(null, margin.start)
                assertEquals(null, margin.end)
                assertEquals(null, margin.horizontal)
                assertEquals(null, margin.vertical)
                assertEquals(null, margin.all)
            }
            else -> throw AssertionError("Expected TextComponent")
        }
    }
}

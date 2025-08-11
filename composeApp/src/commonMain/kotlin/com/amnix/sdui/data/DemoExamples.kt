package com.amnix.sdui.data

import org.jetbrains.compose.resources.ExperimentalResourceApi
import sdui_kt.composeapp.generated.resources.Res

/**
 * Demo example data class for the SDUI Playground
 */
data class DemoExample(
    val name: String,
    val description: String,
    val resourcePath: String,
    var json: String = "", // Will be loaded from resources
)

/**
 * Repository for SDUI demo examples with resource loading capabilities
 */
@OptIn(ExperimentalResourceApi::class)
object SduiDemoExamples {

    /**
     * Available demo examples for the SDUI Playground
     */
    val examples = listOf(
        DemoExample(
            name = "Shapes Layout",
            description = "Row with 4 different shapes using boxes",
            resourcePath = "files/sdui-examples/shapes-layout.json",
        ),
    )

    /**
     * Loads JSON content for a demo example if not already loaded
     *
     * @param example The demo example to load
     * @return A copy of the example with JSON content loaded
     */
    suspend fun loadExample(example: DemoExample): DemoExample = if (example.json.isEmpty()) {
        example.copy(json = loadJsonFromResource(example.resourcePath))
    } else {
        example
    }

    /**
     * Loads JSON content from a resource file
     *
     * @param path The resource path to load from
     * @return The JSON content as a string, or an error JSON if loading fails
     */
    private suspend fun loadJsonFromResource(path: String): String = try {
        Res.readBytes(path).decodeToString()
    } catch (e: Exception) {
        println("Error loading resource $path: ${e.message}")
        // Return a fallback error JSON that still renders something
        """{"type": "text", "id": "error", "text": "Failed to load example: ${e.message}"}"""
    }

    /**
     * Gets the first available example (useful for initial state)
     */
    val defaultExample: DemoExample
        get() = examples.first()

    /**
     * Finds an example by name
     *
     * @param name The name of the example to find
     * @return The example if found, or the default example if not found
     */
    fun findExampleByName(name: String): DemoExample = examples.find { it.name == name } ?: defaultExample

    /**
     * Gets a random example (useful for demos or testing)
     */
    fun getRandomExample(): DemoExample = examples.random()
}

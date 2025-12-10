# GEMINI.md

## Project Overview

This project is a demonstration and educational tool for Server-Driven UI (SDUI) built with Kotlin Multiplatform and Jetpack Compose. It is not intended for use as a production library. The project showcases how to build a flexible UI rendering system where the UI is defined by a JSON structure served from a "server" (in this case, local JSON files).

The core of the project is divided into two main modules:

*   `sdui`: This module contains the core SDUI logic, including the data models for the UI components (`SduiComponent`), actions (`SduiAction`), the JSON serializer (`SduiSerializer`), the validator (`SduiValidator`), and the renderer (`Renderer.kt`).
*   `composeApp`: This is the application module that consumes the `sdui` module. It provides a playground environment to experiment with the SDUI system. It includes a JSON editor, a live preview of the rendered UI, and a log window to show the parsing and validation process.

The project is configured to run on Android, iOS, Desktop (JVM), and Web (Wasm).

## Architectural Deep Dive

The project follows a simple, yet powerful, unidirectional data flow architecture.

```
+----------------+      +------------------+      +-----------------+      +-----------------+
|   JSON Source  |----->|  SduiSerializer  |----->|  SduiComponent  |----->|    Renderer     |
+----------------+      +------------------+      +-----------------+      +-----------------+
                           |                                                     |
                           | (Validation)                                        | (Renders)
                           |                                                     |
                           v                                                     v
+----------------+      +------------------+      +-----------------+      +-----------------+
| Validation Rules|      |  SduiValidator   |      |   Composable UI |<-----| ActionDispatcher|
+----------------+      +------------------+      +-----------------+      +-----------------+
                                                                               ^
                                                                               | (Dispatches)
                                                                               |
                                                                         +------------+
                                                                         | User Input |
                                                                         +------------+
```

1.  **JSON Source**: A JSON string that defines the UI structure, content, and actions. In this project, these are stored as `.json` files in the `composeApp/src/commonMain/composeResources/files/sdui-examples` directory.

2.  **Deserialization and Validation**:
    *   `SduiSerializer`: This object uses the `kotlinx.serialization` library to parse the JSON string into a tree of `SduiComponent` data classes.
    *   `SduiValidator`: Before rendering, the parsed `SduiComponent` tree is traversed by the `SduiValidator` to ensure it conforms to the expected structure and rules (e.g., a `button` must have an `action`).

3.  **UI Rendering**:
    *   `Renderer.kt`: The `RenderSduiComponent` composable function takes the validated `SduiComponent` tree and recursively maps each component to a corresponding Jetpack Compose composable (e.g., `SduiComponent.TextComponent` is rendered as a `Text` composable).
    *   `StyleExtensions.kt`: This file provides helper functions to apply styling properties (e.g., padding, color, font size) from the `Style` model to the composables.

4.  **User Interaction**:
    *   `ActionDispatcher`: This interface defines how actions triggered by the user (e.g., button clicks) are handled. The `SduiPlaygroundScreen` provides a concrete implementation that logs the actions and can update the `FormState`.
    *   `FormState`: This is a state holder that manages the values of input fields (e.g., `TextFieldComponent`) within the rendered UI.

## Building and Running

The project uses Gradle as its build system.

### Android

To run the Android application, open the project in Android Studio (Giraffe or newer) and run the `composeApp` configuration.

### iOS

To run on iOS, open the `iosApp/iosApp.xcodeproj` file in Xcode and run the project.

### Desktop

To run the desktop application, execute the following command in the root directory of the project:

```bash
./gradlew :composeApp:run
```

### Web (Wasm)

To run the web application, execute the following command in the root directory of the project:

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

## Extending the System

The project is designed to be easily extensible. Here's how you can add a new component or action:

### Adding a New Component

1.  **Define the data class**: In `sdui/src/commonMain/kotlin/com/amnix/sdui/sdui/components/SduiComponent.kt`, add a new data class that extends `SduiComponent`. Make sure to annotate it with `@Serializable` and a `@SerialName`.
2.  **Add a renderer**: In `sdui/src/commonMain/kotlin/com/amnix/sdui/sdui/renderer/Renderer.kt`, add a new composable function that takes your new component data class as a parameter and renders the desired UI.
3.  **Update the main renderer**: In the `RenderSduiComponent` function, add a new branch to the `when` statement to call your new renderer.
4.  **(Optional) Add validation**: If your new component has specific validation rules, add them to `sdui/src/commonMain/kotlin/com/amnix/sdui/sdui/validation/SduiValidator.kt`.

### Adding a New Action

1.  **Define the data class**: In `sdui/src/commonMain/kotlin/com/amnix/sdui/sdui/model/SduiAction.kt`, add a new data class that extends `SduiAction`. Annotate it with `@Serializable` and a `@SerialName`.
2.  **Handle the action**: In the `ActionDispatcher` implementation (in `SduiPlaygroundScreen.kt` for the demo), add a new branch to the `when` statement in the `dispatch` function to handle your new action.

## Development Conventions

*   **Kotlin Multiplatform:** The project follows the standard Kotlin Multiplatform project structure. Shared code is located in `commonMain` source sets, and platform-specific code is in `androidMain`, `iosMain`, `jvmMain`, and `wasmJsMain`.
*   **Jetpack Compose:** The UI is built using Jetpack Compose for all platforms.
*   **Serialization:** `kotlinx.serialization` is used for parsing the JSON into the `SduiComponent` data models.
*   **Immutability:** The SDUI models (`SduiComponent`, `SduiAction`, etc.) are immutable data classes.
*   **State Management:** The playground screen uses `mutableStateOf` and `remember` to manage the state of the JSON input, selected example, and logs. Form state is managed in a `mutableStateMapOf`.
*   **Error Handling:** The `SduiSerializer` provides detailed error messages when JSON parsing or validation fails. The playground UI displays these errors in the log window and falls back to the last known good rendering.
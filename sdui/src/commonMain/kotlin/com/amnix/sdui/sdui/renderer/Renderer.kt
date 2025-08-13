package com.amnix.sdui.sdui.renderer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amnix.sdui.sdui.components.SduiComponent

/**
 * Main SDUI renderer function
 */
@Composable
fun RenderSduiComponent(
    component: SduiComponent,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?> = remember { mutableStateMapOf() },
) {
    // Skip rendering if component is not visible
    if (component.visible == false) {
        return
    }

    when (component) {
        is SduiComponent.TextComponent -> SduiTextComponent(component)
        is SduiComponent.ButtonComponent -> SduiButtonComponent(component, dispatcher, formState)
        is SduiComponent.ColumnComponent -> SduiColumnComponent(component, dispatcher, formState)
        is SduiComponent.RowComponent -> SduiRowComponent(component, dispatcher, formState)
        is SduiComponent.ImageComponent -> SduiImageComponent(component)
        is SduiComponent.TextFieldComponent -> SduiTextFieldComponent(component, formState)
        is SduiComponent.SpacerComponent -> SduiSpacerComponent(component)
        is SduiComponent.DividerComponent -> SduiDividerComponent(component)
        is SduiComponent.BoxComponent -> SduiBoxComponent(component, dispatcher, formState)
        is SduiComponent.CardComponent -> SduiCardComponent(component, dispatcher, formState)
        is SduiComponent.ListComponent -> SduiListComponent(component, dispatcher, formState)
        is SduiComponent.GridComponent -> SduiGridComponent(component, dispatcher, formState)
        is SduiComponent.SwitchComponent -> SduiSwitchComponent(component, formState)
        is SduiComponent.CheckboxComponent -> SduiCheckboxComponent(component, formState)
        is SduiComponent.RadioButtonComponent -> SduiRadioButtonComponent(component, formState)
        is SduiComponent.ProgressBarComponent -> SduiProgressBarComponent(component)
        is SduiComponent.SliderComponent -> SduiSliderComponent(component, formState)
        is SduiComponent.ChipComponent -> SduiChipComponent(component, formState)
    }
}

@Composable
private fun SduiTextComponent(component: SduiComponent.TextComponent) {
    val textStyle =
        TextStyle(
            fontSize = component.style?.fontSize?.sp ?: 16.sp,
            fontWeight =
            when (component.style?.fontWeight) {
                "bold" -> FontWeight.Bold
                "light" -> FontWeight.Light
                "medium" -> FontWeight.Medium
                else -> FontWeight.Normal
            },
            color = component.style?.textColor?.let { parseColor(it) } ?: Color.Black,
            textAlign =
            when (component.style?.alignment) {
                "center" -> TextAlign.Center
                "start", "left" -> TextAlign.Start
                "end", "right" -> TextAlign.End
                else -> TextAlign.Start
            },
        )

    Text(
        text = component.text,
        style = textStyle,
        modifier = Modifier.applyStyle(component.style),
        maxLines = component.maxLines ?: Int.MAX_VALUE,
        overflow =
        when (component.textOverflow) {
            "ellipsis" -> TextOverflow.Ellipsis
            "clip" -> TextOverflow.Clip
            else -> TextOverflow.Clip
        },
    )
}

@Composable
private fun SduiButtonComponent(
    component: SduiComponent.ButtonComponent,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
) {
    Button(
        onClick = {
            component.action?.let { action ->
                dispatcher.dispatch(action)
            }
        },
        enabled = component.enabled ?: true,
        modifier = Modifier.applyStyle(component.style),
    ) {
        if (component.loading == true) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        } else {
            Text(text = component.text)
        }
    }
}

@Composable
private fun SduiColumnComponent(
    component: SduiComponent.ColumnComponent,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
) {
    Column(
        modifier = Modifier.applyStyle(component.style),
        verticalArrangement = Arrangement.spacedBy(component.spacing?.dp ?: 0.dp),
        horizontalAlignment = parseHorizontalAlignment(component.style?.alignItems),
    ) {
        component.children.forEach { child ->
            RenderSduiComponent(child, dispatcher, formState)
        }
    }
}

@Composable
private fun SduiRowComponent(
    component: SduiComponent.RowComponent,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
) {
    val horizontalArrangement =
        when (component.style?.justifyContent?.lowercase()) {
            "spacebetween", "space_between", "space-between" -> Arrangement.SpaceBetween
            "spacearound", "space_around", "space-around" -> Arrangement.SpaceAround
            "spaceevenly", "space_evenly", "space-evenly" -> Arrangement.SpaceEvenly
            "end", "right" ->
                component.spacing?.dp?.let { Arrangement.spacedBy(it, Alignment.End) } ?: Arrangement.End
            "center" -> component.spacing?.dp?.let { Arrangement.spacedBy(it, Alignment.CenterHorizontally) } ?: Arrangement.Center
            else -> component.spacing?.dp?.let { Arrangement.spacedBy(it, Alignment.Start) } ?: Arrangement.Start
        }

    Row(
        modifier = Modifier.applyStyle(component.style),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = parseVerticalAlignment(component.style?.alignItems),
    ) {
        component.children.forEach { child ->
            RenderSduiComponent(child, dispatcher, formState)
        }
    }
}

@Composable
private fun SduiImageComponent(component: SduiComponent.ImageComponent) {
    // Simple placeholder for image - in a real implementation, you'd use a proper image loader
    Box(
        modifier =
        Modifier
            .applyStyle(component.style)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = component.altText ?: "Image",
            color = Color.Gray,
        )
    }
}

@Composable
private fun SduiTextFieldComponent(component: SduiComponent.TextFieldComponent, formState: MutableMap<String, Any?>) {
    val (value, setValue) =
        FormState.rememberFormValue(
            key = component.id,
            initialValue = component.value ?: "",
            formState = formState,
        )

    OutlinedTextField(
        value = value,
        onValueChange = setValue,
        placeholder = component.placeholder?.let { { Text(it) } },
        enabled = component.enabled ?: true,
        modifier = Modifier.applyStyle(component.style),
        maxLines = component.maxLines ?: 1,
        visualTransformation =
        if (component.isPassword == true) {
            androidx.compose.ui.text.input
                .PasswordVisualTransformation()
        } else {
            androidx.compose.ui.text.input.VisualTransformation.None
        },
    )
}

@Composable
private fun SduiSpacerComponent(component: SduiComponent.SpacerComponent) {
    Spacer(
        modifier =
        Modifier
            .applyStyle(component.style)
            .width(component.width?.dp ?: 0.dp)
            .height(component.height?.dp ?: 0.dp),
    )
}

@Composable
private fun SduiDividerComponent(component: SduiComponent.DividerComponent) {
    @Suppress("DEPRECATION")
    Divider(
        modifier = Modifier.applyStyle(component.style),
        thickness = component.thickness?.dp ?: 1.dp,
        color =
        component.color?.let { parseColor(it) } ?: MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.12f,
        ),
    )
}

@Composable
private fun SduiBoxComponent(
    component: SduiComponent.BoxComponent,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
) {
    Box(
        modifier = Modifier.applyStyle(component.style),
        contentAlignment = Alignment.Center,
    ) {
        component.children.forEach { child ->
            RenderSduiComponent(child, dispatcher, formState)
        }
    }
}

@Composable
private fun SduiCardComponent(
    component: SduiComponent.CardComponent,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
) {
    Card(
        modifier = Modifier.applyStyle(component.style),
        elevation = CardDefaults.cardElevation(defaultElevation = component.elevation?.dp ?: 1.dp),
    ) {
        component.children.forEach { child ->
            RenderSduiComponent(child, dispatcher, formState)
        }
    }
}

@Composable
private fun SduiListComponent(
    component: SduiComponent.ListComponent,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
) {
    LazyColumn(
        modifier = Modifier.applyStyle(component.style),
        verticalArrangement = Arrangement.spacedBy(component.itemSpacing?.dp ?: 0.dp),
    ) {
        items(component.items) { item ->
            RenderSduiComponent(item, dispatcher, formState)
        }
    }
}

@Composable
private fun SduiGridComponent(
    component: SduiComponent.GridComponent,
    dispatcher: ActionDispatcher,
    formState: MutableMap<String, Any?>,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(component.columns),
        modifier = Modifier.applyStyle(component.style),
        horizontalArrangement = Arrangement.spacedBy(component.itemSpacing?.dp ?: 0.dp),
        verticalArrangement = Arrangement.spacedBy(component.itemSpacing?.dp ?: 0.dp),
    ) {
        items(component.items) { item ->
            RenderSduiComponent(item, dispatcher, formState)
        }
    }
}

@Composable
private fun SduiSwitchComponent(component: SduiComponent.SwitchComponent, formState: MutableMap<String, Any?>) {
    val (checked, setChecked) =
        FormState.rememberFormValue(
            key = component.id,
            initialValue = component.checked,
            formState = formState,
        )

    Row(
        modifier = Modifier.applyStyle(component.style),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Switch(
            checked = checked,
            onCheckedChange = setChecked,
            enabled = component.enabled ?: true,
        )
        component.label?.let { label ->
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label)
        }
    }
}

@Composable
private fun SduiCheckboxComponent(component: SduiComponent.CheckboxComponent, formState: MutableMap<String, Any?>) {
    val (checked, setChecked) =
        FormState.rememberFormValue(
            key = component.id,
            initialValue = component.checked,
            formState = formState,
        )

    Row(
        modifier = Modifier.applyStyle(component.style),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = setChecked,
            enabled = component.enabled ?: true,
        )
        component.label?.let { label ->
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label)
        }
    }
}

@Composable
private fun SduiRadioButtonComponent(
    component: SduiComponent.RadioButtonComponent,
    formState: MutableMap<String, Any?>,
) {
    val (selected, setSelected) =
        FormState.rememberFormValue(
            key = component.id,
            initialValue = component.selected,
            formState = formState,
        )

    Row(
        modifier = Modifier.applyStyle(component.style),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = { setSelected(true) },
            enabled = component.enabled ?: true,
        )
        component.label?.let { label ->
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label)
        }
    }
}

@Composable
private fun SduiProgressBarComponent(component: SduiComponent.ProgressBarComponent) {
    Column(
        modifier = Modifier.applyStyle(component.style),
    ) {
        if (component.indeterminate == true) {
            LinearProgressIndicator()
        } else {
            LinearProgressIndicator(
                progress = { component.progress },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        component.label?.let { label ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label)
        }
    }
}

@Composable
private fun SduiSliderComponent(component: SduiComponent.SliderComponent, formState: MutableMap<String, Any?>) {
    val (value, setValue) =
        FormState.rememberFormValue(
            key = component.id,
            initialValue = component.value,
            formState = formState,
        )

    Column(
        modifier = Modifier.applyStyle(component.style),
    ) {
        Slider(
            value = value,
            onValueChange = setValue,
            valueRange = component.minValue..component.maxValue,
            steps =
            component.step?.let { ((component.maxValue - component.minValue) / it).toInt() - 1 }
                ?: 0,
            enabled = component.enabled ?: true,
        )
        component.label?.let { label ->
            Text(text = label)
        }
    }
}

@Composable
private fun SduiChipComponent(component: SduiComponent.ChipComponent, formState: MutableMap<String, Any?>) {
    val (selected, setSelected) =
        FormState.rememberFormValue(
            key = component.id,
            initialValue = component.selected ?: false,
            formState = formState,
        )

    FilterChip(
        selected = selected,
        onClick = { setSelected(!selected) },
        label = { Text(text = component.text) },
        enabled = component.enabled ?: true,
        modifier = Modifier.applyStyle(component.style),
    )
}

package com.xinto.opencord.ui.component.textfield

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinto.opencord.ui.component.text.OpenCordText
import com.xinto.opencord.ui.theme.secondaryButton

@Composable
fun OpenCordBasicTextField(
    value: String,
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val shapePercent by animateIntAsState(
        targetValue = when (value.lines().size) {
            0 -> 50
            1 -> 50
            2 -> 40
            3 -> 30
            else -> 25
        }
    )
    BasicTextField(
        value = value,
        modifier = modifier,
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = maxLines,
        textStyle = TextStyle(
            fontSize = 14.sp,
            color = Color.White
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        decorationBox = { box ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.secondaryButton,
                shape = RoundedCornerShape(percent = shapePercent),
                elevation = 0.dp
            ) {
                Box(
                    modifier = Modifier.padding(12.dp)
                ) {
                    box()
                    if (value.isBlank()) {
                        CompositionLocalProvider(
                            LocalContentAlpha provides ContentAlpha.medium
                        ) {
                            OpenCordText(text = hint)
                        }
                    }
                }
            }
        }
    )
}
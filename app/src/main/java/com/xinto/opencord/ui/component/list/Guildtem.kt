package com.xinto.opencord.ui.component.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.xinto.opencord.ui.component.indicator.UnreadIndicator

@OptIn(ExperimentalCoilApi::class)
@Composable
fun GuildItem(
    selected: Boolean,
    onClick: () -> Unit,
    image: @Composable BoxScope.() -> Unit
) {
    val indicatorFraction by animateFloatAsState(if (selected) 0.7f else 0.15f)

    val imageCornerRadius by animateIntAsState(
        targetValue = if (selected) 25 else 50,
        animationSpec = tween(400)
    )

    Box(
        modifier = Modifier.height(48.dp),
    ) {
        UnreadIndicator(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight(indicatorFraction)
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 12.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(imageCornerRadius))
                .background(color = MaterialTheme.colors.surface)
                .clickable(onClick = onClick),
            content = image
        )
    }
}
package com.xinto.opencord.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun WidgetChannelListItem(
    onClick: () -> Unit,
    title: String,
    icon: ImageVector,
    selected: Boolean,
    showIndicator: Boolean,
    modifier: Modifier = Modifier,
) {
    val backgroundAlpha by animateFloatAsState(if (selected) 0.2f else 0f)

    Box(
        modifier = modifier,
    ) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterStart),
            visible = showIndicator,
            enter = slideInHorizontally(),
            exit = slideOutHorizontally()
        ) {
            UnreadIndicator(
                modifier = Modifier.height(6.dp)
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colors.onBackground.copy(backgroundAlpha))
                .clickable(onClick = onClick)
                .padding(6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Channel Type"
            )
            Text(
                text = title,
                style = MaterialTheme.typography.h5
            )
        }
    }
}
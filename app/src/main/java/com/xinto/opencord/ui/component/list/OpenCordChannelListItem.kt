package com.xinto.opencord.ui.component.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.component.indicator.UnreadIndicator
import com.xinto.opencord.ui.component.text.OpenCordText

@Composable
fun OpenCordChannelListItem(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundAlpha by animateFloatAsState(if (selected) 0.2f else 0f)
    Box(
        modifier = modifier,
    ) {
        UnreadIndicator(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .height(6.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colors.onBackground.copy(backgroundAlpha))
                .clickable(onClick = onClick)
                .padding(6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                modifier = Modifier.align(Alignment.CenterVertically),
                imageVector = icon,
                contentDescription = "Channel Type"
            )
            OpenCordText(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                text = title,
                style = MaterialTheme.typography.h5
            )
        }
    }
}

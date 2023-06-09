@file:Suppress("NOTHING_TO_INLINE")

package com.xinto.opencord.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
inline fun Boolean.ifComposable(
    crossinline block: @Composable () -> Unit,
): (@Composable () -> Unit)? {
    return if (this) {
        remember {
            @Composable { block() }
        }
    } else {
        null
    }
}

@Composable
inline fun <T> T?.ifNotNullComposable(
    crossinline block: @Composable (T) -> Unit,
): (@Composable () -> Unit)? {
    val isNotNull by remember { derivedStateOf { this != null } }

    return if (isNotNull) {
        remember(this) {
            @Composable { block(this!!) }
        }
    } else {
        null
    }
}

@Composable
inline fun <L : Collection<I>, I> L.ifNotEmptyComposable(
    crossinline block: @Composable (L) -> Unit,
): (@Composable () -> Unit)? {
    val isNotEmpty by remember { derivedStateOf { isNotEmpty() } }

    return if (isNotEmpty) {
        remember(this) {
            @Composable { block(this) }
        }
    } else {
        null
    }
}

@Composable
inline fun <T : CharSequence> T.ifNotEmptyComposable(
    crossinline block: @Composable (T) -> Unit,
): (@Composable () -> Unit)? {
    val isNotEmpty by remember { derivedStateOf { isNotEmpty() } }

    return if (isNotEmpty) {
        remember(this) {
            @Composable { block(this) }
        }
    } else {
        null
    }
}

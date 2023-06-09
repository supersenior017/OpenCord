package com.xinto.opencord.ast.node

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import com.xinto.simpleast.Node

class SpoilerNode<RC> : Node.Parent<RC>() {
    //@formatter:off
    context(AnnotatedString.Builder)
    override fun render(renderContext: RC) { //@formatter:on
        withStyle(
            SpanStyle(
                background = Color.Black,
            ),
        ) {
            super.render(renderContext)
        }
    }
}

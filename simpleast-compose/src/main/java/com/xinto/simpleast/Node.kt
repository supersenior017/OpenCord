package com.xinto.simpleast

import androidx.compose.ui.text.AnnotatedString

public open class Node<RC>(
    private var children: MutableCollection<Node<RC>>? = null,
) {
    public val nodeChildren: Collection<Node<RC>>?
        get() = children

    public val hasChildren: Boolean
        get() = children?.isNotEmpty() == true

    public fun addChild(node: Node<RC>) {
        if (children == null) {
            children = ArrayList()
        }

        children!!.add(node)
    }

    //@formatter:off
    context(AnnotatedString.Builder)
    public open fun render(renderContext: RC): Unit = Unit
    //@formatter:on

    public open class Parent<RC>(
        vararg children: Node<RC>?,
    ) : Node<RC>(children.mapNotNull { it }.toMutableList()) {
        //@formatter:off
        context(AnnotatedString.Builder)
        override fun render(renderContext: RC) { //@formatter:on
            nodeChildren?.forEach {
                it.render(renderContext)
            }
        }
    }
}

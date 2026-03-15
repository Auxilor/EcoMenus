package com.willfp.ecomenus.components

import com.willfp.eco.core.gui.GUIComponent
import com.willfp.eco.core.gui.menu.MenuLayer
import com.willfp.eco.core.gui.page.PageBuilder

interface PositionedComponent : GUIComponent {
    val row: Int
    val column: Int

    val isEnabled: Boolean
        get() = true

    val rowSize: Int
        get() = 1
    val columnSize: Int
        get() = 1

    val layer: MenuLayer
        get() = MenuLayer.MIDDLE

    override fun getRows() = rowSize
    override fun getColumns() = columnSize
}

// Little hack to prevent two identical methods
@Suppress("UNCHECKED_CAST")
fun <T : PageBuilder> T.addComponent(
    component: PositionedComponent
): T = if (component.isEnabled) addComponent(
    component.layer,
    component.row,
    component.column,
    component
) as T else this

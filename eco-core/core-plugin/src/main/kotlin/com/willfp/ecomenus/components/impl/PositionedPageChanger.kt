package com.willfp.ecomenus.components.impl

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.menu.MenuLayer
import com.willfp.eco.core.gui.page.PageChanger
import com.willfp.eco.core.items.Items
import com.willfp.ecomenus.components.PositionedComponent
import org.bukkit.entity.Player

class PositionedPageChanger(
    direction: PageChanger.Direction,
    config: Config,
) : PositionedComponent {
    private val component = PageChanger(
        Items.lookup(config.getString("item")).item,
        direction
    )

    override val row: Int = config.getInt("location.row")
    override val column: Int = config.getInt("location.column")
    override val layer = MenuLayer.TOP

    override val isEnabled = config.getBool("enabled")

    override fun getSlotAt(row: Int, column: Int, player: Player, menu: Menu) =
        component.getSlotAt(row, column, player, menu)
}

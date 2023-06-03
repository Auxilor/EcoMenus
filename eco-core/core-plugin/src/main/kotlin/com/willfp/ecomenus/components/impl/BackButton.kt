package com.willfp.ecomenus.components.impl

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.onLeftClick
import com.willfp.eco.core.gui.slot
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.ecomenus.components.PositionedComponent
import org.bukkit.entity.Player

class BackButton(
    config: Config,
    goBack: (Player, Menu) -> Unit
) : PositionedComponent {
    private val slot = slot(
        ItemStackBuilder(Items.lookup(config.getString("item")))
            .setDisplayName(config.getFormattedString("name"))
            .build()
    ) {
        onLeftClick { _, event, _, menu ->
            goBack(event.whoClicked as Player, menu)
        }
    }

    override val row: Int = config.getInt("location.row")
    override val column: Int = config.getInt("location.column")

    override val isEnabled = config.getBool("enabled")

    override fun getSlotAt(row: Int, column: Int) = slot
}

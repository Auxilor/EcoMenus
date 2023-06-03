package com.willfp.ecomenus.components

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.menu.MenuLayer
import com.willfp.eco.core.gui.onClick
import com.willfp.eco.core.gui.slot
import com.willfp.eco.core.gui.slot.Slot
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.placeholder.context.placeholderContext
import com.willfp.ecomenus.slots.SlotTypes
import com.willfp.libreforge.EmptyProvidedHolder
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.conditions.Conditions
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class ConfigurableSlot(
    private val plugin: EcoPlugin,
    baseContext: ViolationContext,
    private val config: Config
) : PositionedComponent {
    override val row: Int = config.getInt("location.row")
    override val column: Int = config.getInt("location.column")
    val page: Int = config.getInt("location.page")

    override val layer = runCatching { enumValueOf<MenuLayer>(config.getString("layer")) }
        .getOrElse { MenuLayer.MIDDLE }

    val context = baseContext.with("slot at row ${row}, column $column, page $page")

    private val conditions = Conditions.compile(
        config.getSubsections("conditions"),
        context.with("conditions")
    )

    private val showIfNotMet = config.getBool("show-if-not-met")

    private val baseItem = Items.lookup(config.getString("item")).item

    private val slot = slot({ player, _ ->
        baseItem.clone().fast().apply {
            if (config.has("lore")) {
                this.lore = config.getFormattedStrings(
                    "lore", placeholderContext(
                        player = player
                    )
                )
            }

            if (!conditions.areMet(player, EmptyProvidedHolder)) {
                this.lore = this.lore + conditions.getNotMetLines(player, EmptyProvidedHolder)
            }
        }.unwrap()
    }) {
        for (clickType in ClickType.values()) {
            val section = "${clickType.name.lowercase().replace("_", "-")}-click"
            val actions = config.getSubsections(section)

            for (action in actions) {
                val typeName = action.getString("type")
                val type = SlotTypes[typeName] ?: continue

                val function = type.create(
                    action,
                    plugin,
                    context.with("slot at row ${row}, column $column").with(section).with(typeName)
                )?.toSlotAction() ?: continue

                onClick(clickType, function)
            }
        }
    }

    override fun getSlotAt(row: Int, column: Int, player: Player, menu: Menu): Slot? {
        if (!showIfNotMet && !conditions.areMet(player, EmptyProvidedHolder)) {
            return null
        }

        return slot
    }
}

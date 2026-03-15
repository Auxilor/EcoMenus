package com.willfp.ecomenus.components

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.menu.MenuLayer
import com.willfp.eco.core.gui.onClick
import com.willfp.eco.core.gui.page.PageBuilder
import com.willfp.eco.core.gui.slot
import com.willfp.eco.core.gui.slot.Slot
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.placeholder.context.placeholderContext
import com.willfp.eco.core.placeholder.findPlaceholders
import com.willfp.eco.core.placeholder.translatePlaceholders
import com.willfp.libreforge.ConfigViolation
import com.willfp.libreforge.EmptyProvidedHolder
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.effects.executors.impl.NormalExecutorFactory
import com.willfp.libreforge.toDispatcher
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class ConfigurableSlot(
    baseContext: ViolationContext,
    private val config: Config
) : PositionedComponent {
    override val row: Int = config.getInt("location.row")
    override val column: Int = config.getInt("location.column")
    val page: Int? = config.getIntOrNull("location.page")

    override val layer = runCatching { enumValueOf<MenuLayer>(config.getString("location.layer").uppercase()) }
        .getOrElse { MenuLayer.MIDDLE }

    private val context = baseContext.with("slot at row ${row}, column $column, page $page")

    private val conditions = Conditions.compile(
        config.getSubsections("conditions"),
        context.with("conditions")
    )

    private val showIfNotMet = config.getBool("show-if-not-met")

    private val itemLookupString = config.getString("item")

    private val isDynamicBaseItem = itemLookupString.findPlaceholders().isNotEmpty()

    private val baseItem = Items.lookup(config.getString("item")).item

    private val slot = slot({ player, _ ->
        val item = if (isDynamicBaseItem) {
            Items.lookup(
                itemLookupString
                    .translatePlaceholders(placeholderContext(player = player))
            ).item
        } else {
            baseItem
        }

        item.clone().fast().apply {
            if (config.has("lore")) {
                this.lore = config.getFormattedStrings(
                    "lore", placeholderContext(
                        player = player
                    )
                )
            }

            if (!conditions.areMet(player.toDispatcher(), EmptyProvidedHolder)) {
                this.lore = this.lore + conditions.getNotMetLines(player.toDispatcher(), EmptyProvidedHolder)
            }
        }.unwrap()
    }) {
        for (clickType in ClickType.entries) {
            val section = "${clickType.name.lowercase().replace("_", "-")}-click"

            val effects = Effects.compileChain(
                config.getSubsections(section),
                NormalExecutorFactory.create(),
                context.with(section)
            ) ?: continue

            onClick(clickType) { player, _, _, _ ->
                if (conditions.areMet(player.toDispatcher(), EmptyProvidedHolder)) {
                    effects.trigger(player.toDispatcher())
                }
            }
        }
    }

    override fun getSlotAt(row: Int, column: Int, player: Player, menu: Menu): Slot? {
        if (!showIfNotMet && !conditions.areMet(player.toDispatcher(), EmptyProvidedHolder)) {
            return null
        }

        return slot
    }

    fun add(builder: PageBuilder) {
        try {
            builder.addComponent(this)
        } catch (_: Exception) {
            context.log(ConfigViolation("location", "Invalid location!"))
        }
    }
}

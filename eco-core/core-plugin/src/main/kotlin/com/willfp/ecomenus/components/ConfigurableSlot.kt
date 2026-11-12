package com.willfp.ecomenus.components

import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import wiki.ecohub.config.Config
import wiki.ecohub.config.ConfigViolation
import wiki.ecohub.config.ViolationContext
import wiki.ecohub.game.entity.toDispatcher
import wiki.ecohub.game.item.Items
import wiki.ecohub.game.item.fast
import wiki.ecohub.modules.menus.SlotLocation
import wiki.ecohub.modules.menus.component.PositionedComponent
import wiki.ecohub.modules.menus.component.addComponent
import wiki.ecohub.modules.menus.getSlotLocations
import wiki.ecohub.modules.menus.menu.Menu
import wiki.ecohub.modules.menus.menu.MenuLayer
import wiki.ecohub.modules.menus.onClick
import wiki.ecohub.modules.menus.page.PageBuilder
import wiki.ecohub.modules.menus.slot
import wiki.ecohub.modules.menus.slot.Slot
import wiki.ecohub.rules.chain.NormalExecutorFactory
import wiki.ecohub.rules.condition.Conditions
import wiki.ecohub.rules.effect.Effects
import wiki.ecohub.rules.holder.EmptyProvidedHolder
import wiki.ecohub.ui.placeholder.placeholderContext
import wiki.ecohub.ui.text.findPlaceholders
import wiki.ecohub.ui.text.translatePlaceholders

class ConfigurableSlot(
    baseContext: ViolationContext,
    private val config: Config
) : PositionedComponent {
    override val locations: List<SlotLocation> = config.getSlotLocations()

    private val row: Int = locations.first().row
    private val column: Int = locations.first().column
    val page: Int? = config.getIntOrNull("location.page")

    override val layer: Int = config.get("location.layer") as? Int? ?: MenuLayer.MIDDLE

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

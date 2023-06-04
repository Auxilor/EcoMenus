package com.willfp.ecomenus.menus

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.gui.addPage
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.page.PageChanger
import com.willfp.eco.core.gui.slot.FillerMask
import com.willfp.eco.core.gui.slot.MaskItems
import com.willfp.ecomenus.components.ConfigurableSlot
import com.willfp.ecomenus.components.addComponent
import com.willfp.ecomenus.components.impl.PositionedPageChanger
import com.willfp.ecomponent.menuStateVar
import com.willfp.libreforge.ViolationContext
import org.bukkit.entity.Player
import java.util.Stack

val Menu.previousMenus by menuStateVar<Stack<Menu>>("previous-menu", Stack())

fun <T> Stack<T>.popOrNull(): T? =
    if (this.empty()) null else this.pop()

fun Menu.open(
    player: Player,
    from: Menu? = null
) {
    this.open(player)
    if (from != null) {
        this.previousMenus[player] += from
    }
}

fun Menu.close(player: Player) =
    this.previousMenus[player].popOrNull()?.open(player) ?: player.closeInventory()

fun buildMenu(plugin: EcoPlugin, menu: EcoMenu, config: Config): Menu {
    val pageConfigs = config.getSubsections("pages")

    val slots = mutableListOf<ConfigurableSlot>()

    for (slotConfig in config.getSubsections("slots")) {
        val slot = ConfigurableSlot(
            plugin,
            ViolationContext(plugin, "menu ${menu.id}"),
            slotConfig
        )

        slots += slot
    }

    return menu(config.getInt("rows")) {
        title = config.getFormattedString("title")

        allowChangingHeldItem()

        maxPages(pageConfigs.size)

        addComponent(
            PositionedPageChanger(
                PageChanger.Direction.FORWARDS,
                config.getSubsection("forwards-arrow")
            )
        )

        addComponent(
            PositionedPageChanger(
                PageChanger.Direction.BACKWARDS,
                config.getSubsection("backwards-arrow")
            )
        )

        for (page in pageConfigs) {
            val mask = FillerMask(
                MaskItems.fromItemNames(page.getStrings("mask.items")),
                *page.getStrings("mask.pattern").toTypedArray()
            )

            val pageNumber = page.getInt("page")

            addPage(pageNumber) {
                setMask(mask)

                for (slot in slots) {
                    if (pageNumber != slot.page) {
                        continue
                    }

                    slot.add(this)
                }
            }
        }

        onClose { event, menu ->
            val player = event.player as Player
            menu.previousMenus[player].popOrNull()?.open(player)
        }
    }
}

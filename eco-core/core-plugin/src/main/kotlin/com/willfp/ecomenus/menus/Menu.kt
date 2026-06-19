package com.willfp.ecomenus.menus

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.gui.addPage
import com.willfp.eco.core.gui.addPageChanger
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.page.PageChanger
import com.willfp.eco.core.gui.slot.FillerMask
import com.willfp.eco.core.gui.slot.MaskItems
import com.willfp.eco.core.sound.PlayableSound
import com.willfp.ecomenus.components.ConfigurableSlot
import com.willfp.ecomenus.components.addComponent
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
            ViolationContext(plugin, "menu ${menu.id}"),
            slotConfig
        )

        slots += slot
    }

    val pageChangeSound = PlayableSound.create(config.getSubsection("page-change-sound"))

    return menu(config.getInt("rows")) {
        title = config.getFormattedString("title")

        allowChangingHeldItem()

        maxPages(pageConfigs.size)

        if (config.getBool("forwards-arrow.enabled")) {
            addPageChanger(config, "forwards-arrow", PageChanger.Direction.FORWARDS, pageChangeSound)
        }

        if (config.getBool("backwards-arrow.enabled")) {
            addPageChanger(config, "backwards-arrow", PageChanger.Direction.BACKWARDS, pageChangeSound)
        }

        for (page in pageConfigs) {
            val mask = FillerMask(
                MaskItems.fromItemNames(page.getStrings("mask.items")),
                *page.getStrings("mask.pattern").toTypedArray()
            )

            val pageNumber = page.getInt("page")

            addPage(pageNumber) {
                setMask(mask)

                for (slot in slots) {
                    if (slot.page == null || pageNumber == slot.page) {
                        slot.add(this)
                    }
                }
            }
        }

        onClose { event, _ ->
            menu.handleClose(event.player as Player)
        }
    }
}

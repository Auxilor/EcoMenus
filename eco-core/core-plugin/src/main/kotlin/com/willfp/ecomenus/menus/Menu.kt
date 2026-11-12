package com.willfp.ecomenus.menus

import com.willfp.ecomenus.components.ConfigurableSlot
import org.bukkit.entity.Player
import wiki.ecohub.config.Config
import wiki.ecohub.config.ConfigViolation
import wiki.ecohub.config.ViolationContext
import wiki.ecohub.core.plugin.EcoPlugin
import wiki.ecohub.game.sound.PlayableSound
import wiki.ecohub.modules.menus.addPage
import wiki.ecohub.modules.menus.addPageChanger
import wiki.ecohub.modules.menus.menu
import wiki.ecohub.modules.menus.menu.Menu
import wiki.ecohub.modules.menus.menu.menuStateVar
import wiki.ecohub.modules.menus.page.PageChanger
import wiki.ecohub.modules.menus.setMask
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
        // getState reads from the now-current RenderedInventory for this menu.
        // If null, create and store a new Stack so we never mutate the shared default instance.
        val stack = this.getState<Stack<Menu>>(player, "previous-menu")
            ?: Stack<Menu>().also { this.setState(player, "previous-menu", it) }
        stack.push(from)
    }
}

fun Menu.close(player: Player) =
    this.previousMenus[player].popOrNull()?.open(player) ?: player.closeInventory()

fun buildMenu(plugin: EcoPlugin, menu: EcoMenu, config: Config): Menu {
    val pageConfigs = config.getSubsections("pages")

    if (pageConfigs.isEmpty()) {
        ViolationContext(plugin, "menu ${menu.id}").log(
            ConfigViolation(param = "pages", message = "Menu has no pages, it will open empty!")
        )
    }

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
        setTitle(config.getFormattedString("title"))

        allowChangingHeldItem()

        maxPages(pageConfigs.size)

        if (config.getBool("forwards-arrow.enabled")) {
            addPageChanger(config, "forwards-arrow", PageChanger.Direction.FORWARDS, pageChangeSound)
        }

        if (config.getBool("backwards-arrow.enabled")) {
            addPageChanger(config, "backwards-arrow", PageChanger.Direction.BACKWARDS, pageChangeSound)
        }

        for (page in pageConfigs) {
            val pageNumber = page.getInt("page")

            addPage(pageNumber) {
                setMask(page, "")

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

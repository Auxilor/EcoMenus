package com.willfp.ecomenus.menus

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot.ConfigSlot
import com.willfp.eco.core.gui.slot.FillerMask
import com.willfp.eco.core.gui.slot.MaskItems
import com.willfp.ecomenus.components.addComponent
import com.willfp.ecomenus.components.impl.BackButton
import com.willfp.ecomenus.components.impl.CloseButton
import com.willfp.ecomponent.menuStateVar
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

fun buildMenu(plugin: EcoPlugin, config: Config): Menu {
    val mask = FillerMask(
        MaskItems.fromItemNames(config.getStrings("mask.materials")),
        *config.getStrings("mask.pattern").toTypedArray()
    )

    return menu(mask.rows) {
        title = config.getFormattedString("title")

        setMask(mask)

        addComponent(
            CloseButton(
                plugin.configYml.getSubsection("stats-gui.close")
            )
        )

        addComponent(
            BackButton(
                plugin.configYml.getSubsection("stats-gui.back")
            ) { player, menu ->
                menu.close(player)
            }
        )

        // TODO: Implement Slots
        for (test in plugin.configYml.getSubsections("stats-gui.custom-slots")) {
            setSlot(
                test.getInt("row"),
                test.getInt("column"),
                ConfigSlot(test)
            )
        }
    }
}

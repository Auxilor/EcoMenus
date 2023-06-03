package com.willfp.ecomenus.menus

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.registry.KRegistrable
import com.willfp.ecomenus.commands.DynamicMenuCommand
import com.willfp.libreforge.EmptyProvidedHolder
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.conditions.Conditions
import org.bukkit.entity.Player

class EcoMenu(
    private val plugin: EcoPlugin,
    override val id: String,
    val config: Config
) : KRegistrable {
    private val menu = buildMenu(plugin, this, config)

    private val commandName = config.getStringOrNull("command")

    private val conditions = Conditions.compile(
        config.getSubsections("conditions"),
        ViolationContext(plugin, "Menu $id conditions")
    )

    private val cannotOpenMessages = config.getFormattedStrings("cannot-open-messages")

    init {
        if (commandName != null) {
            DynamicMenuCommand(plugin, this, commandName).register()
        }
    }

    fun open(player: Player, parent: Menu? = null) {
        if (!conditions.areMet(player, EmptyProvidedHolder)) {
            for (message in cannotOpenMessages) {
                player.sendMessage(message)
            }
            return
        }

        forceOpen(player, parent)
    }

    fun forceOpen(player: Player, parent: Menu? = null) {
        menu.open(player, parent)
    }
}

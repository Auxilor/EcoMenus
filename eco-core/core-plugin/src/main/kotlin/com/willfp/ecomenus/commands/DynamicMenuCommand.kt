package com.willfp.ecomenus.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.ecomenus.menus.EcoMenu
import org.bukkit.entity.Player

class DynamicMenuCommand(
    plugin: EcoPlugin,
    private val menu: EcoMenu,
    commandName: String
) : PluginCommand(
    plugin,
    commandName,
    "ecomenus.menus.${menu.id}.command",
    true
) {
    override fun onExecute(sender: Player, args: List<String>) {
        menu.open(sender)
    }
}

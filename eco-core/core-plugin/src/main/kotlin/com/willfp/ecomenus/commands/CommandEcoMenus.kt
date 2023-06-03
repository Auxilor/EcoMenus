package com.willfp.ecomenus.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import org.bukkit.command.CommandSender

class CommandEcoMenus(plugin: EcoPlugin) : PluginCommand(
    plugin,
    "ecomenus",
    "ecomenus.command.ecomenus",
    false
) {
    init {
        this.addSubcommand(CommandReload(plugin))
            .addSubcommand(CommandOpen(plugin))
            .addSubcommand(CommandForceOpen(plugin))
    }

    override fun onExecute(sender: CommandSender, args: List<String>) {
        sender.sendMessage(
            plugin.langYml.getMessage("invalid-command")
        )
    }
}

package com.willfp.ecomenus.commands

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.ecomenus.plugin
import org.bukkit.command.CommandSender

object CommandEcoMenus : PluginCommand(
    plugin,
    "ecomenus",
    "ecomenus.command.ecomenus",
    false
) {
    init {
        this.addSubcommand(CommandReload)
            .addSubcommand(CommandOpen)
            .addSubcommand(CommandForceOpen)
    }

    override fun onExecute(sender: CommandSender, args: List<String>) {
        sender.sendMessage(
            plugin.langYml.getMessage("invalid-command")
        )
    }
}

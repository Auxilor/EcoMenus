package com.willfp.ecomenus.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.eco.util.StringUtils
import com.willfp.eco.util.savedDisplayName
import com.willfp.ecomenus.menus.EcoMenus
import org.bukkit.command.CommandSender
import org.bukkit.util.StringUtil

class CommandForceOpen(plugin: EcoPlugin) : Subcommand(
    plugin,
    "forceopen",
    "ecomenus.command.forceopen",
    false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        val menu = notifyNull(EcoMenus[args.getOrNull(0)], "invalid-menu")
        val player = notifyPlayerRequired(args.getOrNull(1), "invalid-player")

        menu.forceOpen(player)
        sender.sendMessage(
            plugin.langYml.getMessage("opened", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                .replace("%player%", player.savedDisplayName)
                .replace("%menu%", menu.id)
        )
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        val completions = mutableListOf<String>()

        if (args.size == 1) {
            StringUtil.copyPartialMatches(
                args[0],
                EcoMenus.values().map { it.id },
                completions
            )
        }

        if (args.size == 2) {
            StringUtil.copyPartialMatches(
                args[1],
                plugin.server.onlinePlayers.map { it.name },
                completions
            )
        }

        return completions
    }
}

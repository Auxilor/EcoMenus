package com.willfp.ecomenus.commands

import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.ecomenus.menus.EcoMenus
import com.willfp.ecomenus.plugin
import com.willfp.ecomenus.runOwned
import org.bukkit.command.CommandSender
import org.bukkit.util.StringUtil
import wiki.ecohub.game.entity.player.PlayerUtils
import wiki.ecohub.ui.text.StringUtils

object CommandOpen : Subcommand(
    plugin,
    "open",
    "ecomenus.command.open",
    false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        val menu = notifyNull(EcoMenus[args.getOrNull(0)], "invalid-menu")
        val player = notifyPlayerRequired(args.getOrNull(1), "invalid-player")

        player.runOwned {
            sender.sendMessage(
                plugin.langYml.getMessage(
                    if (menu.open(player)) "opened" else "could-not-open",
                    StringUtils.FormatOption.WITHOUT_PLACEHOLDERS
                )
                    .replace("%player%", PlayerUtils.getSavedDisplayName(player))
                    .replace("%menu%", menu.id)
            )
        }
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

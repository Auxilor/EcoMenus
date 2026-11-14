package com.willfp.ecomenus.commands

import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder
import wiki.ecohub.game.entity.player.PlayerUtils
import wiki.ecohub.game.entity.runOwned
import wiki.ecohub.ui.text.StringUtils

internal fun CommandBuilder<CommandSender>.openCommand() = subcommand(
    name = "open",
    permission = "ecomenus.command.open"
) {
    executes {
        val (menu, player) = menuAndPlayer()

        player.runOwned {
            sender.sendMessage(
                plugin.langYml.getMessage(
                    if (menu.open(player)) "opened" else "could-not-open",
                    StringUtils.FormatOption.WITHOUT_PLACEHOLDERS
                )
                    .replace(oldValue = "%player%", newValue = PlayerUtils.getSavedDisplayName(player))
                    .replace(oldValue = "%menu%", newValue = menu.id)
            )
        }
    }

    completes {
        menuAndPlayerCompletions()
    }
}

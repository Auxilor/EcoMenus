package com.willfp.ecomenus.commands

import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder
import wiki.ecohub.game.entity.player.PlayerUtils
import wiki.ecohub.game.entity.runOwned

internal fun CommandBuilder<CommandSender>.forceOpenCommand() = subcommand(
    name = "forceopen",
    permission = "ecomenus.command.forceopen"
) {
    executes {
        val (menu, player) = menuAndPlayer()

        player.runOwned {
            menu.forceOpen(player)
            reply("opened", "player" to PlayerUtils.getSavedDisplayName(player), "menu" to menu.id)
        }
    }

    completes {
        menuAndPlayerCompletions()
    }
}

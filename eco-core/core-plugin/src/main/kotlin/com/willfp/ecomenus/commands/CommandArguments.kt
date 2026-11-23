package com.willfp.ecomenus.commands

import com.willfp.ecomenus.menus.EcoMenu
import com.willfp.ecomenus.menus.EcoMenus
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import wiki.ecohub.core.command.CommandContext

/**
 * The menu named by argument 0, then the online player named by argument 1.
 */
internal fun CommandContext<*>.menuAndPlayer(): Pair<EcoMenu, Player> {
    val menu = EcoMenus[args.getOrNull(0)] ?: fail("invalid-menu")
    val player = argument(1, "invalid-player", "invalid-player") { Bukkit.getPlayer(it) }

    return menu to player
}

internal fun CommandContext<*>.menuAndPlayerCompletions(): List<String> = when (args.size) {
    1 -> EcoMenus.values().map { it.id }
    2 -> playerNames()
    else -> emptyList()
}

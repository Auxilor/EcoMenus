package com.willfp.ecomenus.commands

import com.willfp.ecomenus.menus.EcoMenu
import com.willfp.ecomenus.plugin
import wiki.ecohub.core.command.EcoCommand
import wiki.ecohub.core.command.playerCommand

internal fun dynamicMenuCommand(menu: EcoMenu, commandName: String): EcoCommand =
    plugin.playerCommand(name = commandName, permission = "ecomenus.menus.${menu.id}.command") {
        executes {
            menu.open(sender)
        }
    }

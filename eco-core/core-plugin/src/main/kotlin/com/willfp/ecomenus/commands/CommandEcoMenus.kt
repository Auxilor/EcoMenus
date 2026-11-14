package com.willfp.ecomenus.commands

import com.willfp.ecomenus.EcoMenusPlugin
import wiki.ecohub.core.command.command

internal fun EcoMenusPlugin.ecoMenusCommand() = command(name = "ecomenus", permission = "ecomenus.command.ecomenus") {
    reloadCommand()
    openCommand()
    forceOpenCommand()
}

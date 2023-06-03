package com.willfp.ecomenus

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.ecomenus.commands.CommandEcoMenus
import com.willfp.ecomenus.config.ConfigCategory
import com.willfp.ecomenus.menus.EcoMenus

class EcoMenusPlugin : EcoPlugin() {
    private val categories = setOf<ConfigCategory>(
        EcoMenus
    )

    override fun handleEnable() {
        for (category in categories) {
            category.copyConfigs(this)
        }
    }

    override fun handleReload() {
        for (category in categories) {
            category.reload(this)
        }
    }

    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandEcoMenus(this)
        )
    }
}

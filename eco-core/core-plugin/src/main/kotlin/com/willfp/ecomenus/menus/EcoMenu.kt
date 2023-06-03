package com.willfp.ecomenus.menus

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.registry.KRegistrable
import com.willfp.ecomenus.commands.DynamicMenuCommand

class EcoMenu(
    private val plugin: EcoPlugin,
    override val id: String,
    val config: Config
) : KRegistrable {
    val menu = buildMenu(plugin, config)

    private val commandName = config.getStringOrNull("command")

    init {
        if (commandName != null) {
            DynamicMenuCommand(plugin, this, commandName).register()
        }
    }
}

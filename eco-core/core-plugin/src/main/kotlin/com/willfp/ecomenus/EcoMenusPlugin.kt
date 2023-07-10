package com.willfp.ecomenus

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.ecomenus.commands.CommandEcoMenus
import com.willfp.ecomenus.libreforge.EffectOpenMenu
import com.willfp.ecomenus.menus.EcoMenus
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory
import net.kyori.adventure.text.logger.slf4j.ComponentLogger

class EcoMenusPlugin : LibreforgePlugin() {
    override fun handleEnable() {
        Effects.register(EffectOpenMenu)
    }

    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandEcoMenus(this)
        )
    }

    override fun loadConfigCategories(): List<ConfigCategory> {
        return listOf(
            EcoMenus
        )
    }
}

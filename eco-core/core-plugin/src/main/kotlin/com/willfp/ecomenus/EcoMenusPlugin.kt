package com.willfp.ecomenus

import com.willfp.eco.core.bstats.EcoMetricsChart
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.ecomenus.commands.CommandEcoMenus
import com.willfp.ecomenus.libreforge.EffectOpenMenu
import com.willfp.ecomenus.libreforge.EffectResetPreviousMenu
import com.willfp.ecomenus.menus.EcoMenus
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory

internal lateinit var plugin: EcoMenusPlugin
    private set

class EcoMenusPlugin : LibreforgePlugin() {
    init {
        plugin = this
    }

    override fun handleEnable() {
        Effects.register(EffectOpenMenu)
        Effects.register(EffectResetPreviousMenu)
    }

    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandEcoMenus
        )
    }

    override fun loadConfigCategories(): List<ConfigCategory> {
        return listOf(
            EcoMenus
        )
    }

    override fun getCustomCharts() = listOf(
        EcoMetricsChart.SingleLine("total_menus") { EcoMenus.values().size }
    )
}

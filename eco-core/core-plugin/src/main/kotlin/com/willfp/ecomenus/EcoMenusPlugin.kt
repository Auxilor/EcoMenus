package com.willfp.ecomenus

import com.willfp.ecomenus.commands.ecoMenusCommand
import com.willfp.ecomenus.libreforge.EffectOpenMenu
import com.willfp.ecomenus.libreforge.EffectResetPreviousMenu
import com.willfp.ecomenus.menus.EcoMenus
import wiki.ecohub.config.category.NativeConfigCategory
import wiki.ecohub.core.command.EcoCommand
import wiki.ecohub.core.plugin.EcoPlugin
import wiki.ecohub.provider.metrics.EcoMetricsChart
import wiki.ecohub.rules.effect.Effects

internal lateinit var plugin: EcoMenusPlugin
    private set

class EcoMenusPlugin : EcoPlugin() {
    init {
        plugin = this
    }

    override fun handleEnable() {
        Effects.register(EffectOpenMenu)
        Effects.register(EffectResetPreviousMenu)
    }

    override fun loadPluginCommands(): List<EcoCommand> {
        return listOf(
            ecoMenusCommand()
        )
    }

    override fun loadConfigCategories(): List<NativeConfigCategory> {
        return listOf(
            EcoMenus
        )
    }

    override fun getCustomCharts() = listOf(
        EcoMetricsChart.SingleLine("total_menus") { EcoMenus.values().size }
    )
}

package com.willfp.ecomenus.menus

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.registry.Registry
import com.willfp.ecomenus.config.ConfigCategory

object EcoMenus : ConfigCategory("menus") {
    private val registry = Registry<EcoMenu>()

    override fun clear(plugin: EcoPlugin) {
        registry.clear()
    }

    override fun acceptConfig(plugin: EcoPlugin, id: String, config: Config) {
        registry.register(EcoMenu(plugin, id, config))
    }

    fun values(): Collection<EcoMenu> {
        return registry.values()
    }

    operator fun get(id: String): EcoMenu? {
        return registry.get(id)
    }
}

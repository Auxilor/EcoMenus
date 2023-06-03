package com.willfp.ecomenus.menus

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.registry.Registry
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory

object EcoMenus : ConfigCategory("menu", "menus") {
    private val registry = Registry<EcoMenu>()

    override fun clear(plugin: LibreforgePlugin) {
        registry.clear()
    }

    override fun acceptConfig(plugin: LibreforgePlugin, id: String, config: Config) {
        registry.register(EcoMenu(plugin, id, config))
    }

    fun values(): Collection<EcoMenu> {
        return registry.values()
    }

    operator fun get(id: String): EcoMenu? {
        return registry.get(id)
    }
}

package com.willfp.ecomenus.menus

import wiki.ecohub.config.Config
import wiki.ecohub.config.category.NativeConfigCategory
import wiki.ecohub.core.plugin.EcoPlugin
import wiki.ecohub.core.registry.Registry

object EcoMenus : ConfigCategory("menu", "menus") {
    private val registry = Registry<EcoMenu>()

    override fun clear(plugin: LibreforgePlugin) {
        registry.values().forEach { it.dispose() }
        registry.clear()
    }

    override fun acceptConfig(plugin: LibreforgePlugin, id: String, config: Config) {
        registry.register(EcoMenu(id, config))
    }

    fun values(): Collection<EcoMenu> {
        return registry.values()
    }

    operator fun get(id: String?): EcoMenu? {
        return registry.get(id ?: return null)
    }
}

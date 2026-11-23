package com.willfp.ecomenus.menus

import wiki.ecohub.config.Config
import wiki.ecohub.config.category.NativeConfigCategory
import wiki.ecohub.core.plugin.EcoPlugin
import wiki.ecohub.core.registry.Registry

object EcoMenus : NativeConfigCategory("menus", "menu") {
    private val registry = Registry<EcoMenu>()

    override fun clear(plugin: EcoPlugin) {
        registry.values().forEach { it.dispose() }
        registry.clear()
    }

    override fun acceptConfig(plugin: EcoPlugin, id: String, config: Config) {
        registry.register(EcoMenu(id, config))
    }

    fun values(): Collection<EcoMenu> {
        return registry.values()
    }

    operator fun get(id: String?): EcoMenu? {
        return registry[id ?: return null]
    }
}

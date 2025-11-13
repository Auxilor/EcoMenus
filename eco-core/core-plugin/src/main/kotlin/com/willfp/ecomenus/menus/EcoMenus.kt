package com.willfp.ecomenus.menus

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.registry.Registry
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory
import org.bukkit.Bukkit

object EcoMenus : ConfigCategory("menu", "menus") {
    private val registry = Registry<EcoMenu>()

    override fun clear(plugin: LibreforgePlugin) {
        registry.clear()
    }

    override fun acceptConfig(plugin: LibreforgePlugin, id: String, config: Config) {
        registry.register(EcoMenu(plugin, id, config))
    }

    override fun afterReload(plugin: LibreforgePlugin) {
        // Schedule command registration on the next tick to avoid
        // ConcurrentModificationException with Paper's async command system
        Bukkit.getScheduler().runTask(plugin) { _ ->
            registry.values().forEach { it.registerCommand() }
        }
    }

    fun values(): Collection<EcoMenu> {
        return registry.values()
    }

    operator fun get(id: String?): EcoMenu? {
        return registry.get(id ?: return null)
    }
}

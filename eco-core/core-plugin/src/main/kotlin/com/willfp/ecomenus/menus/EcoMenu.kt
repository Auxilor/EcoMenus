package com.willfp.ecomenus.menus

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.registry.KRegistrable
import com.willfp.eco.core.scheduling.EcoWrappedTask
import com.willfp.eco.util.openMenu
import com.willfp.ecomenus.commands.DynamicMenuCommand
import com.willfp.ecomenus.plugin
import com.willfp.libreforge.EmptyProvidedHolder
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.effects.executors.impl.NormalExecutorFactory
import com.willfp.libreforge.toDispatcher
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

class EcoMenu(
    override val id: String,
    config: Config
) : KRegistrable {
    private val menu = buildMenu(plugin, this, config)

    private val commandName = config.getStringOrNull("command")

    private val conditions = Conditions.compile(
        config.getSubsections("conditions"),
        ViolationContext(plugin, "menu $id conditions")
    )

    private val cannotOpenMessages = config.getFormattedStrings("cannot-open-messages")

    private val openEffects = Effects.compileChain(
        config.getSubsections("open-effects"),
        NormalExecutorFactory.create(),
        ViolationContext(plugin, "menu $id open effects")
    )

    private val closeEffects = Effects.compileChain(
        config.getSubsections("close-effects"),
        NormalExecutorFactory.create(),
        ViolationContext(plugin, "menu $id close effects")
    )

    private val refreshEnabled = config.getBool("refresh.enabled")
    private val refreshInterval = config.getInt("refresh.interval").toLong().coerceAtLeast(1)

    private var refreshTask: EcoWrappedTask? = null

    init {
        if (commandName != null) {
            DynamicMenuCommand(this, commandName).register()
        }
        if (refreshEnabled) {
            refreshTask = plugin.scheduler.runTaskTimer(refreshInterval, refreshInterval) {
                Bukkit.getOnlinePlayers()
                    .filter { it.openMenu == menu }
                    .forEach { menu.refresh(it) }
            }
        }
    }

    fun dispose() {
        refreshTask?.cancelTask()
    }

    fun open(player: Player, parent: Menu? = null) {
        if (!conditions.areMet(player.toDispatcher(), EmptyProvidedHolder)) {
            for (message in cannotOpenMessages) {
                player.sendMessage(message)
            }
            return
        }

        forceOpen(player, parent)
    }

    fun forceOpen(player: Player, parent: Menu? = null) {
        menu.open(player, parent)
        openEffects?.trigger(player.toDispatcher())
    }

    fun handleClose(player: Player) {
        closeEffects?.trigger(player.toDispatcher())
        val prev = menu.previousMenus[player].popOrNull()
        plugin.scheduler.runTaskLater(1) {
            if (prev != null && prev != menu) {
                prev.open(player)
            }
        }
    }
}

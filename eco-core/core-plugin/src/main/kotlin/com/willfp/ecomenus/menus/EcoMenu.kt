package com.willfp.ecomenus.menus

import com.willfp.ecomenus.commands.dynamicMenuCommand
import com.willfp.ecomenus.plugin
import com.willfp.ecomenus.runOwned
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import wiki.ecohub.config.Config
import wiki.ecohub.config.ConfigViolation
import wiki.ecohub.config.ViolationContext
import wiki.ecohub.core.command.EcoCommand
import wiki.ecohub.core.registry.KRegistrable
import wiki.ecohub.core.scheduler.EcoTask
import wiki.ecohub.game.entity.toDispatcher
import wiki.ecohub.modules.menus.menu.Menu
import wiki.ecohub.modules.menus.openMenu
import wiki.ecohub.rules.chain.NormalExecutorFactory
import wiki.ecohub.rules.condition.Conditions
import wiki.ecohub.rules.effect.Effects
import wiki.ecohub.rules.holder.EmptyProvidedHolder

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
    private val refreshInterval = config.getInt("refresh.interval").also { interval ->
        if (refreshEnabled && interval <= 0) {
            ViolationContext(plugin, "menu $id").log(
                ConfigViolation(
                    param = "refresh.interval",
                    message = "Refresh interval must be at least 1, refreshing every tick!"
                )
            )
        }
    }.toLong().coerceAtLeast(1)

    private var refreshTask: EcoTask? = null

    private var command: EcoCommand? = null

    init {
        if (commandName != null) {
            command = dynamicMenuCommand(this, commandName).apply { register() }
        }
        if (refreshEnabled) {
            refreshTask = plugin.scheduler.global().runTimer(refreshInterval, refreshInterval) {
                for (player in Bukkit.getOnlinePlayers()) {
                    player.runOwned {
                        if (player.isOnline && player.openMenu == menu) {
                            menu.refresh(player)
                        }
                    }
                }
            }
        }
    }

    fun dispose() {
        refreshTask?.cancel()
        command?.unregister()
    }

    fun open(player: Player, parent: Menu? = null): Boolean {
        if (!conditions.areMet(player.toDispatcher(), EmptyProvidedHolder)) {
            for (message in cannotOpenMessages) {
                player.sendMessage(message)
            }
            return false
        }

        forceOpen(player, parent)
        return true
    }

    fun forceOpen(player: Player, parent: Menu? = null) {
        menu.open(player, parent)
        openEffects?.trigger(player.toDispatcher())
    }

    fun handleClose(player: Player) {
        // If another eco menu is being force-opened, this close was triggered by explicit
        // navigation (e.g. open_menu effect). forceRenderedInventory already points to the
        // new menu, so player.openMenu != menu. Skip the auto-back in that case.
        val openMenu = player.openMenu
        if (openMenu != null && openMenu != menu) return
        val prev = menu.previousMenus[player].popOrNull()
        if (prev == null || prev == menu) {
            closeEffects?.trigger(player.toDispatcher())
            return
        }
        plugin.scheduler.on(player).runLater(1) {
            prev.open(player)
        }
    }
}

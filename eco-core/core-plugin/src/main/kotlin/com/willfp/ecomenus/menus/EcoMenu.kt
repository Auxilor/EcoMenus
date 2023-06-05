package com.willfp.ecomenus.menus

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.registry.KRegistrable
import com.willfp.ecomenus.commands.DynamicMenuCommand
import com.willfp.libreforge.EmptyProvidedHolder
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.effects.executors.impl.NormalExecutorFactory
import org.bukkit.entity.Player

class EcoMenu(
    private val plugin: EcoPlugin,
    override val id: String,
    val config: Config
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

    init {
        if (commandName != null) {
            DynamicMenuCommand(plugin, this, commandName).register()
        }
    }

    fun open(player: Player, parent: Menu? = null) {
        if (!conditions.areMet(player, EmptyProvidedHolder)) {
            for (message in cannotOpenMessages) {
                player.sendMessage(message)
            }
            return
        }

        forceOpen(player, parent)
    }

    fun forceOpen(player: Player, parent: Menu? = null) {
        menu.open(player, parent)
        openEffects?.trigger(player)
    }

    fun handleClose(player: Player) {
        closeEffects?.trigger(player)
        menu.previousMenus[player].popOrNull()?.open(player)
    }
}

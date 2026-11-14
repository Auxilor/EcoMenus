package com.willfp.ecomenus.libreforge

import com.willfp.ecomenus.menus.EcoMenus
import com.willfp.ecomenus.runOwned
import wiki.ecohub.config.ArgType
import wiki.ecohub.config.Config
import wiki.ecohub.config.arguments
import wiki.ecohub.core.registry.NoCompileData
import wiki.ecohub.modules.menus.openMenu
import wiki.ecohub.rules.effect.Effect
import wiki.ecohub.rules.trigger.TriggerData
import wiki.ecohub.rules.trigger.TriggerParameter

object EffectOpenMenu : Effect<NoCompileData>("open_menu") {
    override val description = "Opens a menu for the player."

    override val categories = setOf("meta")

    override val parameters = setOf(
        TriggerParameter.PLAYER
    )

    override val arguments = arguments {
        require(
            "menu",
            "You must specify the menu ID!",
            description = "The ID of the menu to open for the player.",
            type = ArgType.STRING,
            example = "shop"
        )
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false

        val menu = EcoMenus[config.getString("menu")] ?: return false

        player.runOwned {
            menu.forceOpen(player, player.openMenu)
        }

        return true
    }
}

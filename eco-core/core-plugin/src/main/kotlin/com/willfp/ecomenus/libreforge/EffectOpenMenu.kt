package com.willfp.ecomenus.libreforge

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.openMenu
import com.willfp.ecomenus.menus.EcoMenus
import com.willfp.libreforge.ArgType
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter

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
            type = ArgType.STRING
        )
    }

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false

        val menu = EcoMenus[config.getString("menu")] ?: return false
        menu.forceOpen(player, player.openMenu)

        return true
    }
}

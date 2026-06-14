package com.willfp.ecomenus.libreforge

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.openMenu
import com.willfp.ecomenus.menus.previousMenus
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter

object EffectResetPreviousMenu : Effect<NoCompileData>("reset_previous_menu") {
    override val description = "Clears the player's history of previously opened menus, so the back button " +
        "will no longer return to them."

    override val categories = setOf("meta")

    override val parameters = setOf(
        TriggerParameter.PLAYER
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false

        player.openMenu?.previousMenus?.get(player)?.clear()

        return true
    }
}

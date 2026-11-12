package com.willfp.ecomenus.libreforge

import com.willfp.ecomenus.menus.previousMenus
import com.willfp.ecomenus.runOwned
import wiki.ecohub.config.Config
import wiki.ecohub.core.registry.NoCompileData
import wiki.ecohub.modules.menus.openMenu
import wiki.ecohub.rules.effect.Effect
import wiki.ecohub.rules.trigger.TriggerData
import wiki.ecohub.rules.trigger.TriggerParameter

object EffectResetPreviousMenu : Effect<NoCompileData>("reset_previous_menu") {
    override val description = "Clears the player's history of previously opened menus, so the back button " +
        "will no longer return to them."

    override val categories = setOf("meta")

    override val parameters = setOf(
        TriggerParameter.PLAYER
    )

    override fun onTrigger(config: Config, data: TriggerData, compileData: NoCompileData): Boolean {
        val player = data.player ?: return false

        player.runOwned {
            player.openMenu?.previousMenus?.get(player)?.clear()
        }

        return true
    }
}

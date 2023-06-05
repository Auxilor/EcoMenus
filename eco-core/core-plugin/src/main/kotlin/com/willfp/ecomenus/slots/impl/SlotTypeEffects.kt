package com.willfp.ecomenus.slots.impl

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot.Slot
import com.willfp.ecomenus.slots.SlotFunction
import com.willfp.ecomenus.slots.SlotType
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.effects.Chain
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.effects.executors.ChainExecutors
import com.willfp.libreforge.triggers.DispatchedTrigger
import com.willfp.libreforge.triggers.Trigger
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

object SlotTypeEffects : SlotType("effects") {
    override fun create(config: Config, plugin: EcoPlugin, context: ViolationContext): SlotFunction? {
        val chain = Effects.compileChain(
            config.getSubsections("effects"),
            ChainExecutors.getByID(config.getString("run-type")),
            context
        ) ?: return null

        return SlotFunctionEffects(chain)
    }

    private class SlotFunctionEffects(
        private val chain: Chain
    ) : SlotFunction {
        override fun execute(player: Player, event: InventoryClickEvent, slot: Slot, menu: Menu) {
            chain.trigger(player)
        }
    }
}

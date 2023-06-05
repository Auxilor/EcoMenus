package com.willfp.ecomenus.slots

import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot.Slot
import com.willfp.ecomponent.SlotAction
import com.willfp.libreforge.EmptyProvidedHolder
import com.willfp.libreforge.conditions.ConditionList
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

interface SlotFunction {
    fun execute(player: Player, event: InventoryClickEvent, slot: Slot, menu: Menu)

    fun toSlotAction(conditions: ConditionList): SlotAction = { player, event, slot, menu ->
        if (conditions.areMet(player, EmptyProvidedHolder)) {
            this.execute(player, event, slot, menu)
        }
    }
}

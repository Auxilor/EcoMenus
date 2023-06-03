package com.willfp.ecomenus.slots.impl

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot.Slot
import com.willfp.ecomenus.menus.EcoMenu
import com.willfp.ecomenus.menus.EcoMenus
import com.willfp.ecomenus.menus.open
import com.willfp.ecomenus.slots.SlotFunction
import com.willfp.ecomenus.slots.SlotType
import com.willfp.libreforge.ViolationContext
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

object SlotTypeMenu : SlotType("menu") {
    override fun create(config: Config, plugin: EcoPlugin, context: ViolationContext): SlotFunction? {
        val ecoMenu = EcoMenus[config.getString("menu")] ?: return null

        return object : SlotFunction {
            override fun execute(player: Player, event: InventoryClickEvent, slot: Slot, menu: Menu) {
                ecoMenu.open(player, menu)
            }
        }
    }
}

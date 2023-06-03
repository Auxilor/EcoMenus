package com.willfp.ecomenus.slots.impl

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot.Slot
import com.willfp.ecomenus.slots.SlotFunction
import com.willfp.ecomenus.slots.SlotType
import com.willfp.libreforge.ViolationContext
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

object SlotTypeConsoleCommand : SlotType("console_command") {
    override fun create(config: Config, plugin: EcoPlugin, context: ViolationContext): SlotFunction {
        val commands = config.getStrings("commands")

        return CommandSlotFunction(commands, Bukkit.getConsoleSender())
    }
}

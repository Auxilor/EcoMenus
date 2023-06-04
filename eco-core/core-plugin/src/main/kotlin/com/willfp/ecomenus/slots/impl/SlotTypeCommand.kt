package com.willfp.ecomenus.slots.impl

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot.Slot
import com.willfp.eco.core.integrations.placeholder.PlaceholderManager
import com.willfp.eco.core.placeholder.context.placeholderContext
import com.willfp.ecomenus.slots.SlotFunction
import com.willfp.ecomenus.slots.SlotType
import com.willfp.libreforge.ViolationContext
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

object SlotTypeCommand : SlotType("command") {
    override fun create(config: Config, plugin: EcoPlugin, context: ViolationContext): SlotFunction {
        val commands = config.getStrings("commands")

        return CommandSlotFunction(commands)
    }
}

class CommandSlotFunction(
    private val commands: List<String>,
    private val sender: CommandSender? = null
) : SlotFunction {
    override fun execute(player: Player, event: InventoryClickEvent, slot: Slot, menu: Menu) {
        for (command in commands) {
            Bukkit.dispatchCommand(
                sender ?: player,
                PlaceholderManager.translatePlaceholders(
                    command.replace("%player%", player.name),
                    placeholderContext(player = player)
                )
            )
        }
    }
}

package com.willfp.ecomenus.slots

import com.willfp.eco.core.registry.Registry
import com.willfp.ecomenus.slots.impl.SlotTypeClose
import com.willfp.ecomenus.slots.impl.SlotTypeCommand
import com.willfp.ecomenus.slots.impl.SlotTypeConsoleCommand
import com.willfp.ecomenus.slots.impl.SlotTypeEffects
import com.willfp.ecomenus.slots.impl.SlotTypeMenu

object SlotTypes : Registry<SlotType>() {
    init {
        register(SlotTypeCommand)
        register(SlotTypeConsoleCommand)
        register(SlotTypeMenu)
        register(SlotTypeClose)
        register(SlotTypeEffects)
    }
}

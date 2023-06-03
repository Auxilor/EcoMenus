package com.willfp.ecomenus.slots

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.gui.slot.SlotBuilder
import com.willfp.eco.core.registry.KRegistrable
import com.willfp.ecomponent.SlotAction
import com.willfp.libreforge.ViolationContext

abstract class SlotType(override val id: String) : KRegistrable {
    abstract fun create(config: Config, plugin: EcoPlugin, context: ViolationContext): SlotFunction?
}

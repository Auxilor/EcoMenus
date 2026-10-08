package com.willfp.ecomenus

import com.willfp.eco.core.Eco
import com.willfp.eco.core.Prerequisite
import org.bukkit.Bukkit
import org.bukkit.entity.Entity

/**
 * Run [block] on the region owning this entity: now if the current thread already owns it,
 * which is always the case off Folia, otherwise on the entity's next tick.
 */
internal inline fun Entity.runOwned(crossinline block: () -> Unit) {
    if (Eco.get().isOwnedByCurrentRegion(this)) {
        block()
    } else {
        plugin.scheduler.on(this).run { block() }
    }
}

/**
 * Run [block] on the global region: now if this thread is already the global region
 * thread, which is always the case off Folia, otherwise on the next global tick.
 */
internal inline fun runOnGlobalRegion(crossinline block: () -> Unit) {
    if (Prerequisite.HAS_FOLIA.isMet && !Bukkit.isGlobalTickThread()) {
        plugin.scheduler.global().run { block() }
    } else {
        block()
    }
}

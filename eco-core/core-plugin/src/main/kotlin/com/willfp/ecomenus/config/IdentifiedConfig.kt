package com.willfp.ecomenus.config

import com.willfp.eco.core.config.interfaces.Config

internal data class IdentifiedConfig(
    val config: Config,
    val id: String
)

package dev.warpsend.core.model

import kotlinx.serialization.Serializable

@Serializable
data class PairingPayload(
    val id: String,
    val displayName: String,
    val host: String,
    val port: Int,
    val protocolVersion: Int = 1,
    val publicKey: String? = null // For future encryption support
)

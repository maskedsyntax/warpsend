package dev.warpsend.core.model

data class Device(
    val id: String,
    val displayName: String,
    val host: String,
    val port: Int,
    val lastSeenEpochMillis: Long
)

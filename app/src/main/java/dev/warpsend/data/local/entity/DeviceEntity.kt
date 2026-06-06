package dev.warpsend.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val host: String,
    val port: Int,
    val lastSeenEpochMillis: Long,
    val isPaired: Boolean = false
)

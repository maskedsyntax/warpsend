package dev.warpsend.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.warpsend.core.model.TransferDirection
import dev.warpsend.core.model.TransferStatus

@Entity(tableName = "transfer_sessions")
data class TransferSessionEntity(
    @PrimaryKey val id: String,
    val peerDeviceId: String,
    val direction: TransferDirection,
    val status: TransferStatus,
    val totalBytes: Long,
    val transferredBytes: Long,
    val createdAt: Long,
    val updatedAt: Long
)

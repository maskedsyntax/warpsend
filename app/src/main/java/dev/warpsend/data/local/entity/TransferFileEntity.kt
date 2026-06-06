package dev.warpsend.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.warpsend.core.model.FileStatus

@Entity(
    tableName = "transfer_files",
    foreignKeys = [
        ForeignKey(
            entity = TransferSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["sessionId"])]
)
data class TransferFileEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val name: String,
    val uri: String,
    val size: Long,
    val mimeType: String,
    val status: FileStatus,
    val transferredBytes: Long,
    val checksum: String?
)

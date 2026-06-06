package dev.warpsend.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "file_chunks",
    primaryKeys = ["fileId", "index"],
    foreignKeys = [
        ForeignKey(
            entity = TransferFileEntity::class,
            parentColumns = ["id"],
            childColumns = ["fileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["fileId"])]
)
data class FileChunkEntity(
    val fileId: String,
    val index: Int,
    val startByte: Long,
    val endByte: Long,
    val size: Int,
    val isCompleted: Boolean,
    val checksum: String?
)

package dev.warpsend.data.local.converter

import androidx.room.TypeConverter
import dev.warpsend.core.model.FileStatus
import dev.warpsend.core.model.TransferDirection
import dev.warpsend.core.model.TransferStatus

class WarpSendConverters {
    @TypeConverter
    fun fromTransferDirection(value: TransferDirection): String = value.name

    @TypeConverter
    fun toTransferDirection(value: String): TransferDirection = TransferDirection.valueOf(value)

    @TypeConverter
    fun fromTransferStatus(value: TransferStatus): String = value.name

    @TypeConverter
    fun toTransferStatus(value: String): TransferStatus = TransferStatus.valueOf(value)

    @TypeConverter
    fun fromFileStatus(value: FileStatus): String = value.name

    @TypeConverter
    fun toFileStatus(value: String): FileStatus = FileStatus.valueOf(value)
}

package dev.warpsend.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.warpsend.data.local.converter.WarpSendConverters
import dev.warpsend.data.local.dao.DeviceDao
import dev.warpsend.data.local.dao.TransferDao
import dev.warpsend.data.local.entity.DeviceEntity
import dev.warpsend.data.local.entity.TransferFileEntity
import dev.warpsend.data.local.entity.TransferSessionEntity

@Database(
    entities = [
        DeviceEntity::class,
        TransferSessionEntity::class,
        TransferFileEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(WarpSendConverters::class)
abstract class WarpSendDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun transferDao(): TransferDao
}

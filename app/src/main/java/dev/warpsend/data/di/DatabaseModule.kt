package dev.warpsend.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.warpsend.data.local.WarpSendDatabase
import dev.warpsend.data.local.dao.DeviceDao
import dev.warpsend.data.local.dao.TransferDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WarpSendDatabase {
        return Room.databaseBuilder(
            context,
            WarpSendDatabase::class.java,
            "warpsend.db"
        ).build()
    }

    @Provides
    fun provideDeviceDao(database: WarpSendDatabase): DeviceDao {
        return database.deviceDao()
    }

    @Provides
    fun provideTransferDao(database: WarpSendDatabase): TransferDao {
        return database.transferDao()
    }
}

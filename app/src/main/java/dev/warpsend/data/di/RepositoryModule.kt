package dev.warpsend.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.warpsend.data.repository.DeviceRepositoryImpl
import dev.warpsend.data.repository.TransferRepositoryImpl
import dev.warpsend.domain.DeviceRepository
import dev.warpsend.domain.TransferRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDeviceRepository(
        deviceRepositoryImpl: DeviceRepositoryImpl
    ): DeviceRepository

    @Binds
    @Singleton
    abstract fun bindTransferRepository(
        transferRepositoryImpl: TransferRepositoryImpl
    ): TransferRepository
}

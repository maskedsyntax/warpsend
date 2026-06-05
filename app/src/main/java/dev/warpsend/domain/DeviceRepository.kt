package dev.warpsend.domain

import dev.warpsend.core.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun observeNearbyDevices(): Flow<List<Device>>
}

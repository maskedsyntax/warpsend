package dev.warpsend.domain

import dev.warpsend.core.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun observeNearbyDevices(): Flow<List<Device>>
    fun observePairedDevices(): Flow<List<Device>>
    suspend fun getDeviceById(id: String): Device?
    suspend fun saveDevice(device: Device)
    suspend fun deleteDevice(id: String)
    suspend fun setPaired(id: String, isPaired: Boolean)
}

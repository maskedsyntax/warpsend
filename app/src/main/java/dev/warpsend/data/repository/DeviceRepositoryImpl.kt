package dev.warpsend.data.repository

import dev.warpsend.core.model.Device
import dev.warpsend.data.local.dao.DeviceDao
import dev.warpsend.data.local.toDomain
import dev.warpsend.data.local.toEntity
import dev.warpsend.domain.DeviceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val deviceDao: DeviceDao
) : DeviceRepository {

    override fun observeNearbyDevices(): Flow<List<Device>> {
        // TODO: Integrate with NSD/UDP discovery
        return deviceDao.getAllDevices().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observePairedDevices(): Flow<List<Device>> {
        return deviceDao.getPairedDevices().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getDeviceById(id: String): Device? {
        return deviceDao.getDeviceById(id)?.toDomain()
    }

    override suspend fun saveDevice(device: Device) {
        deviceDao.insertDevice(device.toEntity())
    }

    override suspend fun deleteDevice(id: String) {
        deviceDao.deleteDevice(id)
    }

    override suspend fun setPaired(id: String, isPaired: Boolean) {
        deviceDao.setPaired(id, isPaired)
    }
}

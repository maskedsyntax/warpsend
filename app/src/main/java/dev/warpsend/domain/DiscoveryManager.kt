package dev.warpsend.domain

import dev.warpsend.core.model.Device
import kotlinx.coroutines.flow.Flow

interface DiscoveryManager {
    /**
     * Starts discovering other WarpSend devices on the network.
     */
    fun startDiscovery(): Flow<List<Device>>

    /**
     * Stops the discovery process.
     */
    fun stopDiscovery()

    /**
     * Registers this device as a WarpSend service on the network.
     */
    fun registerService(deviceName: String, port: Int)

    /**
     * Unregisters the local WarpSend service.
     */
    fun unregisterService()
}

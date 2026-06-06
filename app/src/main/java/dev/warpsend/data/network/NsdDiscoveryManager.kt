package dev.warpsend.data.network

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.warpsend.core.model.Device
import dev.warpsend.domain.DiscoveryManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NsdDiscoveryManager @Inject constructor(
    @ApplicationContext private val context: Context
) : DiscoveryManager {

    private val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    private val serviceType = "_warpsend._tcp."
    private var registrationListener: NsdManager.RegistrationListener? = null
    private var discoveryListener: NsdManager.DiscoveryListener? = null

    private val discoveredServices = mutableMapOf<String, Device>()

    override fun startDiscovery(): Flow<List<Device>> = callbackFlow {
        discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e("NsdDiscovery", "Discovery failed: $errorCode")
                nsdManager.stopServiceDiscovery(this)
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e("NsdDiscovery", "Stop discovery failed: $errorCode")
                nsdManager.stopServiceDiscovery(this)
            }

            override fun onDiscoveryStarted(serviceType: String) {
                Log.d("NsdDiscovery", "Discovery started")
            }

            override fun onDiscoveryStopped(serviceType: String) {
                Log.d("NsdDiscovery", "Discovery stopped")
            }

            override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                Log.d("NsdDiscovery", "Service found: ${serviceInfo.serviceName}")
                if (serviceInfo.serviceType == serviceType) {
                    nsdManager.resolveService(serviceInfo, object : NsdManager.ResolveListener {
                        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                            Log.e("NsdDiscovery", "Resolve failed: $errorCode")
                        }

                        override fun onServiceResolved(resolvedInfo: NsdServiceInfo) {
                            val device = Device(
                                id = resolvedInfo.serviceName, // Using service name as ID for now
                                displayName = resolvedInfo.serviceName,
                                host = resolvedInfo.host.hostAddress ?: "",
                                port = resolvedInfo.port,
                                lastSeenEpochMillis = System.currentTimeMillis()
                            )
                            discoveredServices[device.id] = device
                            trySend(discoveredServices.values.toList())
                        }
                    })
                }
            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                Log.d("NsdDiscovery", "Service lost: ${serviceInfo.serviceName}")
                discoveredServices.remove(serviceInfo.serviceName)
                trySend(discoveredServices.values.toList())
            }
        }

        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)

        awaitClose {
            stopDiscovery()
        }
    }

    override fun stopDiscovery() {
        discoveryListener?.let {
            nsdManager.stopServiceDiscovery(it)
            discoveryListener = null
        }
    }

    override fun registerService(deviceName: String, port: Int) {
        val serviceInfo = NsdServiceInfo().apply {
            this.serviceName = deviceName
            this.serviceType = this@NsdDiscoveryManager.serviceType
            this.port = port
        }

        registrationListener = object : NsdManager.RegistrationListener {
            override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
                Log.d("NsdDiscovery", "Service registered: ${serviceInfo.serviceName}")
            }

            override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.e("NsdDiscovery", "Registration failed: $errorCode")
            }

            override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
                Log.d("NsdDiscovery", "Service unregistered")
            }

            override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.e("NsdDiscovery", "Unregistration failed: $errorCode")
            }
        }

        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
    }

    override fun unregisterService() {
        registrationListener?.let {
            nsdManager.unregisterService(it)
            registrationListener = null
        }
    }
}

package dev.warpsend

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.warpsend.core.design.WarpSendTheme
import dev.warpsend.domain.DiscoveryManager
import dev.warpsend.presentation.navigation.WarpSendNavHost
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject lateinit var discoveryManager: DiscoveryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Register local device as a discoverable service
        // In a real app, this should probably be in a Foreground Service
        // For MVP, we'll register it while the app is active
        val deviceName = "${Build.MANUFACTURER} ${Build.MODEL}"
        discoveryManager.registerService(deviceName, 8080)

        setContent {
            WarpSendTheme {
                WarpSendNavHost()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        discoveryManager.unregisterService()
    }
}

package dev.warpsend.presentation.pairing

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.warpsend.core.model.Device
import dev.warpsend.domain.DeviceRepository
import dev.warpsend.domain.PairingManager
import dev.warpsend.presentation.pairing.components.QrScannerView
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val pairingManager: PairingManager,
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    fun processScannedCode(code: String, onPaired: (Device) -> Unit) {
        val payload = pairingManager.parsePairingQr(code)
        if (payload != null) {
            val device = Device(
                id = payload.id,
                displayName = payload.displayName,
                host = payload.host,
                port = payload.port,
                lastSeenEpochMillis = System.currentTimeMillis(),
                isPaired = true
            )
            // Save to repository
            // Note: We need a scope here, but since this is a one-off, we can use viewModelScope if we were in the VM.
            // I'll move this logic into a VM method.
        }
    }

    private val _pairingState = mutableStateOf<PairingState>(PairingState.Idle)
    val pairingState: State<PairingState> = _pairingState

    fun pairDevice(code: String, onPaired: (Device) -> Unit) {
        val payload = pairingManager.parsePairingQr(code)
        if (payload != null) {
            val device = Device(
                id = payload.id,
                displayName = payload.displayName,
                host = payload.host,
                port = payload.port,
                lastSeenEpochMillis = System.currentTimeMillis(),
                isPaired = true
            )
            _pairingState.value = PairingState.Pairing(device)
            // Save to DB
            // In a real app, we'd do a handshake here.
            onPaired(device)
        } else {
            _pairingState.value = PairingState.Error("Invalid QR Code")
        }
    }
}

sealed class PairingState {
    object Idle : PairingState()
    data class Pairing(val device: Device) : PairingState()
    data class Success(val device: Device) : PairingState()
    data class Error(val message: String) : PairingState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanRoute(
    onBackClick: () -> Unit,
    onPairingSuccess: (Device) -> Unit,
    viewModel: ScanViewModel = hiltViewModel()
) {
    var isScanning by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan QR Code") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isScanning) {
                QrScannerView(
                    onQrCodeScanned = { code ->
                        isScanning = false
                        viewModel.pairDevice(code) { device ->
                            onPairingSuccess(device)
                        }
                    }
                )
            }

            // Overlay for better UX
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Center the QR code in the frame",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

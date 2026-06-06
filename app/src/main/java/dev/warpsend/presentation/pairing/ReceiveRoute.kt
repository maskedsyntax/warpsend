package dev.warpsend.presentation.pairing

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.warpsend.core.model.PairingPayload
import dev.warpsend.domain.PairingManager
import javax.inject.Inject

@HiltViewModel
class ReceiveViewModel @Inject constructor(
    private val pairingManager: PairingManager
) : ViewModel() {
    
    fun generateQr(host: String, port: Int): Bitmap {
        val payload = PairingPayload(
            id = "${Build.MANUFACTURER} ${Build.MODEL}",
            displayName = "${Build.MANUFACTURER} ${Build.MODEL}",
            host = host,
            port = port
        )
        return pairingManager.generatePairingQr(payload)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiveRoute(
    onBackClick: () -> Unit,
    viewModel: ReceiveViewModel = hiltViewModel()
) {
    // For MVP, we'll use a hardcoded port. In a real app, this comes from the server.
    val qrBitmap = remember { viewModel.generateQr("0.0.0.0", 8080) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Receive Files") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Scan to pair",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ask the sender to scan this QR code to start the transfer",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(48.dp))
            Surface(
                modifier = Modifier.size(280.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        bitmap = qrBitmap.asImageBitmap(),
                        contentDescription = "Pairing QR Code",
                        modifier = Modifier.size(240.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
            CircularProgressIndicator(modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Waiting for connection...",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

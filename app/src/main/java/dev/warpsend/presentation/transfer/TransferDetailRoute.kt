package dev.warpsend.presentation.transfer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.warpsend.core.model.TransferFile
import dev.warpsend.core.model.TransferSession
import dev.warpsend.domain.TransferRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class TransferDetailViewModel @Inject constructor(
    private val transferRepository: TransferRepository
) : ViewModel() {
    private val _sessionId = MutableStateFlow<String?>(null)
    
    val uiState: StateFlow<TransferDetailUiState> = _sessionId
        .filterNotNull()
        .flatMapLatest { id ->
            transferRepository.observeFilesForSession(id).map { files ->
                val session = transferRepository.getSessionById(id)
                TransferDetailUiState(
                    session = session,
                    files = files
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TransferDetailUiState()
        )

    fun setSessionId(id: String) {
        _sessionId.value = id
    }
}

data class TransferDetailUiState(
    val session: TransferSession? = null,
    val files: List<TransferFile> = emptyList()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferDetailRoute(
    sessionId: String,
    onBackClick: () -> Unit,
    viewModel: TransferDetailViewModel = hiltViewModel()
) {
    viewModel.setSessionId(sessionId)
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transfer Progress") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        val session = uiState.session
        if (session == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            TransferDetailScreen(
                session = session,
                files = uiState.files,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
fun TransferDetailScreen(
    session: TransferSession,
    files: List<TransferFile>,
    modifier: Modifier = Modifier
) {
    val progress = if (session.totalBytes > 0) session.transferredBytes.toFloat() / session.totalBytes else 0f

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Overall Progress
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (progress < 1f) "Transferring..." else "Completed",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${formatSize(session.transferredBytes)} of ${formatSize(session.totalBytes)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    strokeCap = StrokeCap.Round
                )
            }
        }

        // Files List
        Text(
            text = "Files (${files.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(files) { file ->
                FileItem(file = file)
            }
        }

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Cancel Transfer")
        }
    }
}

@Composable
fun FileItem(file: TransferFile) {
    val progress = if (file.size > 0) file.transferredBytes.toFloat() / file.size else 0f
    
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = formatSize(file.size),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (progress > 0f && progress < 1f) {
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            if (progress >= 1f) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
            }
        }
    }
}

private fun formatSize(bytes: Long): String {
    val kb = bytes / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    return when {
        gb >= 1 -> "%.2f GB".format(gb)
        mb >= 1 -> "%.2f MB".format(mb)
        kb >= 1 -> "%.2f KB".format(kb)
        else -> "$bytes Bytes"
    }
}

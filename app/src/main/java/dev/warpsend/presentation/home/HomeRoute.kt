package dev.warpsend.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.warpsend.presentation.home.components.ActionCard
import dev.warpsend.presentation.home.components.SectionHeader
import dev.warpsend.presentation.home.components.StatItem

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun HomeRoute(
    onSendClick: (List<android.net.Uri>) -> Unit,
    onReceiveClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            onSendClick(uris)
        }
    }

    HomeScreen(
        uiState = uiState,
        onSendClick = { filePickerLauncher.launch("*/*") },
        onReceiveClick = onReceiveClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onSendClick: () -> Unit,
    onReceiveClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "History", "Settings")
    val icons = listOf(Icons.Default.Home, Icons.Default.History, Icons.Default.Settings)
    
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "WarpSend",
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            NavigationBar(
                tonalElevation = 8.dp
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Hero / Welcome Message
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "Welcome back!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ready to warp some files?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Quick Actions Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ActionCard(
                    title = "Send",
                    description = "Share files to nearby devices",
                    icon = Icons.AutoMirrored.Filled.Send,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = onSendClick,
                    modifier = Modifier.weight(1f)
                )
                ActionCard(
                    title = "Receive",
                    description = "Wait for incoming transfers",
                    icon = Icons.AutoMirrored.Filled.CallReceived,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    onClick = onReceiveClick,
                    modifier = Modifier.weight(1f)
                )
            }

            // Discovery Card (Enhanced)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Device Discovery",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Visible to others on this network",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = true, 
                        onCheckedChange = {},
                        thumbContent = {
                            if (true) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        }
                    )
                }
            }

            // Stats / Activity Section
            Column {
                SectionHeader(title = "Your Activity")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatItem(
                        label = "Nearby",
                        value = uiState.nearbyDeviceCount.toString(),
                        icon = Icons.Default.Devices,
                        modifier = Modifier.weight(1f)
                    )
                    StatItem(
                        label = "Active",
                        value = uiState.activeTransferCount.toString(),
                        icon = Icons.Default.SwapHoriz,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                StatItem(
                    label = "Total Transfers (History)",
                    value = uiState.recentTransferCount.toString(),
                    icon = Icons.Default.History,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

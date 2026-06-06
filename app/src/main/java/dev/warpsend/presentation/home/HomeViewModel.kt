package dev.warpsend.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.warpsend.domain.DeviceRepository
import dev.warpsend.domain.TransferRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeUiState(
    val nearbyDeviceCount: Int = 0,
    val activeTransferCount: Int = 0,
    val recentTransferCount: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    deviceRepository: DeviceRepository,
    transferRepository: TransferRepository
) : ViewModel() {
    val uiState: StateFlow<HomeUiState> = combine(
        deviceRepository.observeNearbyDevices(),
        transferRepository.observeActiveTransfers(),
        transferRepository.observeTransferHistory()
    ) { nearby, active, history ->
        HomeUiState(
            nearbyDeviceCount = nearby.size,
            activeTransferCount = active.size,
            recentTransferCount = history.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )
}

package dev.warpsend.presentation.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class HomeUiState(
    val nearbyDeviceCount: Int = 0,
    val activeTransferCount: Int = 0,
    val recentTransferCount: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val uiState = HomeUiState()
}

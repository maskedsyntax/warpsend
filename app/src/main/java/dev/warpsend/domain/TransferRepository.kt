package dev.warpsend.domain

import dev.warpsend.core.model.TransferSession
import kotlinx.coroutines.flow.Flow

interface TransferRepository {
    fun observeActiveTransfers(): Flow<List<TransferSession>>
}

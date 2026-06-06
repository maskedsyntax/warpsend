package dev.warpsend.domain

import dev.warpsend.core.model.TransferSession
import kotlinx.coroutines.flow.Flow

interface TransferEngine {
    /**
     * Starts the transfer engine for a given session.
     */
    fun startTransfer(session: TransferSession): Flow<TransferSession>

    /**
     * Pauses the current transfer.
     */
    fun pauseTransfer(sessionId: String)

    /**
     * Resumes a paused transfer.
     */
    fun resumeTransfer(sessionId: String)

    /**
     * Cancels the current transfer.
     */
    fun cancelTransfer(sessionId: String)
}

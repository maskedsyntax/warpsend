package dev.warpsend.domain

import dev.warpsend.core.model.FileChunk
import dev.warpsend.core.model.TransferFile
import dev.warpsend.core.model.TransferSession
import dev.warpsend.core.model.TransferStatus
import kotlinx.coroutines.flow.Flow

interface TransferRepository {
    fun observeActiveTransfers(): Flow<List<TransferSession>>
    fun observeTransferHistory(): Flow<List<TransferSession>>
    fun observeFilesForSession(sessionId: String): Flow<List<TransferFile>>
    suspend fun getSessionById(id: String): TransferSession?
    suspend fun createSession(session: TransferSession, files: List<TransferFile>)
    suspend fun updateSessionProgress(sessionId: String, status: TransferStatus, transferredBytes: Long)
    suspend fun deleteSession(id: String)
    
    suspend fun getChunksForFile(fileId: String): List<FileChunk>
    suspend fun updateChunkStatus(fileId: String, index: Int, isCompleted: Boolean)
    suspend fun saveChunks(chunks: List<FileChunk>)
}

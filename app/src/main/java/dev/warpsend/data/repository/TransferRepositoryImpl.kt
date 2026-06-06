package dev.warpsend.data.repository

import dev.warpsend.core.model.TransferFile
import dev.warpsend.core.model.TransferSession
import dev.warpsend.core.model.TransferStatus
import dev.warpsend.data.local.dao.TransferDao
import dev.warpsend.data.local.toDomain
import dev.warpsend.data.local.toEntity
import dev.warpsend.domain.TransferRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransferRepositoryImpl @Inject constructor(
    private val transferDao: TransferDao
) : TransferRepository {

    override fun observeActiveTransfers(): Flow<List<TransferSession>> {
        return transferDao.getActiveSessions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeTransferHistory(): Flow<List<TransferSession>> {
        return transferDao.getHistorySessions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeFilesForSession(sessionId: String): Flow<List<TransferFile>> {
        return transferDao.getFilesForSession(sessionId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getSessionById(id: String): TransferSession? {
        return transferDao.getSessionById(id)?.toDomain()
    }

    override suspend fun createSession(session: TransferSession, files: List<TransferFile>) {
        transferDao.insertSession(session.toEntity())
        transferDao.insertFiles(files.map { it.toEntity() })
    }

    override suspend fun updateSessionProgress(sessionId: String, status: TransferStatus, transferredBytes: Long) {
        transferDao.updateSessionProgress(sessionId, status, transferredBytes, System.currentTimeMillis())
    }

    override suspend fun deleteSession(id: String) {
        transferDao.deleteSession(id)
    }
}

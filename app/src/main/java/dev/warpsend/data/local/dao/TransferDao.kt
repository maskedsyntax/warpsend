package dev.warpsend.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.warpsend.data.local.entity.FileChunkEntity
import dev.warpsend.data.local.entity.TransferFileEntity
import dev.warpsend.data.local.entity.TransferSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransferDao {
    @Query("SELECT * FROM transfer_sessions ORDER BY createdAt DESC")
    fun getAllSessions(): Flow<List<TransferSessionEntity>>

    @Query("SELECT * FROM transfer_sessions WHERE status IN ('Queued', 'Running', 'Paused') ORDER BY createdAt DESC")
    fun getActiveSessions(): Flow<List<TransferSessionEntity>>

    @Query("SELECT * FROM transfer_sessions WHERE status IN ('Completed', 'Failed', 'Cancelled') ORDER BY createdAt DESC")
    fun getHistorySessions(): Flow<List<TransferSessionEntity>>

    @Query("SELECT * FROM transfer_sessions WHERE id = :sessionId")
    suspend fun getSessionById(sessionId: String): TransferSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: TransferSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFiles(files: List<TransferFileEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChunks(chunks: List<FileChunkEntity>)

    @Query("UPDATE file_chunks SET isCompleted = :isCompleted WHERE fileId = :fileId AND `index` = :index")
    suspend fun updateChunkStatus(fileId: String, index: Int, isCompleted: Boolean)

    @Query("SELECT * FROM file_chunks WHERE fileId = :fileId")
    suspend fun getChunksForFile(fileId: String): List<FileChunkEntity>

    @Query("SELECT * FROM transfer_files WHERE sessionId = :sessionId")
    fun getFilesForSession(sessionId: String): Flow<List<TransferFileEntity>>

    @Transaction
    @Query("DELETE FROM transfer_sessions WHERE id = :sessionId")
    suspend fun deleteSession(sessionId: String)

    @Query("UPDATE transfer_sessions SET status = :status, transferredBytes = :transferredBytes, updatedAt = :updatedAt WHERE id = :sessionId")
    suspend fun updateSessionProgress(sessionId: String, status: dev.warpsend.core.model.TransferStatus, transferredBytes: Long, updatedAt: Long)
}

package dev.warpsend.data.network

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.warpsend.core.model.*
import dev.warpsend.domain.TransferEngine
import dev.warpsend.domain.TransferRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.get
import io.ktor.utils.io.readFully
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KtorTransferEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val server: WarpSendServer,
    private val client: HttpClient,
    private val transferRepository: TransferRepository
) : TransferEngine {

    private val engineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _transferEvents = MutableSharedFlow<TransferSession>()
    val transferEvents = _transferEvents.asSharedFlow()

    private val chunkSize = 1024 * 1024 // 1MB

    override fun startTransfer(session: TransferSession): Flow<TransferSession> {
        if (session.direction == TransferDirection.Sent) {
            startServer(session)
        } else {
            engineScope.launch {
                startClient(session)
            }
        }
        return transferEvents
    }

    private fun startServer(session: TransferSession) {
        server.start(8080) {
            get("/manifest") {
                call.respond(session.files)
            }
            get("/chunk/{fileId}/{index}") {
                val fileId = call.parameters["fileId"] ?: ""
                val index = call.parameters["index"]?.toIntOrNull() ?: 0
                val file = session.files.find { it.id == fileId }
                
                if (file != null) {
                    val bytes = readFileChunk(file, index)
                    if (bytes != null) {
                        call.respondBytes(bytes)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }

    private suspend fun startClient(session: TransferSession) {
        val peerUrl = "http://${session.peerDeviceId}:8080"
        try {
            val files: List<TransferFile> = client.get("$peerUrl/manifest").body()
            var totalTransferred = 0L
            
            files.forEach { file ->
                val savedChunks = transferRepository.getChunksForFile(file.id)
                val chunkCount = (file.size + chunkSize - 1) / chunkSize
                
                for (i in 0 until chunkCount.toInt()) {
                    val isCompleted = savedChunks.find { it.index == i }?.isCompleted ?: false
                    if (isCompleted) {
                        totalTransferred += chunkSize.coerceAtMost((file.size - i.toLong() * chunkSize).toInt())
                        continue
                    }
                    
                    val response = client.get("$peerUrl/chunk/${file.id}/$i")
                    if (response.status == HttpStatusCode.OK) {
                        val bytes: ByteArray = response.body()
                        saveFileChunk(file, i, bytes)
                        transferRepository.updateChunkStatus(file.id, i, true)
                        totalTransferred += bytes.size
                        
                        // Emit progress
                        val updatedSession = session.copy(
                            transferredBytes = totalTransferred,
                            status = TransferStatus.Running
                        )
                        _transferEvents.emit(updatedSession)
                        transferRepository.updateSessionProgress(session.id, TransferStatus.Running, totalTransferred)
                    }
                }
            }
            
            val finalSession = session.copy(
                transferredBytes = totalTransferred,
                status = TransferStatus.Completed
            )
            _transferEvents.emit(finalSession)
            transferRepository.updateSessionProgress(session.id, TransferStatus.Completed, totalTransferred)
            
        } catch (e: Exception) {
            _transferEvents.emit(session.copy(status = TransferStatus.Failed))
            transferRepository.updateSessionProgress(session.id, TransferStatus.Failed, session.transferredBytes)
        }
    }

    private fun saveFileChunk(file: TransferFile, index: Int, bytes: ByteArray) {
        // TODO: Implement actual storage persistence using ContentResolver
        // This is where we write the chunk to the local file
    }

    private fun readFileChunk(file: TransferFile, index: Int): ByteArray? {
        return try {
            val uri = Uri.parse(file.uri)
            val chunkSize = 1024 * 1024 // 1MB
            val offset = index.toLong() * chunkSize
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.use {
                it.skip(offset)
                val buffer = ByteArray(chunkSize.coerceAtMost((file.size - offset).toInt()))
                val bytesRead = it.read(buffer)
                if (bytesRead > 0) buffer.copyOf(bytesRead) else null
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun pauseTransfer(sessionId: String) {
        // TODO
    }

    override fun resumeTransfer(sessionId: String) {
        // TODO
    }

    override fun cancelTransfer(sessionId: String) {
        server.stop()
    }
}

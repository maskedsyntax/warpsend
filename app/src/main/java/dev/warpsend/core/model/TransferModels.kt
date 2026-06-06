package dev.warpsend.core.model

enum class TransferDirection {
    Sent,
    Received
}

enum class TransferStatus {
    Queued,
    Running,
    Paused,
    Completed,
    Failed,
    Cancelled
}

enum class FileStatus {
    Pending,
    Transferring,
    Completed,
    Failed
}

data class TransferFile(
    val id: String,
    val sessionId: String,
    val name: String,
    val uri: String,
    val size: Long,
    val mimeType: String,
    val status: FileStatus,
    val transferredBytes: Long,
    val checksum: String? = null
)

data class FileChunk(
    val fileId: String,
    val index: Int,
    val startByte: Long,
    val endByte: Long,
    val size: Int,
    val isCompleted: Boolean = false,
    val checksum: String? = null
)

data class TransferSession(
    val id: String,
    val peerDeviceId: String,
    val direction: TransferDirection,
    val status: TransferStatus,
    val totalBytes: Long,
    val transferredBytes: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val files: List<TransferFile> = emptyList()
)

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
    val transferredBytes: Long
)

data class TransferSession(
    val id: String,
    val peerDeviceId: String,
    val direction: TransferDirection,
    val status: TransferStatus,
    val totalBytes: Long,
    val transferredBytes: Long,
    val createdAt: Long,
    val updatedAt: Long
)

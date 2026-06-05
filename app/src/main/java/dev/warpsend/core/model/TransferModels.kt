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

data class TransferSession(
    val id: String,
    val peerDeviceId: String,
    val direction: TransferDirection,
    val status: TransferStatus,
    val totalBytes: Long,
    val transferredBytes: Long
)

package dev.warpsend.data.local

import dev.warpsend.core.model.Device
import dev.warpsend.core.model.FileChunk
import dev.warpsend.core.model.TransferFile
import dev.warpsend.core.model.TransferSession
import dev.warpsend.data.local.entity.DeviceEntity
import dev.warpsend.data.local.entity.FileChunkEntity
import dev.warpsend.data.local.entity.TransferFileEntity
import dev.warpsend.data.local.entity.TransferSessionEntity

fun DeviceEntity.toDomain(): Device = Device(
    id = id,
    displayName = displayName,
    host = host,
    port = port,
    lastSeenEpochMillis = lastSeenEpochMillis,
    isPaired = isPaired
)

fun Device.toEntity(): DeviceEntity = DeviceEntity(
    id = id,
    displayName = displayName,
    host = host,
    port = port,
    lastSeenEpochMillis = lastSeenEpochMillis,
    isPaired = isPaired
)

fun TransferSessionEntity.toDomain(): TransferSession = TransferSession(
    id = id,
    peerDeviceId = peerDeviceId,
    direction = direction,
    status = status,
    totalBytes = totalBytes,
    transferredBytes = transferredBytes,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun TransferSession.toEntity(): TransferSessionEntity = TransferSessionEntity(
    id = id,
    peerDeviceId = peerDeviceId,
    direction = direction,
    status = status,
    totalBytes = totalBytes,
    transferredBytes = transferredBytes,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun TransferFileEntity.toDomain(): TransferFile = TransferFile(
    id = id,
    sessionId = sessionId,
    name = name,
    uri = uri,
    size = size,
    mimeType = mimeType,
    status = status,
    transferredBytes = transferredBytes,
    checksum = checksum
)

fun TransferFile.toEntity(): TransferFileEntity = TransferFileEntity(
    id = id,
    sessionId = sessionId,
    name = name,
    uri = uri,
    size = size,
    mimeType = mimeType,
    status = status,
    transferredBytes = transferredBytes,
    checksum = checksum
)

fun FileChunkEntity.toDomain(): FileChunk = FileChunk(
    fileId = fileId,
    index = index,
    startByte = startByte,
    endByte = endByte,
    size = size,
    isCompleted = isCompleted,
    checksum = checksum
)

fun FileChunk.toEntity(): FileChunkEntity = FileChunkEntity(
    fileId = fileId,
    index = index,
    startByte = startByte,
    endByte = endByte,
    size = size,
    isCompleted = isCompleted,
    checksum = checksum
)

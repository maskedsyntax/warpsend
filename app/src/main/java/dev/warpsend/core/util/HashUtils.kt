package dev.warpsend.core.util

import java.security.MessageDigest

object HashUtils {
    fun calculateMd5(bytes: ByteArray): String {
        val digest = MessageDigest.getInstance("MD5")
        val hash = digest.digest(bytes)
        return hash.joinToString("") { "%02x".format(it) }
    }

    fun calculateMd5(inputStream: java.io.InputStream): String {
        val digest = MessageDigest.getInstance("MD5")
        val buffer = ByteArray(8192)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            digest.update(buffer, 0, bytesRead)
        }
        val hash = digest.digest()
        return hash.joinToString("") { "%02x".format(it) }
    }
}

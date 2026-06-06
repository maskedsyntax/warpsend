package dev.warpsend.domain

import android.graphics.Bitmap
import dev.warpsend.core.model.PairingPayload

interface PairingManager {
    /**
     * Generates a QR code bitmap for the given pairing payload.
     */
    fun generatePairingQr(payload: PairingPayload): Bitmap

    /**
     * Parses a string (from a QR code) into a PairingPayload.
     * Returns null if the string is not a valid WarpSend pairing payload.
     */
    fun parsePairingQr(qrContent: String): PairingPayload?
}

package dev.warpsend.data.network

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import dev.warpsend.core.model.PairingPayload
import dev.warpsend.domain.PairingManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PairingManagerImpl @Inject constructor() : PairingManager {

    private val json = Json { ignoreUnknownKeys = true }

    override fun generatePairingQr(payload: PairingPayload): Bitmap {
        val qrContent = json.encodeToString(payload)
        val size = 512
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            qrContent,
            BarcodeFormat.QR_CODE,
            size,
            size
        )

        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }

    override fun parsePairingQr(qrContent: String): PairingPayload? {
        return try {
            json.decodeFromString<PairingPayload>(qrContent)
        } catch (e: Exception) {
            null
        }
    }
}

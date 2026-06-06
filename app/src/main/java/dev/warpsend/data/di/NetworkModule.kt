package dev.warpsend.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.warpsend.data.network.KtorTransferEngine
import dev.warpsend.data.network.NsdDiscoveryManager
import dev.warpsend.data.network.PairingManagerImpl
import dev.warpsend.domain.DiscoveryManager
import dev.warpsend.domain.PairingManager
import dev.warpsend.domain.TransferEngine
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindDiscoveryManager(
        nsdDiscoveryManager: NsdDiscoveryManager
    ): DiscoveryManager

    @Binds
    @Singleton
    abstract fun bindPairingManager(
        pairingManagerImpl: PairingManagerImpl
    ): PairingManager

    @Binds
    @Singleton
    abstract fun bindTransferEngine(
        ktorTransferEngine: KtorTransferEngine
    ): TransferEngine

    companion object {
        @Provides
        @Singleton
        fun provideHttpClient(): HttpClient {
            return HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    })
                }
            }
        }
    }
}

package dev.warpsend.data.network

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WarpSendServer @Inject constructor() {
    private var server: io.ktor.server.engine.EmbeddedServer<*, *>? = null

    fun start(port: Int, configure: io.ktor.server.routing.Routing.() -> Unit) {
        server = embeddedServer(CIO, port = port) {
            install(ContentNegotiation) {
                json()
            }
            routing {
                configure()
            }
        }.start(wait = false)
    }

    fun stop() {
        server?.stop(1000, 2000)
        server = null
    }
}

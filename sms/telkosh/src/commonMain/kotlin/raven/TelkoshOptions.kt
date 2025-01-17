package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import raven.internal.TelkoshEndpoint

class TelkoshOptions(
    val user: String,
    val password: String,
    val warning: CreditWarning,
    val outbox: Outbox<SendSmsParams>? = null,
    val http: HttpClient = DEFAULT_HTTP,
    val codec: StringFormat = DEFAULT_CODEC,
    val scope: CoroutineScope = DEFAULT_SCOPE
) {
    companion object {
        val DEFAULT_HTTP by lazy { HttpClient { } }
        val DEFAULT_CODEC by lazy { Json { } }
        val DEFAULT_SCOPE by lazy { CoroutineScope(SupervisorJob()) }
    }

//    internal val endpoint by lazy { TelkoshEndpoint("https://mshastra.com") }
    internal val endpoint by lazy { TelkoshEndpoint("http://login.telkosh.com") }
}
package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.StringFormat

class RemoteEmailOutboxOptions(
    val http: HttpClient,
    val scope: CoroutineScope,
    val endpoint: OutboxDestination,
    val codec: StringFormat
)
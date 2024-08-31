package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.StringFormat

class RemoteOutboxOptions<P>(
    val http: HttpClient,
    val scope: CoroutineScope,
    val endpoint: OutboxDestination,
    val serializer: KSerializer<P>,
    val codec: StringFormat
)
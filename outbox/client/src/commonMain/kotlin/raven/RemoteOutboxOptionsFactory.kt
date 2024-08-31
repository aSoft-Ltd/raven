package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.StringFormat
import kotlinx.serialization.serializer

inline fun <reified P> RemoteOutboxOptions(
    http: HttpClient,
    scope: CoroutineScope,
    endpoint: OutboxDestination,
    codec: StringFormat
) = RemoteOutboxOptions(http, scope, endpoint, serializer<P>(), codec)
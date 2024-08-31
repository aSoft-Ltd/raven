package raven

import kotlinx.serialization.StringFormat
import kotlinx.serialization.serializer

inline fun <reified P> OutboxController(
    service: Outbox<P>,
    endpoint: OutboxDestination,
    codec: StringFormat
) = OutboxController(service, endpoint, serializer(), codec)
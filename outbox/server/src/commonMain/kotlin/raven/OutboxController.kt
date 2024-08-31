package raven

import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat

class OutboxController<P>(
    val service: Outbox<P>,
    val endpoint: OutboxDestination,
    val serializer: KSerializer<P>,
    val codec: StringFormat
)
package raven

import kotlinx.serialization.StringFormat

class OutboxController(
    val service: EmailOutbox,
    val endpoint: OutboxDestination,
    val codec: StringFormat
)
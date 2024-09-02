package raven

import kotlinx.serialization.StringFormat
import raven.OutboxController
import raven.OutboxDestination

class TestAppController(
    val service: TestAppService,
    val codec: StringFormat
) {
    val sms by lazy {
        val outbox = service.outbox.sms ?: return@lazy null
        OutboxController(
            service = outbox,
            endpoint = OutboxDestination("/outbox/sms"),
            codec = codec
        )
    }
    val email by lazy {
        val outbox = service.outbox.email ?: return@lazy null
        OutboxController(
            service = outbox,
            endpoint = OutboxDestination("/outbox/email"),
            codec = codec
        )
    }
}
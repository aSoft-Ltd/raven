package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.StringFormat

fun AgentConfiguration<SendEmailParams>.toBrevoAgent(
    http: HttpClient,
    codec: StringFormat,
    scope: CoroutineScope,
    warning: (count: Int) -> String
) = BrevoEmailAgent(toBrevoAgentOptions(http, codec, scope, warning))

fun AgentConfiguration<SendEmailParams>.toBrevoAgentOptions(
    http: HttpClient,
    codec: StringFormat,
    scope: CoroutineScope,
    warning: (count: Int) -> String
) = BrevoOptions(
    key = params["key"] ?: throw BrevoEmailAgentException("Brevo api key mut be passed in the key option of Brevo Configuration"),
    warning = toCreditWarning(agent = "brevo", warning),
    http = http,
    outbox = outbox,
    codec = codec,
    scope = scope
)
package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.StringFormat

fun AgentConfiguration<SendEmailParams>.toMailgunAgent(
    http: HttpClient,
    codec: StringFormat,
    scope: CoroutineScope,
    warning: (count: Int) -> String
) = MailgunEmailAgent(toMailgunAgentOptions(http, codec, scope, warning))

fun AgentConfiguration<SendEmailParams>.toMailgunAgentOptions(
    http: HttpClient,
    codec: StringFormat,
    scope: CoroutineScope,
    warning: (count: Int) -> String
) = MailgunOptions(
    key = params["key"] ?: throw MailgunEmailAgentException("Mailgun api key mut be passed in the key option of mailgun Configuration"),
    warning = toCreditWarning(agent = "mailgun", warning),
    http = http,
    outbox = outbox,
    codec = codec,
    scope = scope
)
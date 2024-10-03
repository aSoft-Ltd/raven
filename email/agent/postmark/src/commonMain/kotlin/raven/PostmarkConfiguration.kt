package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.StringFormat

fun AgentConfiguration<SendEmailParams>.toPostmarkAgent(
    http: HttpClient,
    codec: StringFormat,
    scope: CoroutineScope,
    warning: (count: Int) -> String
) = PostmarkEmailAgent(toPostmarkAgentOptions(http, codec, scope, warning))

fun AgentConfiguration<SendEmailParams>.toPostmarkAgentOptions(
    http: HttpClient,
    codec: StringFormat,
    scope: CoroutineScope,
    warning: (count: Int) -> String
) = PostmarkOptions(
    token = params["token"] ?: throw PostmarkEmailAgentException("Postmark server api token mut be passed in the key option of Postmark Configuration"),
    warning = toCreditWarning(agent = "postmark", warning),
    http = http,
    outbox = outbox,
    codec = codec,
    scope = scope
)
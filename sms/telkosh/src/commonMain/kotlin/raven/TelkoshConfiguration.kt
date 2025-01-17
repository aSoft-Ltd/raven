package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.StringFormat

fun AgentConfiguration<SendSmsParams>.toTelkoshSmsAgent(
    http: HttpClient,
    codec: StringFormat,
    scope: CoroutineScope,
    warning: (count: Int) -> String
) = TelkoshSmsAgent(toTelkoshAgentOptions(http, codec, scope, warning))

fun AgentConfiguration<SendSmsParams>.toTelkoshAgentOptions(
    http: HttpClient,
    codec: StringFormat,
    scope: CoroutineScope,
    warning: (count: Int) -> String
) = TelkoshOptions(
    user = params["key"] ?: throw TelkoshSmsAgentException("`key` is missing: KilaKona api key must be set in the key option of kilakona agent configuration"),
    password = params["secret"] ?: throw TelkoshSmsAgentException("`secret` is missing: KilaKona secret must be set in the secret option of kilakona agent configuration"),
    warning = toCreditWarning(agent = "kilakona", warning),
    http = http,
    outbox = outbox,
    codec = codec,
    scope = scope
)
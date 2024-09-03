package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.StringFormat

fun AgentConfiguration<SendSmsParams>.toKilaKonaSmsAgent(
    http: HttpClient,
    codec: StringFormat,
    scope: CoroutineScope,
    warning: (count: Int) -> String
) = KilaKonaSmsAgent(toKilaKonaAgentOptions(http, codec, scope, warning))

fun AgentConfiguration<SendSmsParams>.toKilaKonaAgentOptions(
    http: HttpClient,
    codec: StringFormat,
    scope: CoroutineScope,
    warning: (count: Int) -> String
) = KilaKonaOptions(
    key = params["key"] ?: throw KilaKonaSmsAgentException("`key` is missing: KilaKona api key must be set in the key option of kilakona agent configuration"),
    secret = params["secret"] ?: throw KilaKonaSmsAgentException("`secret` is missing: KilaKona secret must be set in the secret option of kilakona agent configuration"),
    warning = toCreditWarning(agent = "kilakona", warning),
    http = http,
    outbox = outbox,
    codec = codec,
    scope = scope
)
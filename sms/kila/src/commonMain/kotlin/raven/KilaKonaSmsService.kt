package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.StringFormat
import raven.KilaKonaOptions.Companion.DEFAULT_CODEC
import raven.KilaKonaOptions.Companion.DEFAULT_HTTP
import raven.KilaKonaOptions.Companion.DEFAULT_SCOPE
import raven.internal.KilaKonaSmsAgentImpl

fun KilaKonaSmsAgent(
    options: KilaKonaOptions
): SmsAgent = KilaKonaSmsAgentImpl(options)

fun KilaKonaSmsAgent(
    key: String,
    secret: String,
    warning: CreditWarning,
    outbox: SmsOutbox? = null,
    http: HttpClient = DEFAULT_HTTP,
    codec: StringFormat = DEFAULT_CODEC,
    scope: CoroutineScope = DEFAULT_SCOPE
): SmsAgent = KilaKonaSmsAgentImpl(KilaKonaOptions(key, secret, warning, outbox, http, codec, scope))
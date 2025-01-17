package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.StringFormat
import raven.TelkoshOptions.Companion.DEFAULT_CODEC
import raven.TelkoshOptions.Companion.DEFAULT_HTTP
import raven.TelkoshOptions.Companion.DEFAULT_SCOPE
import raven.internal.TelkoshSmsAgentImpl

fun TelkoshSmsAgent(
    options: TelkoshOptions
): SmsAgent = TelkoshSmsAgentImpl(options)

fun TelkoshSmsAgent(
    key: String,
    secret: String,
    warning: CreditWarning,
    outbox: Outbox<SendSmsParams>? = null,
    http: HttpClient = DEFAULT_HTTP,
    codec: StringFormat = DEFAULT_CODEC,
    scope: CoroutineScope = DEFAULT_SCOPE
): SmsAgent = TelkoshSmsAgentImpl(TelkoshOptions(key, secret, warning, outbox, http, codec, scope))
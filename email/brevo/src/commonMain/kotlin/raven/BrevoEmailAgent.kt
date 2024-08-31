@file:Suppress("NOTHING_TO_INLINE")

package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.StringFormat
import raven.internal.BrevoEmailAgentImpl

inline fun BrevoEmailAgent(
    options: BrevoOptions
): EmailAgent = BrevoEmailAgentImpl(options)

inline fun BrevoEmailAgent(
    key: String,
    warning: CreditWarning,
    outbox: Outbox<SendEmailParams>? = null,
    http: HttpClient = BrevoOptions.DEFAULT_HTTP,
    codec: StringFormat = BrevoOptions.DEFAULT_CODEC,
    scope: CoroutineScope = BrevoOptions.DEFAULT_SCOPE
): EmailAgent = BrevoEmailAgentImpl(BrevoOptions(key, warning, outbox, http, codec, scope))
@file:Suppress("NOTHING_TO_INLINE")

package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.StringFormat
import raven.internal.MailgunEmailAgentImpl

inline fun MailgunEmailAgent(
    options: MailgunOptions
): EmailAgent = MailgunEmailAgentImpl(options)

inline fun MailgunEmailAgent(
    key: String,
    warning: CreditWarning,
    outbox: Outbox<SendEmailParams>? = null,
    http: HttpClient = MailgunOptions.DEFAULT_HTTP,
    codec: StringFormat = MailgunOptions.DEFAULT_CODEC,
    scope: CoroutineScope = MailgunOptions.DEFAULT_SCOPE
): EmailAgent = MailgunEmailAgentImpl(MailgunOptions(key, warning, outbox, http, codec, scope))
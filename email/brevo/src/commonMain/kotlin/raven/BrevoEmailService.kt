@file:Suppress("NOTHING_TO_INLINE")

package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.StringFormat
import raven.internal.BrevoEmailServiceImpl

inline fun BrevoEmailService(
    options: BrevoOptions
): EmailService = BrevoEmailServiceImpl(options)

inline fun BrevoEmailService(
    apiKey: String,
    warning: CreditWarning,
    http: HttpClient = BrevoOptions.DEFAULT_HTTP,
    codec: StringFormat = BrevoOptions.DEFAULT_CODEC,
    scope: CoroutineScope = BrevoOptions.DEFAULT_SCOPE
): EmailService = BrevoEmailServiceImpl(BrevoOptions(apiKey, warning, http, codec, scope))
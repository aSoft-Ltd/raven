@file:Suppress("NOTHING_TO_INLINE")

package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.StringFormat
import raven.internal.PostmarkEmailAgentImpl

inline fun PostmarkEmailAgent(
    options: PostmarkOptions
): EmailAgent = PostmarkEmailAgentImpl(options)

inline fun PostmarkEmailAgent(
    token: String,
    warning: CreditWarning,
    outbox: Outbox<SendEmailParams>? = null,
    http: HttpClient = PostmarkOptions.DEFAULT_HTTP,
    codec: StringFormat = PostmarkOptions.DEFAULT_CODEC,
    scope: CoroutineScope = PostmarkOptions.DEFAULT_SCOPE
): EmailAgent = PostmarkEmailAgentImpl(PostmarkOptions(token, warning, outbox, http, codec, scope))
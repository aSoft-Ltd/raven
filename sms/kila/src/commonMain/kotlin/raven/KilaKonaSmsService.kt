package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.StringFormat
import raven.KilaKonaOptions.Companion.DEFAULT_CODEC
import raven.KilaKonaOptions.Companion.DEFAULT_HTTP
import raven.KilaKonaOptions.Companion.DEFAULT_SCOPE
import raven.internal.KilaKonaSmsServiceImpl

fun KilaKonaSmsService(
    options: KilaKonaOptions
): SmsService = KilaKonaSmsServiceImpl(options)

fun KilaKonaSmsService(
    key: String,
    secret: String,
    warning: CreditWarning,
    http: HttpClient = DEFAULT_HTTP,
    codec: StringFormat = DEFAULT_CODEC,
    scope: CoroutineScope = DEFAULT_SCOPE
): SmsService = KilaKonaSmsServiceImpl(KilaKonaOptions(key, secret, warning, http, codec, scope))
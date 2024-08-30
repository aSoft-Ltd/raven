package raven.internal

import raven.KilaKonaOptions
import raven.SmsService

@PublishedApi
internal class KilaKonaSmsServiceImpl(
    options: KilaKonaOptions
) : SmsService {
    override val account by lazy { KilaKonaAccountServiceImpl(options) }
    override val sender by lazy { KilaKonaSmsSenderImpl(options, this) }
}
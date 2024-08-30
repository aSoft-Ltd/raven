package raven.internal

import raven.BeemOptions
import raven.SmsService

@PublishedApi
internal class BeemSmsServiceImpl(
    options: BeemOptions
) : SmsService {
    override val account by lazy { BeemAccountServiceImpl(options) }
    override val sender by lazy { BeemSmsSenderImpl(options, this) }
}
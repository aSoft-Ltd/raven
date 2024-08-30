package raven.internal

import raven.BrevoOptions
import raven.EmailService

@PublishedApi
internal class BrevoEmailServiceImpl(options: BrevoOptions) : EmailService {
    override val account by lazy { BrevoAccountService(options) }
    override val sender by lazy { BrevoEmailSender(options,this) }
}
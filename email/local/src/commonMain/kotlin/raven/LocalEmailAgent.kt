package raven

import koncurrent.Later
import koncurrent.toLater

class LocalEmailAgent(private val outbox: EmailOutbox? = null) : EmailAgent {
    override fun credit() = Int.MAX_VALUE.toLater()
    override fun supports(body: EmailContentType): Boolean = true
    override fun send(params: SendEmailParams) = outbox?.store(params) ?: params.toLater()
    override fun canSend(count: Int) = Later(true)
}
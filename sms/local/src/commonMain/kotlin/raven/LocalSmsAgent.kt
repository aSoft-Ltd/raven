package raven

import koncurrent.Later
import koncurrent.toLater

class LocalSmsAgent(private val outbox: Outbox<SendSmsParams>? = null) : SmsAgent {
    override fun credit() = Int.MAX_VALUE.toLater()
    override fun send(params: SendSmsParams) = outbox?.store(params) ?: params.toLater()
    override fun canSend(count: Int) = Later(true)
}
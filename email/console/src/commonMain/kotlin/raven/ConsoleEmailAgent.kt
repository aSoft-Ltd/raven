package raven

import koncurrent.Later
import koncurrent.later.finally
import koncurrent.toLater

class ConsoleEmailAgent(private val options: ConsoleEmailAgentOptions = ConsoleEmailAgentOptions()) : EmailAgent {
    override fun credit() = Int.MAX_VALUE.toLater()
    override fun canSend(count: Int) = Later(true)
    override fun supports(body: EmailContentType) = body == EmailContentType.plain
    override fun send(params: SendEmailParams) = (options.outbox?.store(params) ?: params.toLater()).finally {
        println(options.formatter.format(params))
    }

    override fun toString(): String = "ConsoleEmailAgent"
}
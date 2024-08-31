package raven

import koncurrent.Later
import koncurrent.later.finally
import koncurrent.toLater

class ConsoleSmsAgent(
    private val options: ConsoleSmsAgentOptions = ConsoleSmsAgentOptions()
) : SmsAgent {

    override fun send(params: SendSmsParams) = (options.outbox?.store(params) ?: params.toLater()).finally {
        println(options.formatter.format(params))
    }

    override fun credit(): Later<Int> = Int.MAX_VALUE.toLater()

    override fun canSend(count: Int): Later<Boolean> = Later(true)
}
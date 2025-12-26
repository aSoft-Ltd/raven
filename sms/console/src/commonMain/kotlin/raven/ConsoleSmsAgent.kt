package raven

class ConsoleSmsAgent(
    private val options: ConsoleSmsAgentOptions = ConsoleSmsAgentOptions()
) : SmsAgent {

    override suspend fun send(params: SendSmsParams): SendSmsParams {
        val p = options.outbox?.store(params) ?: params
        println(options.formatter.format(params))
        return p
    }

    override suspend fun credit(): Int = Int.MAX_VALUE

    override suspend fun canSend(count: Int): Boolean = true
}
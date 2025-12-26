package raven

class ConsoleEmailAgent(private val options: ConsoleEmailAgentOptions = ConsoleEmailAgentOptions()) : EmailAgent {
    override suspend fun credit() = Int.MAX_VALUE
    override suspend fun canSend(count: Int) = true
    override fun supports(body: EmailContentType) = body == EmailContentType.plain
    override suspend fun send(params: SendEmailParams): SendEmailParams {
        val p = options.outbox?.store(params) ?: params
        println(options.formatter.format(params))
        return p
    }

    override fun toString(): String = "ConsoleEmailAgent"
}
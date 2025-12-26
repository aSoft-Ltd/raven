package raven

class SmsService(
    private val agents: Collection<Sender<SendSmsParams>>
) : Service<SendSmsParams> {
    override suspend fun send(params: SendSmsParams): SendSmsParams {
        val main = agents.firstOrNull() ?: throw IllegalStateException("Main SmsAgent not found in sms service")
        val others = agents - main
        for (agent in others) agent.send(params)
        return main.send(params)
    }
}
package raven

class EmailService(
    private val agents: Collection<Sender<SendEmailParams>>
) : Service<SendEmailParams> {
    override suspend fun send(params: SendEmailParams): SendEmailParams {
        val main = agents.firstOrNull() ?: throw IllegalStateException("Main EmailAgent not found in email service")
        val others = agents - main
        for (agent in others) agent.send(params)
        return main.send(params)
    }
}
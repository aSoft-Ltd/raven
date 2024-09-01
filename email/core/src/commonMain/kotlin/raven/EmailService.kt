package raven

import koncurrent.FailedLater
import koncurrent.Later

class EmailService(
    private val agents: List<Sender<SendEmailParams>>
) : Service<SendEmailParams> {
    override fun send(params: SendEmailParams): Later<SendEmailParams> {
        val main = agents.firstOrNull() ?: return FailedLater("Main EmailAgent not found in email service")
        val others = agents - main
        for (agent in others) agent.send(params)
        return main.send(params)
    }
}
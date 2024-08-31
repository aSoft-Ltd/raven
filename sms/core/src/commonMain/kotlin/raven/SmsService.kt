package raven

import koncurrent.FailedLater
import koncurrent.Later

class SmsService(
    private val agents: List<SmsAgent>
) : Service<SendSmsParams> {
    override fun send(params: SendSmsParams): Later<SendSmsParams> {
        val main = agents.firstOrNull() ?: return FailedLater("Main SmsAgent not found in sms service")
        val others = agents - main
        for (agent in others) agent.send(params)
        return main.send(params)
    }
}
package raven

import raven.Outbox
import raven.SendSmsParams
import raven.SmsService

class TestAppService(
    private val options: TestAppOptions
) {
    val outbox by lazy { OutboxService() }
    val agents by lazy { MessagingAgents() }

    inner class OutboxService {
        val email by lazy { options.outbox.email }
        val sms by lazy { options.outbox.sms }
    }

    inner class MessagingAgents {
        val email by lazy { EmailService(options.agents.email) }
        val sms by lazy { SmsService(options.agents.sms) }
    }
}
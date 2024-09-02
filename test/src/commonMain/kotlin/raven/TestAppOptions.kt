package raven

class TestAppOptions(
    private val config: TestAppConfiguration
) {
    val outbox by lazy { OutboxOptions() }

    inner class OutboxOptions {
        val sms by lazy { config.sms.outboxes.firstOrNull() }
        val email by lazy { config.email.outboxes.firstOrNull() }
    }

    val agents by lazy { Agents() }

    inner class Agents {
        val sms by lazy { config.sms.agents }
        val email by lazy { config.email.agents }
    }
}
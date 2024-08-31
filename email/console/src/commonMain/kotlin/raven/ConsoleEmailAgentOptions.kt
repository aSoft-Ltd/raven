package raven

class ConsoleEmailAgentOptions(
    val outbox: Outbox<SendEmailParams>? = null,
    val formatter: PrettyConsoleEmailFormatter = PrettyConsoleEmailFormatter()
)
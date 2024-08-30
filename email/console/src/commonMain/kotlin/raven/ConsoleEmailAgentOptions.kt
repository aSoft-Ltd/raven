package raven

class ConsoleEmailAgentOptions(
    val outbox: EmailOutbox? = null,
    val formatter: PrettyConsoleEmailFormatter = PrettyConsoleEmailFormatter()
)
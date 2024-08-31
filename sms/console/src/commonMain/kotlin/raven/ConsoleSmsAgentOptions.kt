package raven

class ConsoleSmsAgentOptions(
    val outbox: Outbox<SendSmsParams>? = null,
    val formatter: PrettyConsoleSmsFormatter = PrettyConsoleSmsFormatter()
)
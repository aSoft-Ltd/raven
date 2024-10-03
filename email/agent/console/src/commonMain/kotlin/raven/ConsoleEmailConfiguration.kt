package raven

fun AgentConfiguration<SendEmailParams>.toConsoleEmailAgent(): Sender<SendEmailParams> {
    return ConsoleEmailAgent(toConsoleEmailAgentOptions())
}

fun AgentConfiguration<SendEmailParams>.toConsoleEmailAgentOptions(): ConsoleEmailAgentOptions {
    val formatter = PrettyConsoleEmailFormatter(
        PrettyConsoleEmailFormatterOptions(
            separator = params["separator"] ?: "=",
            width = params["width"]?.toIntOrNull() ?: 95,
            margin = params["margin"]?.toIntOrNull() ?: 25,
            border = params["border"] ?: "|",
            padding = params["padding"]?.toIntOrNull() ?: 1
        )
    )
    return ConsoleEmailAgentOptions(outbox = outbox, formatter = formatter)
}
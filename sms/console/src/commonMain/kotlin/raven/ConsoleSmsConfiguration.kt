package raven

fun AgentConfiguration<SendSmsParams>.toConsoleSmsAgent(): Sender<SendSmsParams> {
    return ConsoleSmsAgent(toConsoleSmsAgentOptions())
}

fun AgentConfiguration<SendSmsParams>.toConsoleSmsAgentOptions(): ConsoleSmsAgentOptions {
    val formatter = PrettyConsoleSmsFormatter(
        PrettyConsoleSmsFormatterOptions(
            separator = params["separator"] ?: "=",
            width = params["width"]?.toIntOrNull() ?: 95,
            margin = params["margin"]?.toIntOrNull() ?: 25,
            border = params["border"] ?: "|",
            padding = params["padding"]?.toIntOrNull() ?: 1
        )
    )
    return ConsoleSmsAgentOptions(outbox = outbox, formatter = formatter)
}
package raven

fun <P> AgentConfiguration<P>.toCreditWarning(
    agent: String,
    message: (count: Int) -> String
): CreditWarning {
    val to = params["warning_recipients"] ?: throw IllegalArgumentException("`warning_recipients` is missing: One or more recipients must be set when using the $agent agent")
    val recipients = to.split(",")
    if (recipients.isEmpty()) throw IllegalArgumentException("Recipients must have at least one warning recipient")
    val limit = params["warning_limit"]?.toIntOrNull() ?: throw IllegalArgumentException("`warning_limit` is missing: A warning limit must be set using the $agent agent")
    return CreditWarning(recipients, limit, message)
}
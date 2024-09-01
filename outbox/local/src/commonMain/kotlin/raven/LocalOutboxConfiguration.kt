package raven

fun <P> Map<String, String>.toOutbox(isSent: P.(receiver: String) -> Boolean): Outbox<P> {
    val capacity = this["capacity"]?.toIntOrNull() ?: LocalOutbox.DEFAULT_CAPACITY
    return LocalOutbox(capacity = capacity, isSent = isSent)
}
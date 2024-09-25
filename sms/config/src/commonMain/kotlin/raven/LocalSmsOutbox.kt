package raven

internal val isSmsSent: SendSmsParams.(String) -> Boolean = { receiver ->
    val recipients = to.map { it.replace("+", "") }
    val recipient = receiver.replaceFirst("+", "")
    recipient in recipients
}

fun LocalSmsOutbox(capacity: Int = LocalOutbox.DEFAULT_CAPACITY) = LocalOutbox(capacity, isSmsSent)
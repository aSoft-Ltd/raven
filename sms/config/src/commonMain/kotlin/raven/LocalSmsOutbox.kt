package raven

internal val isSent: SendSmsParams.(String) -> Boolean = { receiver -> receiver in to }

fun LocalSmsOutbox(capacity: Int = LocalOutbox.DEFAULT_CAPACITY) = LocalOutbox(capacity, isSent)
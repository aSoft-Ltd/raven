package raven

internal val isSmsSent: SendSmsParams.(String) -> Boolean = { receiver -> receiver in to }

fun LocalSmsOutbox(capacity: Int = LocalOutbox.DEFAULT_CAPACITY) = LocalOutbox(capacity, isSmsSent)
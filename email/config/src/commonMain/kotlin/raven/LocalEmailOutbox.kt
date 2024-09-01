package raven


internal val isSent: SendEmailParams.(String) -> Boolean = { receiver -> receiver in to.map { it.email } }

fun LocalEmailOutbox(capacity: Int = LocalOutbox.DEFAULT_CAPACITY) = LocalOutbox(capacity, isSent)
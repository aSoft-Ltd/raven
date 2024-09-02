package raven


internal val isEmailSent: SendEmailParams.(String) -> Boolean = { receiver -> receiver in to.map { it.email } }

fun LocalEmailOutbox(capacity: Int = LocalOutbox.DEFAULT_CAPACITY) = LocalOutbox(capacity, isEmailSent)
package raven

inline fun buildEmailService(factory: MutableList<EmailAgent>.() -> Unit) = EmailService(buildList(factory))
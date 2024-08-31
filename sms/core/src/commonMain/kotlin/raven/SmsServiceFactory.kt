package raven

inline fun buildSmsService(factory: MutableList<SmsAgent>.() -> Unit) = SmsService(buildList(factory))
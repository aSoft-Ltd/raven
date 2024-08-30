package raven.internal

internal class KilaKonaEndpoint(private val root: String) {
    private val message by lazy { "$root/api/v1/vendor/message" }
    val sms by lazy { "$message/send" }
    val balance by lazy { "$message/balance" }
}
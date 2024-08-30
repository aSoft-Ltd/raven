package raven.internal

internal class BrevoEndpoint(
    private val root: String
) {
    val sms by lazy { "$root/smtp/email" }

    val account by lazy { "$root/account" }
}
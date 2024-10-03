package raven.internal

internal class BrevoEndpoint(
    private val root: String
) {
    val sms by lazy { "$root/v3/smtp/email" }

    val account by lazy { "$root/v3/account" }
}
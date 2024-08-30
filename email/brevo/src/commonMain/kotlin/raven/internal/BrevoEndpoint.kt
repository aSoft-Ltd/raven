package raven.internal

internal class BrevoEndpoint(
    private val root: String = "https://api.brevo.com/v3"
) {
    val sms by lazy { "$root/smtp/email" }

    val account by lazy { "$root/account" }
}
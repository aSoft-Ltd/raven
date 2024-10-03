package raven.internal

internal class MailgunEndpoint(
    private val root: String
) {
    fun email(domain: String) = "$root/v3/$domain/messages"
    fun account() = "$root/v5/accounts/limit/custom/monthly"
}
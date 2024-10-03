package raven.internal

internal class PostmarkEndpoint(
    private val root: String
) {
    fun email() = "$root/email"
    fun account() = "$root/v5/accounts/limit/custom/monthly"
}
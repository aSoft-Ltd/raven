package raven

interface EmailAttachment<out T> {
    val content: T
    val type: String
    val name: String
}
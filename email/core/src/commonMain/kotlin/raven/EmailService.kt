package raven

interface EmailService {
    val account: AccountService
    val sender: EmailSender
}
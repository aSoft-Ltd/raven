package raven

interface EmailAgent : Sender<SendEmailParams> {
    fun supports(body: EmailContentType): Boolean
}
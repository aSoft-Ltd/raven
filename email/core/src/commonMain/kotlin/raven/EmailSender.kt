package raven

import koncurrent.Later

interface EmailSender : Sender {
    fun supports(body: EmailContentType): Boolean

    fun send(params: SendEmailParams): Later<SendEmailParams>
}
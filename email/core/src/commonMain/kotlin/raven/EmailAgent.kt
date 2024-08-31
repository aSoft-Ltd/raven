package raven

import koncurrent.Later

interface EmailAgent : Sender {
    fun supports(body: EmailContentType): Boolean

    fun send(params: SendEmailParams): Later<SendEmailParams>
}
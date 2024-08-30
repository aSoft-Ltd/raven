package raven

import koncurrent.Later

interface EmailAgent : Sender {
    fun credit(): Later<Int>

    fun supports(body: EmailContentType): Boolean

    fun send(params: SendEmailParams): Later<SendEmailParams>
}
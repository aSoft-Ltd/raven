package raven

import koncurrent.Later

interface EmailServiceProvider : Sender {
    fun supports(body: EmailContentType): Boolean

    fun send(params: SendEmailParams): Later<SendEmailParams>

    fun credit(): Later<Int>
}
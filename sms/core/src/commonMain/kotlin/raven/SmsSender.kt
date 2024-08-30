package raven

import koncurrent.Later

interface SmsSender : Sender {
    fun send(params: SendSmsParams): Later<SendSmsParams>
}
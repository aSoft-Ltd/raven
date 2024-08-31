package raven

import koncurrent.Later

interface SmsAgent : Sender {
    fun send(params: SendSmsParams): Later<SendSmsParams>
}
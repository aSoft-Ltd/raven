package raven

import koncurrent.Later

interface EmailOutbox {
    fun store(params: SendEmailParams): Later<SendEmailParams>
    
    fun sent(to: String): Later<List<SendEmailParams>>

    fun delete(receiver: String): Later<List<SendEmailParams>>
}
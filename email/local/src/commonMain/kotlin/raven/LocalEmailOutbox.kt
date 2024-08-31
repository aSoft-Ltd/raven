package raven

import koncurrent.Later
import koncurrent.later.then
import koncurrent.toLater

class LocalEmailOutbox(private val capacity: Int = 10) : Outbox<SendEmailParams> {

    private val messages = mutableListOf<SendEmailParams>()

    override fun store(params: SendEmailParams): Later<SendEmailParams> {
        if (messages.size > capacity) {
            messages.removeFirst()
        }
        messages.add(params)
        return params.toLater()
    }

    override fun sent(to: String) = messages.filter { message ->
        to in message.to.map { it.email }
    }.toLater()

    override fun delete(receiver: String) = sent(to = receiver).then {
        messages -= it
        it
    }
}
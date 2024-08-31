package raven

import koncurrent.Later
import koncurrent.later.then
import koncurrent.toLater

class LocalSmsOutbox(private val capacity: Int = 10) : Outbox<SendSmsParams> {

    private val messages = mutableListOf<SendSmsParams>()

    override fun store(params: SendSmsParams): Later<SendSmsParams> {
        if (messages.size > capacity) {
            messages.removeFirst()
        }
        messages.add(params)
        return params.toLater()
    }

    override fun sent(to: String) = messages.filter { to in it.to }.toLater()

    override fun delete(receiver: String) = sent(to = receiver).then {
        messages -= it
        it
    }
}
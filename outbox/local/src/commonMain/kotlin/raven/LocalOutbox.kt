package raven

import koncurrent.Later
import koncurrent.later.then
import koncurrent.toLater

class LocalOutbox<P>(
    private val capacity: Int = DEFAULT_CAPACITY,
    private val isSent: P.(to: String) -> Boolean
) : Outbox<P> {

    companion object {
        val DEFAULT_CAPACITY = 10
    }

    private val messages = mutableListOf<P>()

    override fun store(params: P): Later<P> {
        if (messages.size > capacity) {
            messages.removeFirst()
        }
        messages.add(params)
        return params.toLater()
    }

    override fun sent(to: String) = messages.filter { it.isSent(to) }.toLater()

    override fun delete(receiver: String) = sent(to = receiver).then {
        messages -= it
        it
    }

    override fun toString() = "LocalOutbox(capacity = $capacity)"
}
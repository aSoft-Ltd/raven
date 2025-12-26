package raven

class LocalOutbox<P>(
    private val capacity: Int = DEFAULT_CAPACITY,
    private val isSent: P.(to: String) -> Boolean
) : Outbox<P> {

    companion object {
        val DEFAULT_CAPACITY = 10
    }

    private val messages = mutableListOf<P>()

    override suspend fun store(params: P): P {
        if (messages.size > capacity) {
            messages.removeFirst()
        }
        messages.add(params)
        return params
    }

    override suspend fun sent(to: String) = messages.filter { it.isSent(to) }

    override suspend fun delete(receiver: String): List<P> {
        val them = sent(to = receiver)
        messages -= them
        return them
    }

    override fun toString() = "LocalOutbox(capacity = $capacity)"
}
package raven

interface Outbox<P> {
    suspend fun store(params: P): P

    suspend fun sent(to: String): List<P>

    suspend fun delete(receiver: String): List<P>
}
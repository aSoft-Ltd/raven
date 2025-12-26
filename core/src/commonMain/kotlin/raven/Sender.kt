package raven

interface Sender<P> {
    suspend fun credit(): Int

    suspend fun canSend(count: Int): Boolean

    suspend fun send(params: P): P
}
package raven

interface Service<P> {
    suspend fun send(params: P): P
}
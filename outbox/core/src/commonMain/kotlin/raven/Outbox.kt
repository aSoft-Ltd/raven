package raven

import koncurrent.Later

interface Outbox<P> {
    fun store(params: P): Later<P>

    fun sent(to: String): Later<List<P>>

    fun delete(receiver: String): Later<List<P>>
}
package raven

import koncurrent.Later

interface Sender<P> {
    fun credit(): Later<Int>

    fun canSend(count: Int): Later<Boolean>

    fun send(params: P): Later<P>
}
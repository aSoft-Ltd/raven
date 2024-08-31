package raven

import koncurrent.Later

interface Service<P> {
    fun send(params: P): Later<P>
}
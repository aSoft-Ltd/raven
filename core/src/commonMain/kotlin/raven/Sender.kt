package raven

import koncurrent.Later

interface Sender {
    fun credit(): Later<Int>

    fun canSend(count: Int): Later<Boolean>
}
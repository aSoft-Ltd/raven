package raven

import koncurrent.Later

interface Sender {
    fun canSend(count: Int): Later<Boolean>
}
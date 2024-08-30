package raven

import koncurrent.Later

interface AccountService {
    fun credit(): Later<Int>
}
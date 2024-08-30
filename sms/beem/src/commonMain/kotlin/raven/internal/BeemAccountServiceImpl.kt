package raven.internal

import koncurrent.Later
import koncurrent.TODOLater
import raven.AccountService
import raven.BeemOptions

internal class BeemAccountServiceImpl(private val options: BeemOptions) : AccountService {
    override fun credit(): Later<Int> {
        return TODOLater()
    }
}
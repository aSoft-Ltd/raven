package raven.internal

import koncurrent.Later
import koncurrent.TODOLater
import raven.AccountService
import raven.KilaKonaOptions

internal class KilaKonaAccountServiceImpl(private val options: KilaKonaOptions) : AccountService {
    override fun credit(): Later<Int> {
        return TODOLater()
    }
}
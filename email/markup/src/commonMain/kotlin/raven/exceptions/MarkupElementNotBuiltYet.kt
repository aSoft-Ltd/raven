package raven.exceptions

class MarkupElementNotBuiltYet(name: String) : IllegalArgumentException("Element $name has not been built yet")

fun notBuiltYet(name: String): Nothing = throw MarkupElementNotBuiltYet(name)
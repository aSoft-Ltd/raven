import java.io.File

pluginManagement {
    includeBuild("../build-logic")
}

plugins {
    id("multimodule")
}

fun includeSubs(base: String, path: String = base, vararg subs: String) {
    subs.forEach {
        include(":$base-$it")
        project(":$base-$it").projectDir = File("$path/$it")
    }
}

listOf(
    "kollections", "koncurrent", "kommander",
).forEach { includeBuild("../$it") }

rootProject.name = "raven"

includeSubs("raven", ".", "core", "config", "test")
includeSubs("raven-outbox", "outbox", "core", "server", "client", "local")
includeSubs("raven-email", "email", "core", "test", "brevo", "console", "markup", "config")
includeSubs("raven-sms", "sms", "core", "test", "console", "beem", "kila", "config")
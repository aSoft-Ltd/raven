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

includeSubs("raven", ".", "core")
includeSubs("raven-outbox", "outbox", "core", "server", "client")
includeSubs("raven-email", "email", "core", "test", "brevo", "local", "console", "markup")
includeSubs("raven-sms", "sms", "core", "test", "local", "console", "beem", "kila")
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
    "kollections", "koncurrent", "kommander", "epsilon-client", "epsilon-api"
).forEach { includeBuild("../$it") }

rootProject.name = "raven"

includeSubs("raven", ".", "core", "config", "test")
includeSubs("raven-outbox", "outbox", "core", "server", "client", "local")
includeSubs("raven-email-agent", "email/agent", "core", "test", "brevo", "mailgun", "postmark", "console", "config")
includeSubs("raven-email", "email", "markup")
includeSubs("raven-email-resources", "email/resources", "core", "file")
includeSubs("raven-sms", "sms", "core", "test", "console", "beem", "kila", "config")
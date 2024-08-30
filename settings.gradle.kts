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
includeSubs("raven-email", "email", "core", "test", "brevo", "markup")
includeSubs("raven-sms", "sms", "core", "test", "beem", "kila")
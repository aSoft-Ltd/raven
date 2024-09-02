plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "An abstraction form sending email destinations and outbox"

kotlin {
    if (Targeting.JVM) jvm { library() }
    if (Targeting.OSX) osxTargets()
    if (Targeting.LINUX) linuxTargets()
//    if (Targeting.MINGW) mingwTargets()

    sourceSets {
        commonMain.dependencies {
            api(projects.ravenOutboxCore)
            api(libs.koncurrent.later.coroutines)
            api(ktor.server.core)
        }
    }
}
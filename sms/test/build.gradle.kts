plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "An abstraction form sending emails"

kotlin {
    if (Targeting.JVM) jvm { library() }
    if (Targeting.JS) js(IR) { library() }
    if (Targeting.OSX) osxTargets()
    if (Targeting.LINUX) linuxTargets()
// if (Targeting.MINGW) mingwTargets()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.ravenSmsCore)
                api(libs.kommander.coroutines)
                api(libs.koncurrent.later.coroutines)
            }
        }
    }
}
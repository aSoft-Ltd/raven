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
    if (Targeting.MINGW) mingwTargets()

    sourceSets {
        commonMain.dependencies {
            api(projects.ravenEmailCore)
            api(projects.ravenOutboxCore)
            implementation(libs.koncurrent.later.coroutines)
            implementation(kotlinx.serialization.json)
            implementation(ktor.client.core)
        }

        commonTest.dependencies {
            implementation(projects.ravenEmailTest)
        }

        jvmTest.dependencies {
            implementation(ktor.client.cio)
        }
    }
}
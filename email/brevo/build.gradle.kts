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

    sourceSets {
        commonMain.dependencies {
            api(projects.ravenEmailCore)
            api(projects.ravenOutboxCore)
            api(kotlinx.serialization.json)
            api(ktor.client.core)
            implementation(libs.koncurrent.later.coroutines)
            implementation(projects.ravenConfig)
        }

        commonTest.dependencies {
            implementation(projects.ravenEmailTest)
        }

        jvmTest.dependencies {
            implementation(ktor.client.cio)
        }
    }
}
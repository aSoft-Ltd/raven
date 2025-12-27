plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "An abstraction form sending emails"

kotlin {
    if (Targeting.JVM) jvm { library() }
    if (Targeting.WASM) wasmJs { library() }
    if (Targeting.JS) js(IR) { library() }
    if (Targeting.OSX) osxTargets()
    if (Targeting.LINUX) linuxTargets()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.exports)
                api(kotlinx.serialization.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kommander.coroutines)
            }
        }
    }
}
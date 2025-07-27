plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "An abstraction form embedding resources into emails"

kotlin {
    if (Targeting.JVM) jvm { library() }
    if (Targeting.WASM) wasmJs { library() }
    if (Targeting.JS) js(IR) { library() }
    if (Targeting.OSX) osxTargets()
    if (Targeting.LINUX) linuxTargets()

    sourceSets {
        commonMain.dependencies {
            api(libs.koncurrent.later.core)
        }
    }
}
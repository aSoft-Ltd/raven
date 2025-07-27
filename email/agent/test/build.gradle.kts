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
    // if (Targeting.MINGW) mingwTargets()

    sourceSets {
        commonMain.dependencies {
            api(projects.ravenEmailAgentCore)
            api(projects.ravenEmailMarkup)
            api(libs.kommander.coroutines)
            api(libs.koncurrent.later.coroutines)
        }

        if (Targeting.JVM) jvmMain.dependencies {
            api(kotlin("test-junit5"))
        }

        wasmJsTest.dependencies {
            implementation(npm("webpack", "*"))
            implementation(npm("sourcemap", "*"))
        }
    }
}
plugins {
    kotlin("multiplatform")
    id("tz.co.asoft.library")
}

description = "A headless library for email components"

kotlin {
    if (Targeting.JVM) jvm { library() }
    if (Targeting.JS) js(IR) { library() }
//    if (Targeting.WASM) wasm { library() }
    if (Targeting.OSX) osxTargets()
    if (Targeting.NDK) ndkTargets()
    if (Targeting.LINUX) linuxTargets()
// if (Targeting.MINGW) mingwTargets()

    sourceSets {
        commonMain.dependencies {
        }

        commonTest.dependencies {
            implementation(libs.kommander.coroutines)
        }
    }
}
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
            api(projects.ravenCore)
        }

        commonTest.dependencies {
            implementation(projects.ravenOutboxLocal)

            implementation(projects.ravenSmsKila)
            implementation(projects.ravenEmailConsole)
            implementation(projects.ravenEmailConfig)

            implementation(projects.ravenEmailBrevo)
            implementation(projects.ravenSmsConsole)
            implementation(projects.ravenSmsConfig)

            implementation(kotlinx.serialization.toml)?.because("We need to test dynamic configuration")
            implementation(libs.kommander.coroutines)
            implementation(libs.koncurrent.later.coroutines)
        }

        jvmTest.dependencies {
            implementation(ktor.client.cio)
        }
    }
}
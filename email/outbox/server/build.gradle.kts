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
    if (Targeting.MINGW) mingwTargets()

    sourceSets {
        commonMain.dependencies {
            api(projects.ravenEmailOutboxCore)
            api(ktor.server.core)
        }

        commonTest.dependencies {
            implementation(libs.kommander.coroutines)
            implementation(ktor.server.test.host)
            implementation(projects.ravenEmailOutboxClient)
            implementation(projects.ravenEmailLocal)
            implementation(projects.ravenEmailConsole)
            implementation(kotlinx.serialization.json)
        }

        jvmMain.dependencies {
            implementation(ktor.client.cio)
        }
    }
}
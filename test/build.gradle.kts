plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "An abstraction form sending email destinations and outbox"

kotlin {
    if (Targeting.JVM) jvm { library() }
//    if (Targeting.OSX) osxTargets()
    if (Targeting.LINUX) linuxTargets()
//    if (Targeting.MINGW) mingwTargets()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.ravenOutboxServer)
            implementation(projects.ravenOutboxClient)
            implementation(projects.ravenOutboxLocal)
            implementation(libs.koncurrent.later.coroutines)

            implementation(projects.ravenEmailConfig)
            implementation(projects.ravenEmailConsole)

            implementation(projects.ravenSmsConfig)
            implementation(projects.ravenSmsConsole)

            implementation(kotlinx.serialization.json)
            implementation(kotlinx.serialization.toml)
            implementation(ktor.server.core)
        }

        commonTest.dependencies {
            implementation(libs.kommander.coroutines)
            implementation(ktor.server.test.host)
        }

        jvmMain.dependencies {
            implementation(ktor.client.cio)
        }
    }
}
plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "An abstraction form embedding file resources into emails"

kotlin {
    if (Targeting.JVM) jvm { library() }
    if (Targeting.WASM) wasmJs { library() }
    if (Targeting.JS) js(IR) { library() }
    if (Targeting.OSX) (iosTargets() + macOsTargets())
    if (Targeting.LINUX) linuxTargets()

    sourceSets {
        commonMain.dependencies {
            api(projects.ravenEmailResourcesCore)
            api(libs.epsilon.core)
        }
    }
}

//rootProject.the<NodeJsRootExtension>().apply {
//    version = npm.versions.node.version.get()
//    downloadBaseUrl = npm.versions.node.url.get()
//}
//
//rootProject.tasks.withType<KotlinNpmInstallTask>().configureEach {
//    args.add("--ignore-engines")
//}
//
//tasks.named("wasmJsTestTestDevelopmentExecutableCompileSync").configure {
//    mustRunAfter(tasks.named("jsBrowserTest"))
//    mustRunAfter(tasks.named("jsNodeTest"))
//}
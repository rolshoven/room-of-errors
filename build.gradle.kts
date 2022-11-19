plugins {
    kotlin("js") version "1.7.21"
}

group = "com.fynnian"
version = "1.0-SNAPSHOT"

object Versions {
    const val wrapperBom = "1.0.0-pre.442"
    const val serializationJson = "1.4.1"
    const val coroutines = "1.6.4"
}

repositories {
    mavenCentral()
}

fun kotlinw(target: String): String =
    "org.jetbrains.kotlin-wrappers:kotlin-$target"

dependencies {
    testImplementation(kotlin("test"))
    implementation(enforcedPlatform(kotlinw("wrappers-bom:${Versions.wrapperBom}")))
    implementation(kotlinw("react"))
    implementation(kotlinw("react-dom"))
    implementation(kotlinw("react-router-dom"))
    implementation(kotlinw("emotion"))
    implementation(kotlinw("mui"))
    implementation(kotlinw("mui-icons"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serializationJson}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")

}

kotlin {
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
                devServer?.open = false

            }
        }
    }
}

// workaround for hardcoded webpack version 4.9 that has a bug
// https://stackoverflow.com/a/72731728/12381648
rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
    versions.webpackCli.version = "4.10.0"
}
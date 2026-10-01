plugins {
    // Application
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // Compose
    alias(libs.plugins.kotlin.compose) apply false

    // Kotlin serialization
    alias(libs.plugins.kotlinx.serialization) apply false

    // KSP
    alias(libs.plugins.ksp) apply false

    // Hilt
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
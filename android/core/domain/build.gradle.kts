plugins {
    alias(libs.plugins.convention.kotlin.library)
}

dependencies {

    // Javax Inject
    api(libs.javax.inject)

    // Kotlin
    api(libs.kotlinx.coroutines.core)
}
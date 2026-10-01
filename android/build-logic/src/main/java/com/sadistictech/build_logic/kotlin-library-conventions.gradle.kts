import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("jvm")
}

java {
    sourceCompatibility = LanguageOptions.javaVersion
    targetCompatibility = LanguageOptions.javaVersion
}

kotlin {
    jvmToolchain(jdkVersion = LanguageOptions.jvmToolchain)

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xskip-prerelease-check",
            "-Xcontext-receivers",
            "-XXLanguage:+ExplicitBackingFields"
        )
    }
}
import com.android.build.gradle.BaseExtension

plugins {
    id("com.android.library")
    kotlin("android")
}

configure<BaseExtension> {
    val libs = libsWorkaround

    plugins {
        id(libs.plugKotlinxSerialization())
        id(libs.plugKotlinCompose())
        id(libs.plugHiltAndroid())
        id(libs.plugKsp())
    }

    dependencies {
        implementation(libs.libKotlinXSerialization())
        implementation(libs.libHiltAndroid())
        kspWorkaround(libs.libHiltCompiler())
        implementation(libs.libHiltExtensions())
        kspWorkaround(libs.libHiltExtensionsProcessor())

        // Compose
        implementation(libs.libComposeUI())
        implementation(libs.libComposeUIGraphics())
        implementation(libs.libComposeActivity())
        implementation(libs.libComposeBom())
        implementation(libs.libComposeNavigation())
        implementation(libs.libComposeMaterial())
        implementation(libs.libComposeMaterialIcons())
        implementation(libs.libComposeUiTooling())
        implementation(libs.libComposeUiToolingPreview())
        implementation(libs.libHiltNavigationCompose())
    }
}

android {
    namespace = AndroidConfig.applicationId + ".${project.name}"

    compileSdk = AndroidConfig.compileSdk

    defaultConfig {
        minSdk = AndroidConfig.minSdk
    }

    compileOptions {
        sourceCompatibility = LanguageOptions.javaVersion
        targetCompatibility = LanguageOptions.javaVersion
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
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
import com.android.build.gradle.BaseExtension

plugins {
    id("com.android.application")
    kotlin("android")
}

configure<BaseExtension> {
    val libs = libsWorkaround

    plugins {
        id(libs.plugHiltAndroid())
        id(libs.plugKotlinCompose())
        id(libs.plugKsp())
    }

    dependencies {
        implementation(libs.libHiltAndroid())
        implementation(libs.libComposeUI())
        implementation(libs.libComposeActivity())
        implementation(libs.libComposeBom())
        implementation(libs.libComposeNavigation())
        implementation(libs.libComposeMaterial())
        implementation(libs.libComposeMaterialIcons())
        implementation(libs.libComposeUiToolingPreview())
        implementation(libs.libComposeUiTooling())
        kspWorkaround(libs.libHiltCompiler())
    }
}

android {
    namespace = AndroidConfig.applicationId

    compileSdk = AndroidConfig.compileSdk

    defaultConfig {
        applicationId = AndroidConfig.applicationId
        minSdk = AndroidConfig.minSdk
        targetSdk = AndroidConfig.targetSdk
        versionCode = AndroidConfig.versionCode
        versionName = AndroidConfig.versionName
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = LanguageOptions.javaVersion
        targetCompatibility = LanguageOptions.javaVersion
    }

    buildFeatures {
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
//
//dependencies {
//
//    implementation(projectsWorkaround.core.presentation)
//}

plugins {
    alias(libs.plugins.convention.android.library)
}

android {
    buildTypes {
        release {
            buildConfigField("String", "BASE_URL", AndroidConfig.BASE_URL)
        }

        debug {
            buildConfigField("String", "BASE_URL", AndroidConfig.BASE_URL)
        }
    }
}

dependencies {

    // Modules
    api(projects.core.domain)

    // Retrofit
    api(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)

    // OkHttp
    implementation(platform(libs.okHttp.bom))
    implementation(libs.okHttp)
    implementation(libs.okHttp.loggingInterceptor)

    // Room
    api(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
}
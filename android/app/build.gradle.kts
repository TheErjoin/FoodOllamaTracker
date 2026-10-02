plugins {
    alias(libs.plugins.convention.android.app)
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.features.home)
    implementation(projects.features.profile)
    implementation(projects.features.settings)
    implementation(projects.features.onboarding)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

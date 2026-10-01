plugins {
    id("android-library-conventions")
}

dependencies {

    implementation(projectsWorkaround.core.data)
    implementation(projectsWorkaround.core.presentation)
}
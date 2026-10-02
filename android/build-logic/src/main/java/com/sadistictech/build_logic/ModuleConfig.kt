import org.gradle.api.JavaVersion

object LanguageOptions {

    const val jvmToolchain = 17
    val javaVersion: JavaVersion = JavaVersion.VERSION_17
}

object AndroidConfig {

    const val applicationId = "com.sadistictech.foodollamatracker"
    const val compileSdk: Int = 36
    const val minSdk: Int = 28
    const val targetSdk: Int = 36
    const val versionCode = 1
    const val versionName = "1.0"

    // Debug/test connection uses `adb reverse tcp:8000 tcp:8000`.
    // Unlike 10.0.2.2, this also works on a physical Android device.
    const val BASE_URL = "\"http://127.0.0.1:8000/\""
}

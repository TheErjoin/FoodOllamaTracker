import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

internal val Project.libsWorkaround: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun DependencyHandler.kspWorkaround(dependencyNotation: Any): Dependency? = add(
    "ksp", dependencyNotation
)

// Kotlin
internal fun VersionCatalog.plugKotlinxSerialization() = findPluginOrThrow("kotlinx-serialization")
internal fun VersionCatalog.libKotlinXSerialization() = findLibraryOrThrow("kotlinx-serialization")

// KSP
internal fun VersionCatalog.plugKsp() = findPluginOrThrow("ksp")

// Hilt
internal fun VersionCatalog.plugHiltAndroid() = findPluginOrThrow("hilt")
internal fun VersionCatalog.libHiltAndroid() = findLibraryOrThrow("hilt-android")
internal fun VersionCatalog.libHiltCompiler() = findLibraryOrThrow("hilt-compiler")
internal fun VersionCatalog.libHiltExtensions() = findLibraryOrThrow("hilt-extensions")
internal fun VersionCatalog.libHiltExtensionsProcessor() = findLibraryOrThrow("hilt-extensions-processor")

// Compose
internal fun VersionCatalog.libComposeMaterial() = findLibraryOrThrow("androidx-compose-material3")
internal fun VersionCatalog.libComposeUI() = findLibraryOrThrow("androidx-compose-ui")
internal fun VersionCatalog.libComposeUIGraphics() = findLibraryOrThrow("androidx-compose-ui-graphics")
internal fun VersionCatalog.libComposeUiToolingPreview() = findLibraryOrThrow("androidx-compose-ui-tooling-preview")
internal fun VersionCatalog.libComposeUiTooling() = findLibraryOrThrow("androidx-compose-ui-tooling")
internal fun VersionCatalog.libComposeNavigation() = findLibraryOrThrow("navigation-compose")
internal fun VersionCatalog.libComposeActivity() = findLibraryOrThrow("androidx-activity-compose")
internal fun VersionCatalog.libComposeBom() = findLibraryOrThrow("androidx-compose-bom")
internal fun VersionCatalog.libHiltNavigationCompose() = findLibraryOrThrow("androidx-hilt-navigation-compose")
internal fun VersionCatalog.plugKotlinCompose() = findPluginOrThrow("kotlin-compose")
internal fun VersionCatalog.libComposeMaterialIcons() = findLibraryOrThrow("compose-material-icons")

private fun VersionCatalog.findPluginOrThrow(name: String) = findPlugin(name).orElseThrow {
    NoSuchElementException("Plugin $name not found in version catalog")
}.get().pluginId

private fun VersionCatalog.findLibraryOrThrow(name: String) = findLibrary(name).orElseThrow {
    NoSuchElementException("Library $name not found in version catalog")
}.get()

import com.android.build.gradle.LibraryExtension
import com.ssafy.convention.extension.configureAndroidCompose
import com.ssafy.convention.extension.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply(libs.findPlugin("android.library").get().get().pluginId)
                apply(libs.findPlugin("jetbrains.kotlin.android").get().get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                defaultConfig.minSdk = 26
                defaultConfig.targetSdk = 34
                compileSdk = 34

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx.core.ktx").get())
                add("implementation", libs.findLibrary("junit").get())
                add("implementation", libs.findLibrary("androidx.junit").get())
                add("implementation", libs.findLibrary("androidx.espresso.core").get())
            }

            configureAndroidCompose(extensions.getByType<LibraryExtension>())
        }
    }

}
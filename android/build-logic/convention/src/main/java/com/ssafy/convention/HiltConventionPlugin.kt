// 패키지 명을 지정하면 안된다. 지정하면 Gradle에서 플러그인 구현체를 찾지 못한다.

import com.ssafy.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply(libs.findPlugin("ksp").get().get().pluginId)
                apply(libs.findPlugin("hilt").get().get().pluginId)
            }

            dependencies {
                add("ksp", libs.findLibrary("hilt.compiler").get())
                add("implementation", libs.findLibrary("hilt.android").get())
                add("implementation", libs.findLibrary("hilt.android.testing").get())
                add("implementation", libs.findLibrary("hilt.core").get())
                add("implementation", libs.findLibrary("hilt.navigation.compose").get())
            }
        }
    }
}
package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class RelayProviderConventionPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		AndroidLibraryConventionPlugin().apply(project)
		with(project) {
			with(pluginManager) {
				apply("com.android.library")
				apply("org.jetbrains.kotlin.plugin.serialization")
			}
			with(dependencies) {
				addComposeDependencies()
				add("api", "com.github.simulatan.matrixcontrol:matrixcontrol-relay:1.1.0")
				if (project.path != ":app:relay-provider-api")
					add("api", project(":app:relay-provider-api"))
			}
			tasks.withType<KotlinCompile>().all {
				compilerOptions {
					freeCompilerArgs.add("-opt-in=kotlin.ExperimentalUnsignedTypes")
				}
			}
		}
	}
}

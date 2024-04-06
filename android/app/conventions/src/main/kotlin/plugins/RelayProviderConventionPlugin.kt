package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

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
				if (project.path != ":app:relay-provider-api")
					add("api", project(":app:relay-provider-api"))
			}
		}
	}
}

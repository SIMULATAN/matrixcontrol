package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidComposeConventionPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		with(project) {
			with(dependencies) {
				addComposeDependencies()
			}
		}
	}
}

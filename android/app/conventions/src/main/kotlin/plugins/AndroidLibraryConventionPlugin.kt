package plugins

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		with(project) {
			with(pluginManager) {
				apply("com.android.library")
				baseAndroidPlugins()
			}
			extensions.configure<LibraryExtension> {
				baseAndroidConfiguration()
			}
		}
	}

}

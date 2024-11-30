import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "2.1.0" apply false
	kotlin("multiplatform") version "2.1.0" apply false
}

group = "com.github.simulatan"
version = "1.1.0"

repositories {
	mavenCentral()
}

subprojects {
	tasks.withType<KotlinCompile> {
		compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.ExperimentalUnsignedTypes,kotlin.ExperimentalStdlibApi")
	}
}

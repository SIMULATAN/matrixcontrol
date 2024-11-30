plugins {
	kotlin("multiplatform")
	`maven-publish`
}

dependencies {
	commonMainImplementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
	commonTestImplementation("org.jetbrains.kotlin:kotlin-test")
}

publishing {
	publications.create<MavenPublication>("maven") {
		val subprojectJarName = project.name
		artifactId = "matrixcontrol-$subprojectJarName"
		from(components["kotlin"])
		pom {
			name.set(artifactId)
		}
	}
}

kotlin {
	jvm()
	linuxX64()
}

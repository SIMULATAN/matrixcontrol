plugins {
	kotlin("jvm")
	`maven-publish`
}

dependencies {
	testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
	useJUnitPlatform()
}

publishing {
	publications.create<MavenPublication>("mavenJava") {
		val subprojectJarName = tasks.jar.get().archiveBaseName.get()
		artifactId = "matrixcontrol-$subprojectJarName"
		from(components["java"])
		pom {
			name.set(artifactId)
		}
	}
}

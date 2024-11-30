plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    api("com.fazecast:jSerialComm:2.10.4")
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

plugins {
    kotlin("jvm") version "1.9.21"
    `maven-publish`
}

group = "com.github.simulatan.matrixcontrol"
version = "1.1.0"

repositories {
    mavenCentral()
}

dependencies {
    api("com.fazecast:jSerialComm:2.10.4")
}

kotlin {
    jvmToolchain(17)
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

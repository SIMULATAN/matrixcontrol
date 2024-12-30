plugins {
	kotlin("multiplatform")
	kotlin("plugin.serialization")
}

repositories {
	mavenCentral()
	mavenLocal()
	maven("https://maven.simulatan.me/releases")
}

dependencies {
	commonMainImplementation(project(":protocol"))

	commonMainImplementation("me.simulatan.uwz:uwz:1.0.0")
	commonMainImplementation("com.squareup.okio:okio:3.9.1")
	commonMainImplementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
	commonMainImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0-RC")
	commonMainImplementation("com.charleskorn.kaml:kaml:0.67.0")

	val ktorVersion = "3.0.3"
	commonMainImplementation("io.ktor:ktor-network:$ktorVersion")
	commonMainImplementation("io.ktor:ktor-client-core:$ktorVersion")
	commonMainImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
	commonMainImplementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
}

kotlin {
	targets {
		// development use mainly
		jvm()
		linuxX64 {
			binaries {
				executable()
			}
		}
	}
}

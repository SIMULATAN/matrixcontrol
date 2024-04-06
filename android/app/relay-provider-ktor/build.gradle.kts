plugins {
	id("matrixcontrol.relay-provider")
}

dependencies {
	implementation(platform("io.ktor:ktor-bom:3.0.0-beta-1"))
	implementation("io.ktor:ktor-client-core")
	implementation("io.ktor:ktor-client-android")
}

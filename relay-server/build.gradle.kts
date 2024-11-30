plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

dependencies {
    val ktorVersion = "3.0.1"
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation(project(":protocol"))
    implementation(project(":relay"))
}

application {
    mainClass = "com.github.simulatan.matrixcontrol.relay.server.RelayServerKt"
}

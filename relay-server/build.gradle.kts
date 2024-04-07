plugins {
    kotlin("jvm") version "1.9.21"
    application
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "com.github.simulatan.matrixcontrol"
version = "1.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:5.6.3")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation(project(":protocol"))
    implementation(project(":relay"))
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass = "com.github.simulatan.matrixcontrol.relay.server.RelayServerKt"
}

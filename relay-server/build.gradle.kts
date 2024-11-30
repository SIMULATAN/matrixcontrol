plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

dependencies {
    implementation("io.javalin:javalin:5.6.3")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation(project(":protocol"))
    implementation(project(":relay"))
}

application {
    mainClass = "com.github.simulatan.matrixcontrol.relay.server.RelayServerKt"
}

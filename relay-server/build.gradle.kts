plugins {
    kotlin("jvm") version "1.9.21"
}

group = "com.github.simulatan.matrixcontrol"
version = "1.0-SNAPSHOT"

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

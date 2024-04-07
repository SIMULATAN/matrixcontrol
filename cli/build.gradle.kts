plugins {
    kotlin("jvm")
}

group = "com.github.simulatan.matrixcontrol"
version = "1.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":relay"))
    implementation(project(":protocol"))
}

kotlin {
    jvmToolchain(17)
}

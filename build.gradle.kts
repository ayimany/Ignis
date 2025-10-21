plugins {
    kotlin("jvm") version "2.2.20"
}

group = "xyz.ayimany"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.xenomachina:kotlin-argparser:2.0.7")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(24)
}
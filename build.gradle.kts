import kr.entree.spigradle.kotlin.*
import kr.entree.spigradle.data.Load
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    id("kr.entree.spigradle") version "2.4.2"
}

group = "com.github.opentypefont"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(spigot())
    compileOnly(paper())

    testImplementation(kotlin("test"))
}

spigot {
    name = project.name.split('-').joinToString("") { it.capitalize() }
    description = "Anonymous survival plugin"
    load = Load.STARTUP
    apiVersion = "1.19"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
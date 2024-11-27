import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("commons-io:commons-io:2.11.0")
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")
    implementation("io.rsocket:rsocket-core:1.1.2")
    implementation("io.rsocket:rsocket-transport-netty:1.1.2")
    implementation("io.projectreactor:reactor-core:3.4.34")
    implementation("io.netty:netty-all:4.1.79.Final")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}
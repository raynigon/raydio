import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    jacoco
    id("groovy")
    id("org.springframework.boot") version "2.4.1"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("nebula.ospackage") version "8.4.2"
    id("com.google.cloud.tools.jib") version "2.7.1"

    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
    kotlin("plugin.jpa") version "1.4.21"
    kotlin("kapt") version "1.4.21"
}

group = "io.rayd"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.liquibase:liquibase-core")
    implementation("org.xerial:sqlite-jdbc:3.34.0")
    implementation("com.github.gwenn:sqlite-dialect:0.1.1")
    implementation("org.mapstruct:mapstruct:1.4.1.Final")
    // Sound Libraries
    implementation("com.googlecode.soundlibs:mp3spi:1.9.5.4")
    testImplementation("org.testcontainers:spock:1.15.1")
    testImplementation("org.codehaus.groovy:groovy:3.0.7")
    testImplementation("org.spockframework:spock-core:2.0-M4-groovy-3.0")
    testImplementation("org.spockframework:spock-spring:2.0-M4-groovy-3.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.github.tomakehurst:wiremock:2.27.2")
    testImplementation("com.github.tomjankes:wiremock-groovy:0.2.0")

    // Annotation Processor
    kapt("org.mapstruct:mapstruct-processor:1.4.1.Final")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

apply(from = "gradle/packages.gradle")
apply(from = "gradle/docker.gradle")

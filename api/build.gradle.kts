plugins {
    kotlin("jvm") version "1.6.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    application
}

group = "ca.vikelabs"
version = "1.0-SNAPSHOT"

application {
    applicationName = "maps"
    mainClass.set("$group.maps.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.http4k:http4k-bom:4.16.2.0"))
    implementation(group = "org.http4k", name = "http4k-core")
    implementation(group = "org.http4k", name = "http4k-contract")
    implementation(group = "org.http4k", name = "http4k-format-jackson")
    implementation(group = "org.http4k", name = "http4k-client-apache")

    testImplementation(group = "org.jetbrains.kotlin", name = "kotlin-test-junit5")
    testImplementation(group = "org.http4k", name = "http4k-testing-approval")
    testImplementation(group = "org.http4k", name = "http4k-testing-hamkrest")
    testImplementation(group = "com.jayway.jsonpath", name = "json-path", version = "2.6.0")
}

tasks.withType<Test> {
    useJUnitPlatform {
        systemProperties["junit.jupiter.execution.parallel.enabled"] = true
        systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
        maxParallelForks = Runtime.getRuntime().availableProcessors() / 2
    }
    reports.html.required.set(false)
    reports.junitXml.required.set(false)
}

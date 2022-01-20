plugins {
    kotlin("jvm") version "1.6.0"
    id("nu.studer.jooq") version "6.0.1"
    application
}

group = "ca.vikelabs"
version = "1.0-SNAPSHOT"

application {
    applicationName = "maps"
    mainClass.set("$group.$applicationName.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    // http4k
    implementation(platform("org.http4k:http4k-bom:4.17.2.0"))
    implementation(group = "org.http4k", name = "http4k-core")
    implementation(group = "org.http4k", name = "http4k-contract")
    implementation(group = "org.http4k", name = "http4k-format-jackson")
    implementation(group = "org.http4k", name = "http4k-client-apache")
    implementation(group = "org.http4k", name = "http4k-server-jetty")

    // logging
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "2.0.0-alpha5")
    implementation(group = "io.github.microutils", name = "kotlin-logging-jvm", version = "2.0.10")

    // testing
    testImplementation(group = "org.jetbrains.kotlin", name = "kotlin-test-junit5")

    // database
    implementation(group = "org.jooq", name = "jooq", version = "3.16.2")
    implementation("org.postgresql:postgresql:42.2.14")
    jooqGenerator("org.postgresql:postgresql:42.2.14")

    // http4k testing
    testImplementation(group = "org.http4k", name = "http4k-testing-approval")
    testImplementation(group = "org.http4k", name = "http4k-testing-hamkrest")

    // jsonPath
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

jooq {
    configurations {
        create("main") {  // name of the jOOQ configuration
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/mapuvic"
                    user = "uvic"
                    password = "uvic"
                }
                generator.apply {
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    generate.apply {
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "ca.vikelabs.maps.domain"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

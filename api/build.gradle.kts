import nu.studer.gradle.jooq.JooqGenerate
import org.jooq.meta.jaxb.Logging

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

object Version {
    const val jooq = "3.16.2"
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
    implementation(group = "org.jooq", name = "jooq", version = Version.jooq)
    implementation("org.postgresql:postgresql:42.2.14")
    jooqGenerator("org.postgresql:postgresql:42.2.14")
    implementation("com.zaxxer:HikariCP:5.0.1")

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

tasks.withType<JooqGenerate> {
    inputs.dir(rootDir.resolve("database"))
    inputs.file(rootDir.resolve("db.Dockerfile"))
    allInputsDeclared.set(true)
    outputs.dir(outputDir)
}

tasks.withType<JavaCompile> {
    targetCompatibility = "1.8"
}

fun Map<String, String>.getOrLogAndDefault(key: String, default: String) =
    this[key] ?: run { println("No $key found in environment. Defaulting to $default"); default }

jooq {
    val env = System.getenv()
    version.set(Version.jooq)
    configurations {
        create("main") {  // name of the jOOQ configuration
            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url =
                        "jdbc:postgresql://${
                            env.getOrLogAndDefault(
                                "DATABASE_SERVER_NAME",
                                "localhost"
                            )
                        }:${
                            env.getOrLogAndDefault("DATABASE_PORT", "5432")
                        }/${
                            env.getOrLogAndDefault(
                                "DATABASE_NAME",
                                "mapuvic"
                            )
                        }"
                    user = env.getOrLogAndDefault("DATABASE_USERNAME", "uvic")
                    password = env.getOrLogAndDefault("DATABASE_PASSWORD", "uvic")
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

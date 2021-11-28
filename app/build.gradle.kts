import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node") version "3.0.1"
    base
}

repositories {
    mavenCentral()
}

node {
    version.set("16.13.0")
    download.set(true)
}

tasks {
    val generateClient by registering(NpmTask::class) {
        description = "Creates the client for hitting the maps api"
        dependsOn(npmInstall)
        args.set(listOf("run", "generate"))
    }

    val dev by registering(NpmTask::class) {
        dependsOn(assemble)
        description = "Runs the a development build of the project "
        args.set(listOf("run", "dev"))
    }

    val run by registering(NpmTask::class) {
        dependsOn(assemble)
        description = "Runs a production build of the project"
        args.set(listOf("run", "start"))
    }

    val validate by registering(NpmTask::class) {
        description = "Tests the project"
        args.set(listOf("run", "validate"))
    }

    assemble {
        dependsOn(npmInstall)
        dependsOn(generateClient)
    }

    check {
        dependsOn(validate)
    }
}


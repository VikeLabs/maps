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
        description = "Runs the a development build of the project "
        dependsOn(npmInstall)
        dependsOn(generateClient)
        args.set(listOf("run", "dev"))
    }

    val run by registering(NpmTask::class) {
        description = "Runs a production build of the project"
        dependsOn(assemble)
        args.set(listOf("run", "serve"))
    }

    val validate by registering(NpmTask::class) {
        description = "runs svelte-check on the project"
        args.set(listOf("run", "check"))
    }

    val buildProd by registering(NpmTask::class) {
        description = "creates an optimized production build"
        dependsOn(npmInstall)
        dependsOn(generateClient)
        args.set(listOf("run", "build"))
    }

    assemble {
        dependsOn(npmInstall)
        dependsOn(generateClient)
        dependsOn(buildProd)
    }

    check {
        dependsOn(validate)
    }
}


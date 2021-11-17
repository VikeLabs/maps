import com.github.gradle.node.npm.task.NpmTask


plugins {
    id("com.github.node-gradle.node") version "3.0.1"
}

repositories {
    mavenCentral()
}

node {
    version.set("16.13.0")
    download.set(true)
}

tasks.register(name = "generateClient", type = NpmTask::class) {
    description = "Creates the client for hitting the maps api"
    dependsOn("npmInstall")
    args.set(listOf("run", "generate"))
}

tasks.create(name = "run", type = NpmTask::class) {
    description = "Runs the project in develop"
    dependsOn.add("npmInstall")
    dependsOn.add("generateClient")
    args.set(listOf("run", "dev"))
}

tasks.create(name = "test", type = NpmTask::class) {
    description = "Tests the project"
    dependsOn.add("npmInstall")
    dependsOn.add("generateClient")
    args.set(listOf("run", "test", "--", "--watchAll=false"))
}

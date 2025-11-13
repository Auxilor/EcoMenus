group = "com.willfp"
version = rootProject.version

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    compileOnly("com.github.ben-manes.caffeine:caffeine:3.1.5")

    implementation("com.willfp:ecomponent:1.4.1")
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            groupId = project.group.toString()
            version = project.version.toString()
            artifactId = rootProject.name

            artifact(rootProject.tasks.shadowJar.get().archiveFile)
        }
    }
}

tasks {
    build {
        dependsOn(publishToMavenLocal)
    }
}

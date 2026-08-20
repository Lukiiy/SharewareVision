plugins {
    id("multiloader-common")
}

configurations {
    maybeCreate("commonJava").apply {
        isCanBeResolved = true
    }

    maybeCreate("commonResources").apply {
        isCanBeResolved = true
    }
}

dependencies {
    val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)

    compileOnly(project(":common")) {
        attributes {
            attribute(loaderAttribute, "common")
        }
    }

    "commonJava"(project(mapOf("path" to ":common", "configuration" to "commonJava")))
    "commonResources"(project(mapOf("path" to ":common", "configuration" to "commonResources")))
}

val genDocs = rootProject.findProperty("enableJavadoc")?.toString()?.toBoolean() ?: false

tasks {
    compileJava {
        dependsOn(configurations.named("commonJava"))
        source(configurations.named("commonJava"))
    }

    processResources {
        dependsOn(configurations.named("commonResources"))
        from(configurations.named("commonResources"))
    }

    if (genDocs) {
        javadoc {
            dependsOn(configurations.named("commonJava"))
            source(configurations.named("commonJava"))
        }
    }

    jar {
        dependsOn(configurations.named("commonJava"))
        from(configurations.named("commonJava"))

        dependsOn(configurations.named("commonResources"))
        from(configurations.named("commonResources"))
    }
}
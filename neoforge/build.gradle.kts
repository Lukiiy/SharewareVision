plugins {
    id("multiloader-loader")
    id("net.neoforged.moddev")
}

val modId = rootProject.property("modId").toString()

neoForge {
    version = rootProject.property("neoforgeVer").toString()

    project(":common").file("src/main/resources/META-INF/accesstransformer.cfg").apply {
        if (exists()) accessTransformers.from(this.absolutePath)
    }

    runs {
        configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", modId)

            ideName = "NeoForge ${name.replaceFirstChar { it.uppercase() }} (${rootProject.path})" // Unify the run config names with fabric
        }

        create("client") {
            client()

            gameDirectory = mkdir(file("runs/client"))
        }

        create("data") {
            clientData()

            gameDirectory = mkdir(file("runs/data"))

            programArguments.addAll("--mod", modId, "--all", "--output", file("src/generated/resources/").absolutePath, "--existing", file("src/main/resources/").absolutePath)
        }

        create("server") {
            server()

            gameDirectory = mkdir(file("runs/server"))
        }
    }

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
        }
    }
}

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)

val variants = mutableListOf("apiElements", "runtimeElements", "sourcesElements").apply {
    if (rootProject.findProperty("enableJavadoc")?.toString()?.toBoolean() ?: false) add("javadocElements")
}

variants.forEach {
    configurations.named(it) {
        attributes.attribute(loaderAttribute, "neoforge")
    }
}

sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName, getTaskName(null, "jarJar")).forEach {
        configurations.named(it) {
            attributes.attribute(loaderAttribute, "neoforge")
        }
    }
}
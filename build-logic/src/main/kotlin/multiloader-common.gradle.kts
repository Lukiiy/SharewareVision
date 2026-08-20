plugins {
    java
    id("maven-publish")
}

val modId = rootProject.property("modId").toString()
val minecraftVersion = rootProject.property("minecraftVer").toString()
val javaVersion = rootProject.property("javaVer").toString()
val modName = rootProject.property("modName").toString()
val modAuthor = rootProject.property("modAuthor").toString()
val minecraftVersionRange = rootProject.property("minecraftVerRange").toString()
val fabricVer = rootProject.property("fabricVer").toString()
val fabricLoaderVer = rootProject.property("fabricLoaderVer").toString()
val license = rootProject.property("license").toString()
val neoforgeVer = rootProject.property("neoforgeVer").toString()
val neoforgeVerRange = rootProject.property("neoforgeLoaderVerRange").toString()
val credits = rootProject.property("credits").toString()

base.archivesName.set("$modId-${project.name}-$minecraftVersion")

val genDocs = rootProject.findProperty("enableJavadoc")?.toString()?.toBoolean() ?: false

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))

    withSourcesJar()

    if (genDocs) withJavadocJar()
}

tasks {
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        from(rootProject.file("LICENSE")) {
            rename { "${it}_$modName" }
        }

        manifest {
            attributes(
                mapOf("Specification-Title" to modName, "Specification-Vendor" to modAuthor, "Specification-Version" to archiveVersion.get(), "Implementation-Title" to project.name, "Implementation-Version" to archiveVersion.get(), "Implementation-Vendor" to modAuthor, "Built-On-Minecraft" to minecraftVersion)
            )
        }
    }

    processResources {
        val expandProps = mapOf("version" to version, "group" to project.group, "minecraftVer" to minecraftVersion, "minecraftVerRange" to minecraftVersionRange, "fabricVer" to fabricVer, "fabricLoaderVer" to fabricLoaderVer, "modName" to modName, "modAuthor" to modAuthor, "modId" to modId, "license" to license, "description" to (project.description ?: ""), "neoforgeVer" to neoforgeVer, "neoforgeLoaderVerRange" to neoforgeVerRange, "credits" to credits, "javaVer" to javaVersion)

        val jsonExpandProps = expandProps.mapValues { (_, value) ->
            if (value is String) value.replace("\n", "\\n") else value
        }

        filesMatching(listOf("META-INF/mods.toml", "META-INF/neoforge.mods.toml")) {
            expand(expandProps)
        }

        filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "*.mixins.json")) {
            expand(jsonExpandProps)
        }

        inputs.properties(expandProps)
    }
}

publishing.publications {
    create<MavenPublication>("mavenJava") {
        artifactId = base.archivesName.get()

        from(components["java"])
    }
}
plugins {
    id("multiloader-loader")
    id("net.fabricmc.fabric-loom")
}

val modId = rootProject.property("modId").toString()
val minecraftVersion = rootProject.property("minecraftVer").toString()
val fabricLoaderVer = rootProject.property("fabricLoaderVer").toString()
val fabricVer = rootProject.property("fabricVer").toString()

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    implementation("net.fabricmc:fabric-loader:$fabricLoaderVer")
    implementation("net.fabricmc.fabric-api:fabric-api:$fabricVer")
}

loom {
    project(":common").file("src/main/resources/$modId.accesswidener").apply {
        if (exists()) accessWidenerPath.set(this)
    }

    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir = "runs/client"
        }

        named("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir = "runs/server"
        }
    }
}

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)

val variants = mutableListOf("apiElements", "runtimeElements", "sourcesElements", "includeInternal", "modCompileClasspath").apply {
    if (rootProject.findProperty("enableJavadoc")?.toString()?.toBoolean() ?: false) add("javadocElements")
}

variants.forEach {
    configurations.named(it) {
        attributes.attribute(loaderAttribute, "fabric")
    }
}

sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName).forEach {
        configurations.named(it) {
            attributes.attribute(loaderAttribute, "fabric")
        }
    }
}
import org.gradle.api.plugins.JavaPluginExtension

plugins {
    alias(libs.plugins.screaming.plugin.builder) apply(false)
}

defaultTasks("clean", "screamCompile")//, 'allowJavadocUpload'

subprojects {
    if (!project.file("src").exists() && !project.file("build.gradle").exists()) {
        extensions.extraProperties["onlyPomArtifact"] = true

        apply(plugin = "org.screamingsandals.plugin-builder-lite")
    } else {
        apply(plugin = "java")
        apply(plugin = "java-library")
        apply(plugin = "org.screamingsandals.plugin-builder")
    }

    if (name.matches(Regex("[a-z]+-[a-z]+")) && !name.endsWith("-common")) {
        var foundAnotherModule = false

        val platform = name.substringAfter("-")

        // modules for NMS based platforms can have shared vanilla modules
        if (platform in listOf("bukkit", "sponge")) {
            val vanillaProject = rootProject.findProject(":${name.substringBefore("-")}-vanilla")
            if (vanillaProject != null) {
                dependencies {
                    "api"(vanillaProject)
                }
                foundAnotherModule = true
            }
        }

        if (!foundAnotherModule) {
            val commonProject = rootProject.findProject(":${name.substringBefore("-")}-common")

            if (commonProject != null) {
                dependencies {
                    "api"(commonProject)
                }
            }
        }
    }

    extensions.configure<JavaPluginExtension>("java") {
        disableAutoTargetJvm()
        sourceCompatibility = JavaVersion.VERSION_11
    }

    repositories {
        maven("https://libraries.minecraft.net")
        maven("https://jitpack.io")
        maven("https://repo-new.spongepowered.org/repository/maven-public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.screamingsandals.org/public/")
        maven("https://repo.viaversion.com/")
    }
}

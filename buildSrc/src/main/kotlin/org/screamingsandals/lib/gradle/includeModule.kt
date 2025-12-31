package org.screamingsandals.lib.gradle

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

fun Project.includeModule(module: String) {
    if (!name.endsWith("-common")) {
        throw UnsupportedOperationException("This method can be used only in common modules!")
    }

    dependencies {
        "api"(project(":${module}-common"))
    }

    val baseName = name.removeSuffix("-common")
    val moduleRegex = Regex("$baseName-[^-]+")

    rootProject.subprojects.forEach { sub ->
        if (!sub.name.endsWith("-common") && moduleRegex.matches(sub.name)) {
            val platform = sub.name.substringAfter("-")
            val moduleProject = rootProject.findProject(":$module-$platform")

            if (moduleProject != null) {
                sub.dependencies {
                    "api"(moduleProject)

                    if (moduleProject.name == "core-bukkit") {
                        // Fix another source sets missing when compiling
                        val sourceSets = moduleProject.extensions.getByType<SourceSetContainer>()

                        "compileOnly"(sourceSets.named("main").get().output)
                    }
                }
            }
        }
    }
}
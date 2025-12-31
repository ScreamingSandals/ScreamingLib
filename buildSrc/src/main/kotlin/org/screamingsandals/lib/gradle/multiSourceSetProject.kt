package org.screamingsandals.lib.gradle

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named

fun Project.initSupportSourceSets(
    versions: Collection<String>,
    createCommon: Boolean = false
) {
    val sourceSets = extensions.getByType<SourceSetContainer>()

    val commonSourceSet = if (createCommon) {
        sourceSets.create("common") {
            java {
            }
        }
    } else {
        null
    }

    for (ver in versions) {
        val support = sourceSets.create("support_${ver}") {
            if (commonSourceSet != null) {
                compileClasspath += commonSourceSet.output
            }

            java {
            }
        }

        sourceSets.named("main") {
            compileClasspath += support.output
            output.dir(
                mapOf("builtBy" to tasks.named("compileSupport_${ver}Java")),
                support.output.classesDirs
            )
        }
    }

    if (commonSourceSet != null) {
        val compileOnly = configurations.named("compileOnly").get()
        val api = configurations.named("api").get()

        val mainCompileOnly = configurations.create("mainCompileOnly")

        configurations.named("commonCompileOnly") {
            extendsFrom(mainCompileOnly, compileOnly, api)
        }

        for (ver in versions) {
            configurations.named("support_${ver}CompileOnly") {
                extendsFrom(compileOnly, api)
            }
        }

        sourceSets.named("main") {
            compileClasspath += mainCompileOnly + commonSourceSet.output
            output.dir(
                mapOf("builtBy" to tasks.named("compileCommonJava")),
                commonSourceSet.output.classesDirs
            )
            compileClasspath += mainCompileOnly
        }
    }
}

fun Project.fixSourceJar() {
    val sourceSets = extensions.getByType<SourceSetContainer>()

    tasks.findByName("sourceJar")?.let {
        tasks.named<Jar>("sourceJar") {
            sourceSets.matching { it.name != "test" }.forEach {
                from(it.allJava)
            }
        }
    }
}
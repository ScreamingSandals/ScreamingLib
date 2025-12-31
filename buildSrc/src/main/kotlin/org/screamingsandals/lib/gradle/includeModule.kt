package org.screamingsandals.lib.gradle

import org.gradle.api.Project

fun Project.includeModule(module: String) {

    // TODO: ACTUALLY REIMPLEMENT THE FUNCTION!!!!

    @Suppress("UNCHECKED_CAST")
    val includeModule = project.extensions.extraProperties["includeModule"]
            as groovy.lang.Closure<*>

    includeModule.call(module)
}
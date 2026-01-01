package org.screamingsandals.lib.gradle

import org.gradle.api.Project

fun Project.enableShadowPlugin() {
    @Suppress("UNCHECKED_CAST")
    val enableShadowPlugin = project.extensions.extraProperties["enableShadowPlugin"]
            as groovy.lang.Closure<*>

    enableShadowPlugin.call()
}
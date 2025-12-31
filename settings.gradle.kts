pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.screamingsandals.org/public/")
    }
}

// The result will be for Java 11 but some dependencies requires Java 17 or Java 21
val javaVersion = System.getProperty("java.class.version").toDouble()
if (javaVersion < 65.0) {
    throw GradleException("ScreamingLib requires JDK 21 to compile")
}

rootProject.name = "ScreamingLib"

// Shared content
include("api-utils")
registerModule("utils")
include("nms")
include("minitag")
include("nbt")
include("shared-core")
include("annotation")
registerModule("spectator", "adventure", "bungee")

// Main modules
registerModule("core", "bukkit", "vanilla"/*, "minestom"*/) //TODO: add others (minestom, sponge)
registerModule("proxy", "bungee", "velocity")

// Extensions (Optional modules)
include(":lang")
project(":lang").projectDir = file("extensions/lang")
include(":kotlin-extra")
project(":kotlin-extra").projectDir = file("extensions/kotlin-extra")
registerExtension("sidebar", "bukkit") //TODO: add others
registerExtension("healthindicator", "bukkit") //TODO: add others
registerExtension("npc", "bukkit") //TODO: add others
registerExtension("hologram", "bukkit") //TODO: add others
registerExtension("visuals-api", "bukkit") //TODO: add others
registerExtension("signs", "bukkit") //TODO: add others
registerExtension("placeholders", "bukkit") //TODO: add others
registerExtension("economy", "bukkit")
registerExtension("packets", "vanilla", "bukkit") //TODO: add others
registerExtension("ai", "bukkit")
registerExtension("fakedeath", "bukkit")
registerExtension("cloud", "bukkit", /*"minestom" , "sponge",*/ "bungee", "velocity", "extras")


fun registerModule(name: String, vararg platforms: String) {
    include(":$name-common")
    project(":$name-common").projectDir = file("$name/common")
    platforms.forEach {
        include(":$name-$it")
        project(":$name-$it").projectDir = file("$name/$it")
    }
}

fun registerExtension(name: String, vararg platforms: String) {
    include(":$name-common")
    project(":$name-common").projectDir = file("extensions/$name/common")
    platforms.forEach {
        include(":$name-$it")
        project(":$name-$it").projectDir = file("extensions/$name/$it")
    }
}
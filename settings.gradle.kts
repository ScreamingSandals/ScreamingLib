pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.screamingsandals.org/public/")
    }
}

// The result will be for Java 11 but some dependencies requires Java 17, 21 or 25
val javaVersion = System.getProperty("java.class.version").toDouble()
if (javaVersion < 69.0) {
    throw GradleException("ScreamingLib requires JDK 25 to compile")
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
setupProject("lang", "extensions/lang")
setupProject("kotlin-extra", "extensions/kotlin-extra")
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
    setupProject("$name-common", "$name/common")
    platforms.forEach {
        setupProject("$name-$it", "$name/$it", mkdir=true)
    }
}

fun registerExtension(name: String, vararg platforms: String) {
    setupProject("$name-common", "extensions/$name/common")
    platforms.forEach {
        setupProject("$name-$it", "extensions/$name/$it", mkdir=true)
    }
}

fun setupProject(name: String, folder: String, mkdir: Boolean = false) {
    include(name)
    project(":$name").let {
        it.projectDir = file(folder)
        if (mkdir) {
            it.projectDir.mkdirs()
        }
    }
}
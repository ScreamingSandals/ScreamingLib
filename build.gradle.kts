import io.freefair.gradle.plugins.lombok.LombokPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.screamingsandals.gradle.builder.*

plugins {
    alias(libs.plugins.screaming.plugin.builder) apply(false)
    alias(libs.plugins.lombok) apply(false)
}

defaultTasks("clean", "build")

subprojects {
    apply<BuilderPlugin>()

    repositories {
        mavenCentral()
        maven("https://libraries.minecraft.net")
        maven("https://jitpack.io")
        maven("https://repo-new.spongepowered.org/repository/maven-public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.screamingsandals.org/public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.viaversion.com/")
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

    val onlyPomArtifact = !project.file("src").exists() && !project.file("build.gradle.kts").exists()
    if (!onlyPomArtifact) {
        apply<JavaPlugin>()
        apply<LombokPlugin>()

        configureJavac(JavaVersion.VERSION_11)
        configureLicenser()

        extensions.configure<JavaPluginExtension>("java") {
            disableAutoTargetJvm()
        }

        dependencies {
            "compileOnly"(rootProject.libs.jetbrains.annotations)
        }

        if (name != "nms") {
            configureSourcesJar(
                // allow every non-test source set to be in sources jar
                predicate = { it.name != "test" }
            )
        }
    }

    project.afterEvaluate {
        // Some subprojects may configure shadowJar later, so we need to postpone this to afterEvaluate

        setupMavenPublishing(onlyPomArtifact = onlyPomArtifact, addSourceJar = !onlyPomArtifact && name != "nms") {
            pom {
                name.set("ScreamingLib")
                description.set("Cross-platform library for developing Minecraft: Java Edition server plugins.")
                url.set("https://github.com/ScreamingSandals/ScreamingLib")
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://github.com/ScreamingSandals/ScreamingLib/blob/ver/2.0.x/LICENSE")
                    }
                }
            }
        }
    }

    setupMavenRepositoriesFromProperties()
}

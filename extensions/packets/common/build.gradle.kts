import org.screamingsandals.lib.gradle.includeModule

includeModule("core")

dependencies {
    compileOnly(libs.netty)
}
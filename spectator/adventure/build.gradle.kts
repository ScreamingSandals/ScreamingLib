import org.screamingsandals.lib.gradle.initSupportSourceSets

initSupportSourceSets(listOf("4"), createCommon = false)

dependencies {
    compileOnly(libs.adventure5.api)
    compileOnly(libs.adventure5.gson)
    compileOnly(libs.adventure5.json)
    compileOnly(libs.adventure5.legacy)
    compileOnly(libs.adventure5.plain)
    "support_4CompileOnly"(project(":spectator-common"))
    "support_4CompileOnly"(libs.adventure4.api)
    "support_4CompileOnly"(libs.adventure4.gson)
    "support_4CompileOnly"(libs.adventure4.json)
    "support_4CompileOnly"(libs.adventure4.legacy)
    "support_4CompileOnly"(libs.adventure4.plain)
}
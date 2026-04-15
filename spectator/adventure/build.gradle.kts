import org.screamingsandals.lib.gradle.initSupportSourceSets

initSupportSourceSets(listOf("4"), createCommon = false)

dependencies {
    // TODO: depend on Adventure 5 in main module when it releases
    compileOnly(libs.adventure.api)
    compileOnly(libs.adventure.gson)
    compileOnly(libs.adventure.json)
    compileOnly(libs.adventure.legacy)
    compileOnly(libs.adventure.plain)
    "support_4CompileOnly"(libs.adventure4.api)
    "support_4CompileOnly"(libs.adventure4.gson)
    "support_4CompileOnly"(libs.adventure4.json)
    "support_4CompileOnly"(libs.adventure4.legacy)
    "support_4CompileOnly"(libs.adventure4.plain)
}
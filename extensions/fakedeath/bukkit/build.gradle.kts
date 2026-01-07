import org.screamingsandals.lib.gradle.initSupportSourceSets

initSupportSourceSets(listOf("1_20_1"), createCommon = false)

dependencies {
    compileOnly(libs.paper)
    "support_1_20_1CompileOnly"(libs.paper.support.one.twenty.one)
}
import org.screamingsandals.lib.gradle.initSupportSourceSets
import org.screamingsandals.lib.gradle.fixSourceJar

val supportVersions = mapOf(
    "1_21_3" to libs.paper.support.one.twentyone.three,
    "1_21_1" to libs.paper.support.one.twentyone.one,
    "1_20_6" to libs.paper.support.one.twenty.six,
    "1_20_1" to libs.paper.support.one.twenty.one,
    "1_12_2" to libs.paper.support.one.twelve.two,
    "1_8_8" to libs.paper.support.one.eight.eight,

    "datafixer2" to libs.datafixerupper2,
)

initSupportSourceSets(supportVersions.keys, createCommon = true)

dependencies {
    api(project(":nms")) {
        targetConfiguration = "shadow"
    }

    api(project(":spectator-adventure"))
    api(project(":spectator-bungee"))

    "mainCompileOnly"(libs.paper)
    "mainCompileOnly"(libs.datafixerupper)
    for (ver in supportVersions) {
        "support_${ver.key}CompileOnly"(ver.value)
    }

    compileOnly(libs.netty)
    compileOnly(libs.viaversion)
    compileOnly(libs.protocolsupport)
}

fixSourceJar()
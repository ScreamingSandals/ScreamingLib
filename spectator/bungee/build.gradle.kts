import org.screamingsandals.lib.gradle.initSupportSourceSets
import org.screamingsandals.lib.gradle.fixSourceJar

initSupportSourceSets(listOf("1_21_R01"), createCommon = false)

dependencies {
    compileOnly(libs.bungeecord.chat)
    "support_1_21_R01CompileOnly"(libs.bungeecord.chat.support.one.twentyone.r01)
}

fixSourceJar()
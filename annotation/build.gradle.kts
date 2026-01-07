import org.screamingsandals.gradle.builder.configureShadowPlugin

dependencies {
    implementation(project(":utils-common"))
    implementation(project(":shared-core"))
    implementation(libs.javapoet)
    implementation(libs.guava)
    implementation(libs.configurate.yaml)
    implementation(libs.configurate.gson)
}

license {
    exclude("org/screamingsandals/lib/annotation/utils/JohnsonSimpleCycles.java")
}

configureShadowPlugin()
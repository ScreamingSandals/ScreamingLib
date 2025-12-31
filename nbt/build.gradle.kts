dependencies {
    compileOnly(libs.configurate.core) // not required

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.configurate.core)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}
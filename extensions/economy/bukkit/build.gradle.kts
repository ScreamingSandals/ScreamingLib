dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.vault) {
        isTransitive = false
    }
}
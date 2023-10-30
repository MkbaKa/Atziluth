
dependencies {
    compileOnly(fileTree("libs"))
    compileOnly(project(":common"))
    implementation(project(":hooker-mythicmobs4"))
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}
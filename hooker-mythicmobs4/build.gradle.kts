
dependencies {
    compileOnly(fileTree("libs"))
    compileOnly(project(":common"))
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}
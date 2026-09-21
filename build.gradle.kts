rootProject.name = "AFCV444"

file(rootDir).listFiles()?.forEach { dir ->
    if (dir.isDirectory && File(dir, "build.gradle.kts").exists()) {
        if (dir.name != "buildSrc") {
            include(":${dir.name}")
        }
    }
}

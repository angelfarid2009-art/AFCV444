plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}
android {
    compileSdk = 34
    namespace = "com.farid.otakustv"
    defaultConfig { minSdk = 21 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions { jvmTarget = "1.8" }
}
dependencies {
    compileOnly("com.github.recloudstream:cloudstream:master-SNAPSHOT")
}
tasks.register("make") {
    dependsOn("assembleDebug")
    doLast {
        val jarFile = file("build/intermediates/aar_main_jar/debug/classes.jar")
        val outFile = file("build/OtakusTV.cs3")
        outFile.parentFile.mkdirs()
        if (jarFile.exists()) jarFile.copyTo(outFile, overwrite = true)
    }
}

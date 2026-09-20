plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.github.recloudstream.gradle") version "0.0.1"
}

android {
    namespace = "com.example"
    compileSdk = 34
    defaultConfig { minSdk = 21 }
    compileOptions {
        sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
        targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
    }
    kotlinOptions { jvmTarget = "1.8" }
}

csExtension {
    name = "FaridExtensions"
    description = "Repo Farid - HenaoJara arreglado + OtakusTV"
    authors = listOf("Farid")
    lang = "es"
    version = 1
}
dependencies {
    implementation("com.github.Blatzar:NiceHttp:0.4.11")
    implementation("org.jsoup:jsoup:1.17.1")
}

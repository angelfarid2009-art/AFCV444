cloudstream {
    authors = listOf("Farid")
    description = "Extension de Farid"
    language = "es"
}
android {
    compileSdkVersion(34)
    namespace = "com.farid"
    
    defaultConfig {
        minSdk = 21
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

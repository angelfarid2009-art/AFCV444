plugins {
    id("com.lagradost.cloudstream3.gradle") version "0.6.2"
}

cloudstream {
    language = "es"
    authors = listOf("Farid")
    description = "Plugin de Farid"
}

android {
    namespace = "com.farid.afcv444"
}

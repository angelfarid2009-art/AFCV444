buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
    }
}

plugins {
    id("com.lagradost.cloudstream3.gradle") version "0.6.2" apply false
}

tasks.register("makePlugins") {
    dependsOn(subprojects.map { ":${it.name}:make" })
}

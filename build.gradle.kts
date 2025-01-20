// 根目录的 build.gradle 文件
plugins {
    kotlin("jvm") version "1.9.10" apply false
    id("com.android.application") version "8.1.1" apply false
    id("com.android.library") version "8.1.1" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Gradle 插件版本
        classpath("com.android.tools.build:gradle:8.1.1")
        // Google Services 插件版本
        classpath("com.google.gms:google-services:4.3.15")
        // Kotlin 插件版本
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
    }
}
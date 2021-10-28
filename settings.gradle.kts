rootProject.name = "spring-boot-kotlin-reactive-template"

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val dependencyManagementVersion: String by settings
    plugins {
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version dependencyManagementVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
    }
    repositories {
        gradlePluginPortal()
    }
}

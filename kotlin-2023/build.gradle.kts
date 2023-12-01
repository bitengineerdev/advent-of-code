plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.8.22"
}

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
    mavenCentral()
}

kotlin {
    // jvmToolchain will also set the java source and target compatibility
    jvmToolchain {
        // Normally we try to stay on the latest LTS
        languageVersion.set(JavaLanguageVersion.of(17))
        // Adoptium is the OpenJDK Temurin
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

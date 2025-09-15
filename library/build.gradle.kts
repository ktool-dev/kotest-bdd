import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.dokka)
}

kotlin {
    jvm {
        testRuns.named("test") {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
    }
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()
    mingwX64()
    macosX64()
    macosArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotest.framework.engine)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotest.framework.engine)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.kotest.runner.junit5)
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotlinReflect)
            }
        }
    }
}

object DeployConfig {
    const val ARTIFACT = "kotest-bdd"
    const val NAME = "Kotest BDD"
    const val DESCRIPTION = "Behavior Driven Development extensions for Kotest"
    const val INCEPTION_YEAR = "2025"
    const val DEV_ID = "aaronfreeman"
    const val DEV_NAME = "Aaron Freeman"
    const val DEV_EMAIL = "aaron@ktool.dev"
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

android {
    namespace = "dev.ktool.${DeployConfig.ARTIFACT.replace("-", "")}"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

mavenPublishing {
    val domain = "ktool.dev"
    val gitHubOrg = domain.replace(".", "-")
    val groupId = domain.split(".").reversed().joinToString(".")
    val version = "0.0.0"
    val projectUrl = "https://github.com/$gitHubOrg/${project.name}"

    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Dokka("javadocJar"),
            sourcesJar = true,
            androidVariantsToPublish = listOf("release"),
        )
    )

    publishToMavenCentral()

    signAllPublications()

    coordinates(groupId, DeployConfig.ARTIFACT, version)

    pom {
        name = DeployConfig.NAME
        description = DeployConfig.DESCRIPTION
        inceptionYear = DeployConfig.INCEPTION_YEAR
        url = projectUrl
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = DeployConfig.DEV_ID
                name = DeployConfig.DEV_NAME
                email = DeployConfig.DEV_EMAIL
                url = "https://github.com/${DeployConfig.DEV_ID}"
            }
        }
        scm {
            url = "https://github.com/$gitHubOrg/$groupId/"
            connection = "scm:git:git://github.com/$gitHubOrg/$groupId.git"
            developerConnection = "scm:git:ssh://git@github.com/$gitHubOrg/$groupId.git"
        }
    }
}

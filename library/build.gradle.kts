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

description = "Simple Behavior Driven Development extensions for Kotest"

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(tasks.dokkaGeneratePublicationHtml)
    from(tasks.dokkaGeneratePublicationHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

android {
    namespace = "dev.ktool.${project.name.replace("-", "")}"
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
    val rootName = rootProject.name
    val orgUrl = project.property("scm.org.url") as String
    val artifactId = "$rootName-${project.name}"
    val repoPath = project.property("scm.repo.path") as String
    val projectUrl = "https://$repoPath"

    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Dokka("javadocJar"),
            sourcesJar = true,
        )
    )

    publishToMavenCentral()

    signAllPublications()

    coordinates(project.group.toString(), artifactId, project.version.toString())

    pom {
        name = artifactId
        description = project.description
        inceptionYear = project.property("inception.year") as String
        url = projectUrl
        licenses {
            license {
                name = project.property("license.name") as String
                url = project.property("license.url") as String
                distribution = project.property("license.url") as String
            }
        }
        developers {
            developer {
                id = project.property("developer.id") as String
                name = project.property("developer.name") as String
                email = project.property("developer.email") as String
                url = orgUrl
            }
        }
        scm {
            url = projectUrl
            connection = "scm:git:git://$repoPath.git"
            developerConnection = "scm:git:ssh://git@$repoPath.git"
        }
    }
}

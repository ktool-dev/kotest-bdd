plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.deployer)
    id("org.jetbrains.dokka") version "1.9.20"
    `maven-publish`
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvm {
        testRuns.named("test") {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    linuxX64()
    linuxArm64()
    mingwX64()

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

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(tasks.dokkaHtml, tasks.sourcesJar)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

publishing {
    publications.withType<MavenPublication> {
        artifact(javadocJar)
    }
}

object DeployerSettings {
    const val NAME = "Kotest BDD"
    const val DESCRIPTION = "Behavior Driven Development extensions for Kotest"
    const val DEV_NAME = "Aaron Freeman"
    const val DEV_EMAIL = "aaron@kotool.dev"
}

deployer {
    content {
        kotlinComponents()
    }

    projectInfo {
        groupId = "dev.kotool"
        artifactId = project.name
        name = DeployerSettings.NAME
        description = DeployerSettings.DESCRIPTION
        url = "https://github.com/kotool-dev/${project.name}"
        license(apache2)
        developer(DeployerSettings.DEV_NAME, DeployerSettings.DEV_EMAIL, "Kotool", "https://kotool.dev")
    }

    release {
        release.version.set("0.0.0")
    }

    centralPortalSpec {
        signing.key = secret("SIGNING_KEY")
        signing.password = secret("SIGNING_PASSWORD")
        auth.user = secret("CENTRAL_PORTAL_USERNAME")
        auth.password = secret("CENTRAL_PORTAL_PASSWORD")
        allowMavenCentralSync = true
    }
}

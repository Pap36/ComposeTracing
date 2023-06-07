// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.0.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.21" apply false
    id("com.google.dagger.hilt.android") version "2.46.1" apply false
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            println("AAA")
            if (project.findProperty("composeCompilerReports") == "true") {
                println("BBB")
                freeCompilerArgs = freeCompilerArgs + "-P" + ("plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + project.buildDir.absolutePath + "/compose_compiler")

            }
            if (project.findProperty("composeCompilerReports") == "true") {
                println("CCC")
                freeCompilerArgs = freeCompilerArgs + "-P" + ("plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                        project.buildDir.absolutePath + "/compose_compiler")
            }
        }
    }
}
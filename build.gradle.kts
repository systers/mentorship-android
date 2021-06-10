// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.google.com/")
            name = "Google"
        }
    }

    dependencies {
        classpath(Dependencies.gradle_build_tool)
        classpath(Dependencies.kotlin_gradle_plugin)
        classpath(Dependencies.navigation_safe_args_plugin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.google.com/")
            name = "Google"
        }
        maven {
            url = uri("https://jitpack.io")
            name = "Jitpack"
        }
    }
    configureSpotless()
}

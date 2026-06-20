plugins {
    id("com.android.library")
    // org.jetbrains.kotlin.android is applied below in the body, not here.
    // During generatePrecompiledScriptPluginAccessors, the synthetic project inherits
    // kotlin-jvm from buildSrc's kotlin-dsl. Putting kotlin-android in plugins {}
    // causes a name conflict on the 'kotlin' extension. Body code is not executed
    // during accessor generation, so applying it here is safe.
}

// Must run before ivy.hilt and ivy.kotlinx-serialization are applied by ivy.module.
pluginManager.apply("org.jetbrains.kotlin.android")

val javaVersion = catalog.version("jvm-target")

android {
    compileOptions {
        sourceCompatibility = JavaVersion.valueOf("VERSION_$javaVersion")
        targetCompatibility = JavaVersion.valueOf("VERSION_$javaVersion")
    }

    compileSdk = catalog.version("compile-sdk").toInt()
    defaultConfig {
        minSdk = catalog.version("min-sdk").toInt()
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(javaVersion))
    }
}

gradle.projectsEvaluated {
    tasks.withType<Test> {
        maxHeapSize = "2048m"
    }
}

dependencies {
    implementation(libs.bundles.arrow)
    implementation(libs.bundles.kotlin)
    implementation(catalog.bundle("kotlin-android"))
    implementation(libs.timber)

    testImplementation(libs.bundles.testing)
}

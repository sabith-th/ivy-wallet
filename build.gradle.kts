// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Run with:
    // ./gradlew detekt // Simple report in the console
    // ./gradlew detektFormat // To check with enabled auto-correction
    id("ivy.detekt")
    id("com.jraska.module.graph.assertion")

    alias(libs.plugins.gradleWrapperUpgrade)

    // Kover 0.9.x uses TestResultsProvider.hasOutput(long, Destination) which was removed in
    // Gradle 9. Disabled until a Kover release adds Gradle 9 support.
    // alias(libs.plugins.koverPlugin)
}

// subprojects {
//     apply(plugin = "org.jetbrains.kotlinx.kover")
//     kover {
//         reports {
//             filters {
//                 excludes {
//                     classes(
//                         "*Activity",
//                         "*Activity\$*",
//                         "*.BuildConfig",
//                         "dagger.hilt.*",
//                         "hilt_aggregated_deps.*",
//                         "*.Hilt_*"
//                     )
//                     annotatedBy("@Composable")
//                 }
//             }
//         }
//     }
// }

wrapperUpgrade {
    gradle {
        create("ivyWallet") {
            repo.set("sabith-th/ivy-wallet")
            baseBranch.set("main")
        }
    }
}

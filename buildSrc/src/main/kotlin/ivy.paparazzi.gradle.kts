plugins {
    id("ivy.compose")
    id("app.cash.paparazzi")
}

// TODO Remove when https://github.com/google/guava/issues/6567 is fixed.
// See also: https://github.com/google/guava/issues/6801.
dependencies.constraints {
    testImplementation("com.google.guava:guava") {
        attributes {
            attribute(
                TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE,
                objects.named(TargetJvmEnvironment::class.java, TargetJvmEnvironment.STANDARD_JVM)
            )
        }
        because(
            "Paparazzi's layoutlib and sdk-common depend on Guava's -jre published variant." +
                    "See https://github.com/cashapp/paparazzi/issues/906."
        )
    }
}

// Paparazzi 2.0.0-alpha04 calls TestResultsProvider.hasOutput(long, Destination) when a
// screenshot test fails. Gradle 9.6.0 removed that overload, causing NoSuchMethodError.
// Keep Paparazzi tests out of testDebugUnitTest (which CI runs as the unit-test job) so
// the removed API is never reached there. verifyPaparazziDebug / recordPaparazziDebug are
// AGP-native tasks independent of testDebugUnitTest and still execute them correctly.
afterEvaluate {
    tasks.named<Test>("testDebugUnitTest") {
        filter {
            excludeTestsMatching("*PaparazziTest*")
            excludeTestsMatching("*ScreenshotTest*")
        }
    }
}

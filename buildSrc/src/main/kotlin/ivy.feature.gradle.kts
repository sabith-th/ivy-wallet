plugins {
    id("ivy.module")
    id("ivy.compose")
    // ivy.paparazzi removed from ivy.feature: Paparazzi 2.0.0-alpha04 wraps
    // TestResultsProvider with only the (long, Destination) overload; Gradle 9.6.0
    // calls (TestDescriptor, Destination), causing AbstractMethodError on every
    // testDebugUnitTest in any module that applies the Paparazzi plugin. Only
    // apply ivy.paparazzi in modules that actually have screenshot tests.
    org.jetbrains.kotlin.plugin.compose
}

plugins {
    id("ivy.feature")
}

android {
    namespace = "com.ivy.design"
    lint {
        // Slack compose-lints ParameterOrderDetector has an NPE bug on IvyIcon.kt
        disable += "ComposeParameterOrder"
    }
}

dependencies {
    implementation(projects.shared.base)
    implementation(projects.shared.ui.core)

    implementation(projects.shared.domain)
}
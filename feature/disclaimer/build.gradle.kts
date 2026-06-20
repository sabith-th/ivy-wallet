plugins {
    id("ivy.feature")
    id("ivy.paparazzi")
}

android {
    namespace = "com.ivy.disclaimer"
}

dependencies {
    implementation(projects.shared.data.core)
    implementation(projects.shared.ui.core)
    implementation(projects.shared.ui.navigation)

    testImplementation(projects.shared.ui.testing)
}

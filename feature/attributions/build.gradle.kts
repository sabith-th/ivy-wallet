plugins {
    id("ivy.feature")
    id("ivy.paparazzi")
}

android {
    namespace = "com.ivy.attributions"
}

dependencies {
    implementation(projects.shared.base)
    implementation(projects.shared.domain)
    implementation(projects.shared.ui.core)
    implementation(projects.shared.ui.navigation)

    testImplementation(projects.shared.ui.testing)
}

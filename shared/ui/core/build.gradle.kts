plugins {
    id("ivy.feature")
    id("ivy.paparazzi")
}

android {
    namespace = "com.ivy.ui"
}

dependencies {
    implementation(projects.shared.base)
    implementation(projects.shared.domain)
}
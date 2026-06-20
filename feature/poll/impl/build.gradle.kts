plugins {
  id("ivy.feature")
    id("ivy.paparazzi")
}

android {
  namespace = "com.ivy.poll.impl"
}

dependencies {
  implementation(projects.shared.base)
  implementation(projects.shared.data.core)
  implementation(projects.shared.domain)
  implementation(projects.shared.ui.core)
  implementation(projects.shared.ui.navigation)
  implementation(projects.feature.poll.public)

  implementation(libs.firebase.firestore)

  testImplementation(projects.shared.ui.testing)
}
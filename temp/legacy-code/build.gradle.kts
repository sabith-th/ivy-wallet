plugins {
    id("ivy.feature")
    id("ivy.room")
}

android {
    namespace = "com.ivy.legacy"
}

// Same Compose compiler 2.1.21 IrSourcePrinterVisitor workaround as temp:old-design.
composeCompiler {
    reportsDestination.unset()
    metricsDestination.unset()
}

dependencies {
    implementation(projects.shared.base)
    implementation(projects.shared.data.core)
    implementation(projects.shared.domain)
    implementation(projects.shared.ui.core)
    implementation(projects.shared.ui.navigation)
    implementation(projects.temp.oldDesign)

    implementation(libs.bundles.activity)
    implementation(libs.bundles.opencsv)
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.ktor)
    implementation(libs.androidx.work)
    implementation(libs.datastore)
    implementation(libs.keval)
    implementation(libs.androidx.recyclerview)
}

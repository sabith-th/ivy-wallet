plugins {
    id("ivy.feature")
}

android {
    namespace = "com.ivy.design"
}

// Compose compiler 2.1.21 has a bug in IrSourcePrinterVisitor that crashes on
// certain when-expressions in this legacy module. Disable stability reports here
// since this module is being migrated out anyway.
composeCompiler {
    reportsDestination.unset()
    metricsDestination.unset()
}

dependencies {
    implementation(projects.shared.base)
    implementation(projects.shared.ui.core)

    implementation(projects.shared.domain)
}
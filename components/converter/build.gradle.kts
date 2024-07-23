plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(projects.components.generator.iconpack)
    api(projects.components.generator.imagevector)
    api(projects.components.parser)

    // https://plugins.jetbrains.com/docs/intellij/using-kotlin.html#kotlin-standard-library
    compileOnly(libs.kotlin.stdlib)
    compileOnly(libs.kotlinx.coroutines)
}

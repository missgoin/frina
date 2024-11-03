// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

fun String.execute(currentWorkingDir: File = file("./")): String {
    val byteOut = java.io.ByteArrayOutputStream()
    project.exec {
        workingDir = currentWorkingDir
        commandLine = split("\\s".toRegex())
        standardOutput = byteOut
    }
    return String(byteOut.toByteArray()).trim()
}

val gitCommitCount = "git rev-list HEAD --count".execute().toInt()
val gitCommitHash = "git rev-parse --verify --short HEAD".execute()

val minSdkVer by extra(24)
val targetSdkVer by extra(34)
val buildToolsVer by extra("34.0.0")

val appVerName by extra("3.2")
val configVerCode by extra(90)
val serviceVerCode by extra(98)
val minBackupVerCode by extra(65)

val androidSourceCompatibility = JavaVersion.VERSION_18
val androidTargetCompatibility = JavaVersion.VERSION_18

val localProperties = Properties()
localProperties.load(file("local.properties").inputStream())
val officialBuild by extra(localProperties.getProperty("officialBuild", "false") == "true")

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
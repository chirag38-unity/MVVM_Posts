import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // KSP
    alias(libs.plugins.ksp)
    // Serialization
    id ("kotlinx-serialization")
    id("kotlin-parcelize")
}

android {

    fun loadLocalProperties(rootDir: File): Properties {
        val propertiesFile = File(rootDir, "local.properties")
        val properties = Properties()
        if (propertiesFile.exists()) {
            propertiesFile.inputStream().use { inputStream ->
                properties.load(inputStream)
            }
        }
        return properties
    }

    // Load properties
    val localProperties = loadLocalProperties(rootDir)

    // Signing Config
    val keystorePath: String = localProperties.getProperty("keystorePath")
    val keystorePass: String = localProperties.getProperty("keystorePass")
    val keyID: String = localProperties.getProperty("keyID")
    val keyPass: String = localProperties.getProperty("keyPass")

    signingConfigs {
        create("release") {
            storeFile = file(keystorePath)
            storePassword = keystorePass
            keyAlias = keyID
            keyPassword = keyPass
        }
    }
    namespace = "com.cr.mvvmposts"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cr.mvvmposts"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    applicationVariants.all {
        addJavaSourceFoldersToModel(
            File(layout.buildDirectory.asFile.get(), "generated/ksp/$name/kotlin")
        )
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // KotlinX Serialization -----------------------------------------------------------------------
    implementation(libs.kotlinx.serialization.json)

    // Google & Material3 --------------------------------------------------------------------------
    implementation(libs.androidx.splashscreen)
    implementation(libs.material.icons)
    implementation(libs.androidx.ui.text.google.fonts)

    // UI ------------------------------------------------------------------------------------------
    implementation(libs.compose.shimmer)
    implementation(libs.messageBar)

    // Logging -------------------------------------------------------------------------------------
    implementation(libs.napier)

    // Compose Destinations ------------------------------------------------------------------------
    implementation(libs.raamcosta.compose.destinations.core)
    ksp(libs.raamcosta.compose.destinations.ksp)

    // KOIN ----------------------------------------------------------------------------------------
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    // ROOM ----------------------------------------------------------------------------------------
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

}
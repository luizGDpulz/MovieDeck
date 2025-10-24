import java.util.Properties
import java.io.FileInputStream

val localProperties = Properties().apply {
    val localPropsFile = rootProject.file("local.properties")
    if (localPropsFile.exists()) {
        load(FileInputStream(localPropsFile))
    }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.pulz.moviedeck"
    compileSdk = 36

    buildFeatures {
        buildConfig = true
        compose = true
    }

    defaultConfig {
        applicationId = "com.pulz.moviedeck"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Api Key, busca do local.properties (Deve ser Inserido manualmente)
        buildConfigField(
            "String",
            "OMDB_API_KEY",
            "\"${localProperties.getProperty("OMDB_API_KEY", "")}\""
        )

        // URL base da OMDb API
        buildConfigField(
            "String",
            "OMDB_BASE_URL",
            "\"https://www.omdbapi.com/\""
        )

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}


dependencies {

    // Core Android
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.3")

    // Compose UI
    implementation("androidx.compose.ui:ui:1.7.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.3")

    // Material 3
    implementation("androidx.compose.material3:material3:1.3.0")

    // Material Icons
    implementation("androidx.compose.material:material-icons-core:1.4.3")
    implementation("androidx.compose.material:material-icons-extended:1.4.3")

    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.2")

    // Retrofit + Gson
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Coil Compose (imagens)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // Volley (se ainda precisar)
    implementation("com.android.volley:volley:1.2.1")

    // Testes
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.3")
}
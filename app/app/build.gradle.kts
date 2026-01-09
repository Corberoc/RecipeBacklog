plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10" // ou ta version Kotlin

}

android {
    namespace = "com.example.recipebacklog"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.recipebacklog"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Fournit des extensions Kotlin pour les API principales d'Android.
    implementation(libs.androidx.core.ktx)
    // Fournit des fonctionnalités de cycle de vie pour les composants conscients du cycle de vie.
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Intègre Jetpack Compose avec ComponentActivity.
    implementation(libs.androidx.activity.compose)
    // Gère les versions des bibliothèques Jetpack Compose (Bill of Materials).
    implementation(platform(libs.androidx.compose.bom))
    // Fournit les composants de base de l'interface utilisateur de Compose.
    implementation(libs.androidx.compose.ui)
    // Fournit des API graphiques de bas niveau pour Compose.
    implementation(libs.androidx.compose.ui.graphics)
    // Permet de prévisualiser les composants Compose dans l'EDI.
    implementation(libs.androidx.compose.ui.tooling.preview)
    // Fournit les composants de Material Design 3 pour Compose.
    implementation(libs.androidx.compose.material3)
    // Fournit la navigation dans les applications Jetpack Compose.
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Framework de test unitaire pour Java.
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0") // _____Clément_____
    testImplementation(libs.junit)
    // Implémentation Android de JUnit pour les tests d'instrumentation.
    androidTestImplementation(libs.androidx.junit)
    // Framework de test d'interface utilisateur pour les tests d'instrumentation.
    androidTestImplementation(libs.androidx.espresso.core)
    // Gère les versions des bibliothèques de test de Compose.
    androidTestImplementation(platform(libs.androidx.compose.bom))
    // Fournit des API de test pour Jetpack Compose avec JUnit4.
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    // Fournit des outils de développement pour Compose en mode débogage.
    debugImplementation(libs.androidx.compose.ui.tooling)
    // Fournit le manifeste pour les tests Compose.
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-cio:2.3.7")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("androidx.navigation:navigation-compose:2.7.7")


    // Gère les versions des bibliothèques Firebase.
    implementation(platform("com.google.firebase:firebase-bom:34.0.0"))
    // Fournit l'authentification Firebase.
    implementation("com.google.firebase:firebase-auth")

    implementation("androidx.navigation:navigation-compose:2.8.0")
    // Fournit une meilleure intégration des coroutines avec les services Google Play.
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.9.0")
}
// Fichier de construction de premier niveau où vous pouvez ajouter des options de configuration communes à tous les sous-projets/modules.
plugins {
    // Plugin pour la construction d'applications Android.
    alias(libs.plugins.android.application) apply false
    // Plugin pour la prise en charge du langage Kotlin dans les projets Android.
    alias(libs.plugins.kotlin.android) apply false
    // Plugin pour la prise en charge de Jetpack Compose.
    alias(libs.plugins.kotlin.compose) apply false

    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.firebase.crashlytics") version "3.0.2" apply false

}
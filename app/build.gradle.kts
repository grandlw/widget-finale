plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }

android {
    namespace = "com.grandl.rankwidget"
    compileSdk = 35
    defaultConfig { applicationId = "com.grandl.rankwidget"; minSdk = 26; targetSdk = 35; versionCode = 1; versionName = "1.0" }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.work:work-runtime-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("org.jsoup:jsoup:1.18.3")
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //kapt
    kotlin("kapt")

    //nav component
    id("androidx.navigation.safeargs.kotlin")

    //hilt
    id("com.google.dagger.hilt.android")

    //parcelable
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.mealplan"
    compileSdk = 34
    viewBinding.enable = true
    buildFeatures.buildConfig = true
    defaultConfig {
        applicationId = "com.example.mealplan"
        minSdk = 21
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
   //extensions
    val utilsVersion = "2.2.10"
    implementation ("com.github.FunkyMuse.KAHelpers:viewbinding:$utilsVersion")
    implementation ("com.github.FunkyMuse.KAHelpers:recyclerview:$utilsVersion")
    implementation ("com.github.FunkyMuse.KAHelpers:kotlinextensions:$utilsVersion")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    //OkHTTP client
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    //coil
    implementation("io.coil-kt:coil:2.3.0")
    val room_version = "2.5.2"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    //navigation component
    val navVersion = "2.6.0"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    //hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    //dataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //Calligraphy
    implementation("io.github.inflationx:calligraphy3:3.1.1")
    implementation("io.github.inflationx:viewpump:2.0.3")
    val lifecycle_version = "2.6.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    //Other
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.todkars:shimmer-recyclerview:0.4.1")
    implementation("com.airbnb.android:lottie:5.2.0")
    implementation("com.github.MrNouri:DynamicSizes:1.0")
    implementation ("com.bitvale:lavafab:1.0.1")
    implementation ("com.tbuonomo:dotsindicator:4.2")
    implementation("com.github.skydoves:balloon:1.5.4")
    implementation ("com.github.skydoves:expandablelayout:1.0.7")
    implementation ("com.github.skydoves:progressview:1.1.3")
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")
    implementation ("com.github.skydoves:androidveil:1.1.3")
    implementation ("com.paulrybitskyi.valuepicker:valuepicker:1.0.3")
    implementation ("kr.co.prnd:readmore-textview:1.0.0")



}
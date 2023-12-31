import com.sun.xml.fastinfoset.sax.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.dicodingstoryapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dicodingstoryapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GMP_KEY", "\"AIzaSyDHbdJM_hEJU-HDnarjaMsWEVz21Qom84s\"")
    }
buildFeatures{
    viewBinding = true
    buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    /*Retrofit*/
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    /*Logging*/
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    /*lifescycleScope*/
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    implementation("androidx.activity:activity-ktx:1.8.0")



    /*DataStore*/
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    /*Coroutines*/
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")


    /*Room*/
    val room_version = "2.5.0"

    //noinspection GradleDependency,GradleDependency
    implementation("androidx.room:room-runtime:$room_version")
    //noinspection GradleDependency
    annotationProcessor("androidx.room:room-compiler:$room_version")


    /*Glide*/
    implementation("com.github.bumptech.glide:glide:4.16.0")


    /*viewmodel*/

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")


    /*FAB Expand*/
    implementation("com.nambimobile.widgets:expandable-fab:1.2.1")

    /*ZoomRecylerView*/
    implementation("com.github.Spikeysanju:ZoomRecylerLayout:1.0")

/*Paging*/
    implementation("androidx.paging:paging-runtime-ktx:3.1.0")

    /*Testing*/

    testImplementation("androidx.arch.core:core-testing:2.1.0") // InstantTaskExecutorRule
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1") //TestDispatcher
    testImplementation("org.mockito:mockito-core:3.12.4")
    testImplementation("org.mockito:mockito-inline:3.12.4")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navigation.safeargs.plugin)
}

android {
    namespace = "by.beltelecom.innowise"
    compileSdk = 34

    defaultConfig {
        applicationId = "by.beltelecom.innowise"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.rxjava2)
    implementation(libs.rxandroid)

    implementation(libs.retrofit2.runtime)
    implementation(libs.retrofit2.scalar.converter)
    implementation(libs.retrofit2.serialization.converter)
    implementation(libs.retrofit2.adapter.rxjava2)

    implementation(libs.okhhtp3)
    implementation(libs.okhttp3.loggging.interceptor)

    implementation(libs.paging)
    implementation(libs.paging.rxjava2)

    implementation(libs.glide)
    implementation(libs.glide.recyclerview.integration)
    implementation(libs.glide.annotations)
    implementation(libs.glide.okhttp.integration)
    ksp(libs.glide.ksp)

    implementation(libs.hilt.android)
    ksp(libs.dagger.complier)
    ksp(libs.hilt.complier)

    implementation(libs.splash)

    implementation(libs.room.runtime)
    implementation(libs.room.rxjava2)
    ksp(libs.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

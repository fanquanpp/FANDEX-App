plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.fandex.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.fandex.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 13
        versionName = "2.0.0-beta"
    }

    signingConfigs {
        create("release") {
            storeFile = file("../fandex-release.jks")
            storePassword = "fandex2026"
            keyAlias = "fandex"
            keyPassword = "fandex2026"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    defaultConfig {
        resourceConfigurations += listOf("zh-rCN")
    }

    applicationVariants.all {
        val variant = this
        variant.outputs.all {
            val output = this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
            output.outputFileName = "FANDEX-v${variant.versionName}.apk"
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    /* 极致裁剪：排除冗余 META-INF 文件 */
    packaging {
        resources {
            excludes += setOf(
                "META-INF/*.kotlin_module",
                "META-INF/*.version",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "kotlin-tooling-metadata.json",
                "DebugProbesKt.bin"
            )
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
}

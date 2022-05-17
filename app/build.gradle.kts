plugins {
    id("com.android.application")
    id("kotlin-android")
}

fun selectAppSignVersion(config: com.android.build.api.dsl.ApkSigningConfig) {
    config.enableV1Signing = MakeConfig.appMinSDK < 24
    config.enableV2Signing = MakeConfig.appMinSDK < 28
    config.enableV3Signing = MakeConfig.appMinSDK >= 28
    config.enableV4Signing = false
}

android {
    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "META-INF/notice.txt",
                "META-INF/license.txt",
                "META-INF/LICENSE",
                "META-INF/NOTICE",
                "META-INF/DEPENDENCIES",
                "META-INF/*.version"
            )
        )
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    compileSdk = MakeConfig.appCompileSDK
    buildToolsVersion = MakeConfig.appBuildTools
    ndkVersion = MakeConfig.appNdk

    defaultConfig {
        minSdk = MakeConfig.appMinSDK
        targetSdk = MakeConfig.appTargetSDK
        versionCode = MakeConfig.appVersionCode
        versionName = MakeConfig.appVersionName
        buildConfigField("boolean", "MANAGE_SCOPED_STORAGE", "true")

        applicationId = "dev.ragnarok.filegallery"

        ndk {
            abiFilters.addAll(listOf("arm64-v8a", "armeabi-v7a", "x86_64"))
        }
    }

    lint {
        abortOnError = true
        checkReleaseBuilds = true
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.addAll(listOf("-Xmaxwarns", "1000", "-Xmaxerrs", "1000"))
    }

    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = MakeConfig.appMinSDK < 26

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        encoding = "utf-8"
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-opt-in=kotlin.contracts.ExperimentalContracts")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
        }

        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
}

dependencies {
    implementation(fileTree("include" to "*.aar", "dir" to "libs"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${MakeConfig.kotlin_version}")
    implementation("org.jetbrains.kotlin:kotlin-parcelize-runtime:${MakeConfig.kotlin_version}")
    implementation("org.jetbrains.kotlin:kotlin-android-extensions-runtime:${MakeConfig.kotlin_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${MakeConfig.kotlin_coroutines}")
    compileOnly("org.jetbrains.kotlin:kotlin-annotations-jvm:${MakeConfig.kotlin_version}")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
    implementation("androidx.core:core-ktx:${MakeConfig.coreVersion}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${MakeConfig.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-common:${MakeConfig.lifecycleVersion}")
    implementation("androidx.annotation:annotation:${MakeConfig.annotationVersion}")
    implementation("androidx.recyclerview:recyclerview:${MakeConfig.recyclerviewVersion}")
    implementation("androidx.viewpager2:viewpager2:${MakeConfig.viewpager2Version}")
    implementation("androidx.vectordrawable:vectordrawable:${MakeConfig.vectordrawableVersion}")
    implementation("androidx.appcompat:appcompat:${MakeConfig.appcompatVersion}")
    implementation("androidx.customview:customview:1.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    implementation("androidx.browser:browser:1.4.0")
    implementation("androidx.exifinterface:exifinterface:${MakeConfig.exifinterfaceVersion}")
    implementation("io.reactivex.rxjava3:rxjava:${MakeConfig.rxJavaVersion}")
    implementation("io.reactivex.rxjava3:rxandroid:${MakeConfig.rxAndroidVersion}")
    //implementation(project("path" to ":libnative"))
    implementation(project("path" to ":picasso"))
    implementation(project("path" to ":material"))
    implementation(project("path" to ":gson"))
    implementation(project("path" to ":preference"))
    implementation(project("path" to ":retrofit"))
    implementation("com.squareup.okhttp3:okhttp:${MakeConfig.okhttpLibraryVersion}")
    implementation("com.squareup.okhttp3:logging-interceptor:${MakeConfig.okhttpLibraryVersion}")
    implementation("com.squareup.okio:okio:${MakeConfig.okioVersion}")
    implementation("com.google.android.exoplayer:exoplayer-core:${MakeConfig.exoLibraryVersion}")
    implementation("androidx.constraintlayout:constraintlayout:${MakeConfig.constraintlayoutVersion}")
    implementation("androidx.media:media:1.6.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:${MakeConfig.coordinatorlayoutVersion}")
    implementation("androidx.activity:activity-ktx:${MakeConfig.activityVersion}")
    implementation("androidx.fragment:fragment-ktx:${MakeConfig.fragmentVersion}")
    implementation("androidx.work:work-runtime-ktx:2.8.0-alpha02")
    implementation("com.google.guava:guava:${MakeConfig.guavaVersion}")
    implementation("androidx.drawerlayout:drawerlayout:${MakeConfig.drawerlayoutVersion}")
    implementation("androidx.loader:loader:1.1.0")
    implementation("androidx.collection:collection-ktx:${MakeConfig.collectionVersion}")
    implementation("androidx.savedstate:savedstate-ktx:${MakeConfig.savedStateVersion}")
}

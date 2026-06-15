# TradePulse: Master Re-generation Prompt

This file contains the complete instructions and source code to recreate both the **TradePulse Client** (Kotlin Multiplatform Mobile App) and **TradePulse Backend** (Ktor API Server) from scratch, preserving every single file, logic, dependency, and configuration exactly as implemented.

To recreate the project, copy the instructions and file blocks below and feed them to an AI developer agent or assistant in a clean workspace.

***

### START OF MASTER PROMPT

I want to build a B2B trading terminal application called **TradePulse** from scratch. It consists of two projects:
1. **Client**: A Kotlin Multiplatform (KMP) mobile app for Android and iOS using Compose Multiplatform, SQLDelight local caching, and Koin for Dependency Injection.
2. **Backend**: A Ktor API server (JVM) using Exposed ORM, Neon PostgreSQL cloud database (with SQLite fallback for local development), and Base64 persistent image storage.

Please generate the entire codebase step-by-step using the exact folder structures, file paths, and file contents provided below. Do not omit any files or details, and preserve the package namespaces:
- **Client Base Package**: `com.yeshuwahane.tradepulse`
- **Backend Base Package**: `com.tradepulse`

---

## Part 1: KMP Mobile Client

Create the following files for the Client project. Each file path and its full content is specified below.

### File: `androidApp/build.gradle.kts`
```kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}
dependencies {
    implementation(projects.shared)

    implementation(libs.androidx.activity.compose)

    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)
}

android {
    namespace = "com.yeshuwahane.tradepulse"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.yeshuwahane.tradepulse"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
```

### File: `androidApp/src/main/AndroidManifest.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:usesCleartextTraffic="true"
        android:theme="@android:style/Theme.Material.Light.NoActionBar">
        <activity
            android:exported="true"
            android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

### File: `androidApp/src/main/kotlin/com/yeshuwahane/tradepulse/MainActivity.kt`
```kotlin
package com.yeshuwahane.tradepulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

import com.yeshuwahane.tradepulse.data.db.DatabaseDriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        DatabaseDriverFactory.appContext = applicationContext
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
```

### File: `androidApp/src/main/res/drawable-v24/ic_launcher_foreground.xml`
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:aapt="http://schemas.android.com/aapt"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path android:pathData="M31,63.928c0,0 6.4,-11 12.1,-13.1c7.2,-2.6 26,-1.4 26,-1.4l38.1,38.1L107,108.928l-32,-1L31,63.928z">
        <aapt:attr name="android:fillColor">
            <gradient
                android:endX="85.84757"
                android:endY="92.4963"
                android:startX="42.9492"
                android:startY="49.59793"
                android:type="linear">
                <item
                    android:color="#44000000"
                    android:offset="0.0" />
                <item
                    android:color="#00000000"
                    android:offset="1.0" />
            </gradient>
        </aapt:attr>
    </path>
    <path
        android:fillColor="#FFFFFF"
        android:fillType="nonZero"
        android:pathData="M65.3,45.828l3.8,-6.6c0.2,-0.4 0.1,-0.9 -0.3,-1.1c-0.4,-0.2 -0.9,-0.1 -1.1,0.3l-3.9,6.7c-6.3,-2.8 -13.4,-2.8 -19.7,0l-3.9,-6.7c-0.2,-0.4 -0.7,-0.5 -1.1,-0.3C38.8,38.328 38.7,38.828 38.9,39.228l3.8,6.6C36.2,49.428 31.7,56.028 31,63.928h46C76.3,56.028 71.8,49.428 65.3,45.828zM43.4,57.328c-0.8,0 -1.5,-0.5 -1.8,-1.2c-0.3,-0.7 -0.1,-1.5 0.4,-2.1c0.5,-0.5 1.4,-0.7 2.1,-0.4c0.7,0.3 1.2,1 1.2,1.8C45.3,56.528 44.5,57.328 43.4,57.328L43.4,57.328zM64.6,57.328c-0.8,0 -1.5,-0.5 -1.8,-1.2s-0.1,-1.5 0.4,-2.1c0.5,-0.5 1.4,-0.7 2.1,-0.4c0.7,0.3 1.2,1 1.2,1.8C66.5,56.528 65.6,57.328 64.6,57.328L64.6,57.328z"
        android:strokeWidth="1"
        android:strokeColor="#00000000" />
</vector>
```

### File: `androidApp/src/main/res/drawable/ic_launcher_background.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path
        android:fillColor="#3DDC84"
        android:pathData="M0,0h108v108h-108z" />
    <path
        android:fillColor="#00000000"
        android:pathData="M9,0L9,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,0L19,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M29,0L29,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M39,0L39,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M49,0L49,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M59,0L59,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M69,0L69,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M79,0L79,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M89,0L89,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M99,0L99,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,9L108,9"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,19L108,19"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,29L108,29"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,39L108,39"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,49L108,49"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,59L108,59"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,69L108,69"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,79L108,79"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,89L108,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,99L108,99"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,29L89,29"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,39L89,39"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,49L89,49"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,59L89,59"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,69L89,69"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,79L89,79"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M29,19L29,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M39,19L39,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M49,19L49,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M59,19L59,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M69,19L69,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M79,19L79,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
</vector>
```

### File: `androidApp/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
```

### File: `androidApp/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
```

### File: `androidApp/src/main/res/values/strings.xml`
```xml
<resources>
    <string name="app_name">TradePulse</string>
</resources>
```

### File: `build.gradle.kts`
```kotlin
plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}
```

### File: `gradle.properties`
```properties
#Kotlin
kotlin.code.style=official
kotlin.daemon.jvmargs=-Xmx3072M

#Gradle
org.gradle.jvmargs=-Xmx4096M -Dfile.encoding=UTF-8
org.gradle.configuration-cache=true
org.gradle.caching=true

#Android
android.nonTransitiveRClass=true
android.useAndroidX=true
```

### File: `gradle/gradle-daemon-jvm.properties`
```properties
#This file is generated by updateDaemonJvm
toolchainUrl.FREE_BSD.AARCH64=https\://api.foojay.io/disco/v3.0/ids/b62178ff26b34365c61e54dea2180e32/redirect
toolchainUrl.FREE_BSD.X86_64=https\://api.foojay.io/disco/v3.0/ids/f2dede3f3c566068b401dc14a9646d39/redirect
toolchainUrl.LINUX.AARCH64=https\://api.foojay.io/disco/v3.0/ids/b62178ff26b34365c61e54dea2180e32/redirect
toolchainUrl.LINUX.X86_64=https\://api.foojay.io/disco/v3.0/ids/f2dede3f3c566068b401dc14a9646d39/redirect
toolchainUrl.MAC_OS.AARCH64=https\://api.foojay.io/disco/v3.0/ids/9aafe8bc391c4bbca3e440130e15608b/redirect
toolchainUrl.MAC_OS.X86_64=https\://api.foojay.io/disco/v3.0/ids/109553caae279a667336ea8850b50c92/redirect
toolchainUrl.UNIX.AARCH64=https\://api.foojay.io/disco/v3.0/ids/b62178ff26b34365c61e54dea2180e32/redirect
toolchainUrl.UNIX.X86_64=https\://api.foojay.io/disco/v3.0/ids/f2dede3f3c566068b401dc14a9646d39/redirect
toolchainUrl.WINDOWS.X86_64=https\://api.foojay.io/disco/v3.0/ids/9eb5d45802b65696ed3ce0f14bb1e4ff/redirect
toolchainVendor=AMAZON
toolchainVersion=21
```

### File: `gradle/libs.versions.toml`
```toml
[versions]
agp = "9.0.1"
android-compileSdk = "36"
android-minSdk = "24"
android-targetSdk = "36"
androidx-activity = "1.13.0"
androidx-appcompat = "1.7.1"
androidx-core = "1.19.0"
androidx-espresso = "3.7.0"
androidx-lifecycle = "2.11.0-beta01"
androidx-testExt = "1.3.0"
composeMultiplatform = "1.11.1"
junit = "4.13.2"
kotlin = "2.4.0"
material3 = "1.11.0-alpha07"

sqlDelight = "2.3.2"
ktor = "3.0.3"
kotlinx-coroutines = "1.10.1"
kotlinx-datetime = "0.6.1"
koin = "3.5.6"
koinCompose = "1.1.5"
napier = "2.7.1"

multiplatformSettingsNoArg = "1.3.0"

[libraries]

multiplatform-settings-no-arg = { module = "com.russhwolf:multiplatform-settings-no-arg", version.ref = "multiplatformSettingsNoArg" }


napier = { module = "io.github.aakira:napier", version.ref = "napier" }
kotlin-test = { module = "org.jetbrains.kotlin:kotlin-test", version.ref = "kotlin" }
kotlin-testJunit = { module = "org.jetbrains.kotlin:kotlin-test-junit", version.ref = "kotlin" }
junit = { module = "junit:junit", version.ref = "junit" }
androidx-core-ktx = { module = "androidx.core:core-ktx", version.ref = "androidx-core" }
androidx-testExt-junit = { module = "androidx.test.ext:junit", version.ref = "androidx-testExt" }
androidx-espresso-core = { module = "androidx.test.espresso:espresso-core", version.ref = "androidx-espresso" }
androidx-appcompat = { module = "androidx.appcompat:appcompat", version.ref = "androidx-appcompat" }
androidx-activity-compose = { module = "androidx.activity:activity-compose", version.ref = "androidx-activity" }
compose-uiTooling = { module = "org.jetbrains.compose.ui:ui-tooling", version.ref = "composeMultiplatform" }
androidx-lifecycle-viewmodelCompose = { module = "org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose", version.ref = "androidx-lifecycle" }
androidx-lifecycle-runtimeCompose = { module = "org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose", version.ref = "androidx-lifecycle" }
compose-runtime = { module = "org.jetbrains.compose.runtime:runtime", version.ref = "composeMultiplatform" }
compose-foundation = { module = "org.jetbrains.compose.foundation:foundation", version.ref = "composeMultiplatform" }
compose-material3 = { module = "org.jetbrains.compose.material3:material3", version.ref = "material3" }
compose-ui = { module = "org.jetbrains.compose.ui:ui", version.ref = "composeMultiplatform" }
compose-components-resources = { module = "org.jetbrains.compose.components:components-resources", version.ref = "composeMultiplatform" }
compose-uiToolingPreview = { module = "org.jetbrains.compose.ui:ui-tooling-preview", version.ref = "composeMultiplatform" }

ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-android = { module = "io.ktor:ktor-client-android", version.ref = "ktor" }
ktor-client-darwin = { module = "io.ktor:ktor-client-darwin", version.ref = "ktor" }
ktor-client-content-negotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
ktor-client-logging = { module = "io.ktor:ktor-client-logging", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }

kotlinx-coroutines-core = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "kotlinx-coroutines" }
kotlinx-datetime = { module = "org.jetbrains.kotlinx:kotlinx-datetime", version.ref = "kotlinx-datetime" }

koin-core = { module = "io.insert-koin:koin-core", version.ref = "koin" }
koin-compose = { module = "io.insert-koin:koin-compose", version.ref = "koinCompose" }

android-driver = { module = "app.cash.sqldelight:android-driver", version.ref = "sqlDelight" }
native-driver = { module = "app.cash.sqldelight:native-driver", version.ref = "sqlDelight" }
runtime = { module = "app.cash.sqldelight:runtime", version.ref = "sqlDelight" }

[plugins]
androidApplication = { id = "com.android.application", version.ref = "agp" }
androidMultiplatformLibrary = { id = "com.android.kotlin.multiplatform.library", version.ref = "agp" }
composeMultiplatform = { id = "org.jetbrains.compose", version.ref = "composeMultiplatform" }
composeCompiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
kotlinMultiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }

kotlinxSerialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
sqldelight = { id = "app.cash.sqldelight", version.ref = "sqlDelight" }
```

### File: `gradle/wrapper/gradle-wrapper.properties`
```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionSha256Sum=a17ddd85a26b6a7f5ddb71ff8b05fc5104c0202c6e64782429790c933686c806
distributionUrl=https\://services.gradle.org/distributions/gradle-9.1.0-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

### File: `iosApp/Configuration/Config.xcconfig`
```properties
TEAM_ID=

PRODUCT_NAME=TradePulse
PRODUCT_BUNDLE_IDENTIFIER=com.yeshuwahane.tradepulse.TradePulse$(TEAM_ID)

CURRENT_PROJECT_VERSION=1
MARKETING_VERSION=1.0

OTHER_LDFLAGS = $(inherited) -lsqlite3
```

### File: `iosApp/iosApp/ContentView.swift`
```swift
import UIKit
import SwiftUI
import Shared

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Self.Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Self.Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}
```

### File: `iosApp/iosApp/Info.plist`
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
	<key>CADisableMinimumFrameDurationOnPhone</key>
	<true/>
</dict>
</plist>
```

### File: `iosApp/iosApp/iOSApp.swift`
```swift
import SwiftUI

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

### File: `settings.gradle.kts`
```kotlin
rootProject.name = "TradePulse"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":androidApp")
include(":shared")
```

### File: `shared/build.gradle.kts`
```kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    androidLibrary {
       namespace = "com.yeshuwahane.tradepulse.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
    }


    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.android)
            implementation(libs.android.driver)
        }
        commonMain.dependencies {
            val voyagerVersion = "1.1.0-beta02"
//            implementation(libs.multiplatform.settings.no.arg)

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(compose.material)
            implementation(compose.materialIconsExtended)
            implementation(libs.napier)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.runtime)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            implementation("cafe.adriel.voyager:voyager-navigator:${voyagerVersion}")
// Screen Model
            implementation("cafe.adriel.voyager:voyager-screenmodel:${voyagerVersion}")
// BottomSheetNavigator
            implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:${voyagerVersion}")
// TabNavigator
            implementation("cafe.adriel.voyager:voyager-tab-navigator:${voyagerVersion}")
// Transitions
            implementation("cafe.adriel.voyager:voyager-transitions:${voyagerVersion}")
// Koin integration
            implementation("cafe.adriel.voyager:voyager-koin:${voyagerVersion}")
            implementation(libs.multiplatform.settings.no.arg)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.native.driver)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

sqldelight {
    databases {
        create("TradePulseDb") {
            packageName.set("com.yeshuwahane.tradepulse.data.db")
        }
    }
}
```

### File: `shared/src/androidMain/kotlin/com/yeshuwahane/tradepulse/Platform.android.kt`
```kotlin
package com.yeshuwahane.tradepulse

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun getCurrentTimeMillis(): Long = java.lang.System.currentTimeMillis()
```

### File: `shared/src/androidMain/kotlin/com/yeshuwahane/tradepulse/data/db/PlatformDatabaseDriver.android.kt`
```kotlin
package com.yeshuwahane.tradepulse.data.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val context = appContext ?: throw IllegalStateException("Android Context not initialized for DatabaseDriverFactory")
        return AndroidSqliteDriver(TradePulseDb.Schema, context, "tradepulse.db")
    }

    companion object {
        var appContext: Context? = null
    }
}
```

### File: `shared/src/androidMain/kotlin/com/yeshuwahane/tradepulse/presentation/components/ImagePicker.android.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.components

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.InputStream

@Composable
actual fun ImagePickerButton(
    onImagesSelected: (List<Pair<String, ByteArray>>) -> Unit,
    maxSelectionLimit: Int,
    modifier: Modifier
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = maxSelectionLimit)
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            val selected = uris.mapNotNull { uri ->
                try {
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                    val bytes = inputStream?.readBytes()
                    if (bytes != null) {
                        val name = uri.lastPathSegment?.substringAfterLast('/') ?: "gallery_image.png"
                        name to bytes
                    } else null
                } catch (e: Exception) {
                    null
                }
            }
            onImagesSelected(selected)
        }
    }

    Button(
        onClick = {
            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        },
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add Images")
        Spacer(modifier = Modifier.width(6.dp))
        Text("Add Images from Gallery")
    }
}

actual fun byteArrayToImageBitmap(bytes: ByteArray): ImageBitmap {
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    return bitmap.asImageBitmap()
}
```

### File: `shared/src/commonMain/composeResources/drawable/compose-multiplatform.xml`
```xml
<vector
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:aapt="http://schemas.android.com/aapt"
    android:width="450dp"
    android:height="450dp"
    android:viewportWidth="64"
    android:viewportHeight="64">
  <path
      android:pathData="M56.25,18V46L32,60 7.75,46V18L32,4Z"
      android:fillColor="#6075f2"/>
  <path
      android:pathData="m41.5,26.5v11L32,43V60L56.25,46V18Z"
      android:fillColor="#6b57ff"/>
  <path
      android:pathData="m32,43 l-9.5,-5.5v-11L7.75,18V46L32,60Z">
    <aapt:attr name="android:fillColor">
      <gradient
          android:centerX="23.131"
          android:centerY="18.441"
          android:gradientRadius="42.132"
          android:type="radial">
        <item android:offset="0" android:color="#FF5383EC"/>
        <item android:offset="0.867" android:color="#FF7F52FF"/>
      </gradient>
    </aapt:attr>
  </path>
  <path
      android:pathData="M22.5,26.5 L32,21 41.5,26.5 56.25,18 32,4 7.75,18Z">
    <aapt:attr name="android:fillColor">
      <gradient
          android:startX="44.172"
          android:startY="4.377"
          android:endX="17.973"
          android:endY="34.035"
          android:type="linear">
        <item android:offset="0" android:color="#FF33C3FF"/>
        <item android:offset="0.878" android:color="#FF5383EC"/>
      </gradient>
    </aapt:attr>
  </path>
  <path
      android:pathData="m32,21 l9.526,5.5v11L32,43 22.474,37.5v-11z"
      android:fillColor="#000000"/>
</vector>
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/App.kt`
```kotlin
package com.yeshuwahane.tradepulse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.yeshuwahane.tradepulse.di.appModule
import com.yeshuwahane.tradepulse.domain.model.UserRole
import com.yeshuwahane.tradepulse.domain.usecase.GetSettingsUserUseCase
import com.yeshuwahane.tradepulse.presentation.admin.AdminOverviewScreen
import com.yeshuwahane.tradepulse.presentation.login.LoginScreen
import com.yeshuwahane.tradepulse.presentation.marketplace.CustomerMarketplaceScreen
import com.yeshuwahane.tradepulse.presentation.supplier.SupplierDashboardScreen
import com.yeshuwahane.tradepulse.theme.TradePulseTheme
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(appModule())
        }
    ) {
        var startScreen by remember { mutableStateOf<Screen?>(null) }
        val getSettingsUserUseCase = koinInject<GetSettingsUserUseCase>()

        LaunchedEffect(Unit) {
            val user = getSettingsUserUseCase()
            if (user != null) {
                startScreen = when (user.role) {
                    UserRole.CUSTOMER -> CustomerMarketplaceScreen()
                    UserRole.SUPPLIER -> SupplierDashboardScreen()
                    UserRole.ADMIN -> AdminOverviewScreen()
                }
            } else {
                startScreen = LoginScreen()
            }
        }

        TradePulseTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                val screen = startScreen
                if (screen != null) {
                    Navigator(screen)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/Greeting.kt`
```kotlin
package com.yeshuwahane.tradepulse

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return sayHello(platform.name)
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/GreetingUtil.kt`
```kotlin
package com.yeshuwahane.tradepulse

fun sayHello(to: String): String =
    "Hello, $to!"
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/Platform.kt`
```kotlin
package com.yeshuwahane.tradepulse

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getCurrentTimeMillis(): Long
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/data/db/PlatformDatabaseDriver.kt`
```kotlin
package com.yeshuwahane.tradepulse.data.db

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory() {
    fun createDriver(): SqlDriver
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/data/db/RoomDatabase.kt`
```kotlin
package com.yeshuwahane.tradepulse.data.db

import com.yeshuwahane.tradepulse.data.repository.ProductDto

interface ProductDao {
    suspend fun clear()
    suspend fun insertAll(products: List<ProductDto>)
    suspend fun getAll(): List<ProductDto>
}

class ProductDaoImpl(private val database: TradePulseDb) : ProductDao {
    private val productQueries = database.tradePulseDbQueries

    override suspend fun clear() {
        productQueries.clearProducts()
    }

    override suspend fun insertAll(products: List<ProductDto>) {
        products.forEach { p ->
            productQueries.insertProduct(
                p.id, p.title, p.description, p.price, p.imageUrl, p.supplierId,
                if (p.isApproved) 1L else 0L, p.currentHighestBid, p.highestBidderName, p.auctionEndTimeMillis
            )
        }
    }

    override suspend fun getAll(): List<ProductDto> {
        return productQueries.getAllProducts().executeAsList().map {
            ProductDto(
                id = it.id,
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                supplierId = it.supplierId,
                isApproved = it.isApproved != 0L,
                currentHighestBid = it.currentHighestBid,
                highestBidderName = it.highestBidderName,
                auctionEndTimeMillis = it.auctionEndTimeMillis
            )
        }
    }
}

class RoomDatabase(private val database: TradePulseDb) {
    val productDao: ProductDao = ProductDaoImpl(database)
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/data/model/ProductDto.kt`
```kotlin
package com.yeshuwahane.tradepulse.data.model

import com.yeshuwahane.tradepulse.domain.model.Product

data class ProductDto(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val supplierId: String,
    val isApproved: Boolean,
    val currentHighestBid: Double,
    val highestBidderName: String,
    val auctionEndTimeMillis: Long
)

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        price = price,
        imageUrl = imageUrl,
        supplierId = supplierId,
        isApproved = isApproved,
        currentHighestBid = currentHighestBid,
        highestBidderName = highestBidderName,
        auctionEndTimeMillis = auctionEndTimeMillis
    )
}

fun Product.toDto(): ProductDto {
    return ProductDto(
        id = id,
        title = title,
        description = description,
        price = price,
        imageUrl = imageUrl,
        supplierId = supplierId,
        isApproved = isApproved,
        currentHighestBid = currentHighestBid,
        highestBidderName = highestBidderName,
        auctionEndTimeMillis = auctionEndTimeMillis
    )
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/data/model/UserDto.kt`
```kotlin
package com.yeshuwahane.tradepulse.data.model

import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.model.UserRole

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: String
)

fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email,
        password = password,
        role = UserRole.valueOf(role.uppercase())
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        name = name,
        email = email,
        password = password,
        role = role.name
    )
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/data/repository/ProductRepositoryImpl.kt`
```kotlin
package com.yeshuwahane.tradepulse.data.repository

import com.russhwolf.settings.Settings
import com.yeshuwahane.tradepulse.data.db.ProductDao
import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.data.utils.apiCall
import com.yeshuwahane.tradepulse.domain.model.Product
import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.model.UserRole
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ProductDto(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val supplierId: String,
    val isApproved: Boolean,
    val currentHighestBid: Double,
    val highestBidderName: String,
    val auctionEndTimeMillis: Long
)

@Serializable
data class BidRequestDto(val amount: Double, val bidderName: String)

@Serializable
data class UploadProductRequestDto(
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val supplierId: String,
    val isAuction: Boolean,
    val durationHours: Int
)

@Serializable
data class ImageUploadResponseDto(val url: String)

@Serializable
private data class LastUpdatedDto(val lastUpdated: Long)

@Serializable
private data class UserDtoHelper(val id: String, val name: String, val email: String, val password: String, val role: String)

class ProductRepositoryImpl(
    private val httpClient: HttpClient,
    private val dao: ProductDao,
    private val settings: Settings
) : ProductRepository {

    private val json = Json { ignoreUnknownKeys = true }

    private fun getSettingsUserSync(): User? {
        val userJson = settings.getStringOrNull("settings_user") ?: return null
        return try {
            val dto = json.decodeFromString(UserDtoHelper.serializer(), userJson)
            User(dto.id, dto.name, dto.email, dto.password, UserRole.valueOf(dto.role.uppercase()))
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getProducts(forceRefresh: Boolean): DataResource<List<Product>> {
        if (!forceRefresh) {
            val lastUpdatedResource = apiCall<LastUpdatedDto> {
                httpClient.get("/api/products/last-updated")
            }
            if (lastUpdatedResource.isSuccess() && lastUpdatedResource.data != null) {
                val remoteLastUpdated = lastUpdatedResource.data.lastUpdated
                val localLastUpdated = settings.getLong("settings_products_last_updated", 0L)
                if (remoteLastUpdated == localLastUpdated) {
                    val cached = dao.getAll().map { it.toDomain() }
                    if (cached.isNotEmpty()) {
                        return DataResource.success(cached)
                    }
                }
            }
        }

        val resource = apiCall<List<ProductDto>> {
            httpClient.get("/api/products")
        }
        return if (resource.isSuccess() && resource.data != null) {
            val products = resource.data
            dao.clear()
            dao.insertAll(products)

            val lastUpdatedResource = apiCall<LastUpdatedDto> {
                httpClient.get("/api/products/last-updated")
            }
            if (lastUpdatedResource.isSuccess() && lastUpdatedResource.data != null) {
                settings.putLong("settings_products_last_updated", lastUpdatedResource.data.lastUpdated)
            }

            DataResource.success(products.map { it.toDomain() })
        } else {
            val cached = dao.getAll().map { it.toDomain() }
            DataResource.error(resource.error, cached)
        }
    }

    override suspend fun getProductById(id: String): DataResource<Product> {
        val resource = apiCall<ProductDto> {
            httpClient.get("/api/products/$id")
        }
        return if (resource.isSuccess() && resource.data != null) {
            DataResource.success(resource.data.toDomain())
        } else {
            val cached = dao.getAll().firstOrNull { it.id == id }?.toDomain()
            if (cached != null) {
                DataResource.success(cached)
            } else {
                DataResource.error(resource.error, null)
            }
        }
    }

    override suspend fun placeBid(productId: String, amount: Double, bidderName: String): DataResource<String> {
        val resource = apiCall<String> {
            httpClient.post("/api/products/$productId/bid") {
                contentType(ContentType.Application.Json)
                setBody(BidRequestDto(amount, bidderName))
            }
        }
        if (resource.isSuccess()) {
            val cachedProducts = dao.getAll()
            val updated = cachedProducts.map {
                if (it.id == productId) {
                    it.copy(currentHighestBid = amount, highestBidderName = bidderName)
                } else {
                    it
                }
            }
            dao.clear()
            dao.insertAll(updated)
        }
        return resource
    }

    override suspend fun addProduct(
        title: String,
        description: String,
        price: Double,
        category: String,
        supplierId: String,
        isAuction: Boolean,
        durationHours: Int
    ): DataResource<Product> {
        val resource = apiCall<ProductDto> {
            httpClient.post("/api/products") {
                contentType(ContentType.Application.Json)
                setBody(UploadProductRequestDto(title, description, price, category, supplierId, isAuction, durationHours))
            }
        }
        return if (resource.isSuccess() && resource.data != null) {
            val p = resource.data
            val currentList = dao.getAll().toMutableList()
            currentList.add(p)
            dao.clear()
            dao.insertAll(currentList)
            DataResource.success(p.toDomain())
        } else {
            DataResource.error(resource.error, null)
        }
    }

    override suspend fun approveProduct(productId: String): DataResource<String> {
        val user = getSettingsUserSync()
        return apiCall<String> {
            httpClient.post("/api/products/$productId/approve") {
                header("X-User-Id", user?.id ?: "")
            }
        }
    }

    override suspend fun rejectProduct(productId: String): DataResource<String> {
        val user = getSettingsUserSync()
        return apiCall<String> {
            httpClient.post("/api/products/$productId/reject") {
                header("X-User-Id", user?.id ?: "")
            }
        }
    }

    override suspend fun uploadProductImage(imageBytes: ByteArray): DataResource<String> {
        val resource = apiCall<ImageUploadResponseDto> {
            httpClient.post("/api/products/upload-image") {
                setBody(MultiPartFormDataContent(
                    formData {
                        append("image", imageBytes, Headers.build {
                            append(HttpHeaders.ContentType, "image/png")
                            append(HttpHeaders.ContentDisposition, "filename=upload.png")
                        })
                    }
                ))
            }
        }
        return if (resource.isSuccess() && resource.data != null) {
            DataResource.success(resource.data.url)
        } else {
            DataResource.error(resource.error, null)
        }
    }

    override suspend fun updateProduct(
        id: String,
        title: String,
        description: String,
        price: Double,
        category: String,
        supplierId: String,
        isAuction: Boolean,
        durationHours: Int
    ): DataResource<String> {
        val resource = apiCall<String> {
            httpClient.post("/api/products/$id/update") {
                contentType(ContentType.Application.Json)
                setBody(UploadProductRequestDto(title, description, price, category, supplierId, isAuction, durationHours))
            }
        }
        return resource
    }

    override suspend fun getCachedProducts(): List<Product> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun getCachedProductById(id: String): Product? {
        return dao.getAll().firstOrNull { it.id == id }?.toDomain()
    }

    private fun ProductDto.toDomain(): Product = Product(
        id = id,
        title = title,
        description = description,
        price = price,
        imageUrl = imageUrl,
        supplierId = supplierId,
        isApproved = isApproved,
        currentHighestBid = currentHighestBid,
        highestBidderName = highestBidderName,
        auctionEndTimeMillis = auctionEndTimeMillis
    )
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/data/repository/UserRepositoryImpl.kt`
```kotlin
package com.yeshuwahane.tradepulse.data.repository

import com.russhwolf.settings.Settings
import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.data.utils.apiCall
import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.model.UserRole
import com.yeshuwahane.tradepulse.domain.repository.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class LoginRequestDto(val email: String, val password: String, val role: String)

@Serializable
data class RegisterRequestDto(val name: String, val email: String, val password: String, val role: String)

@Serializable
data class UserDto(val id: String, val name: String, val email: String, val password: String, val role: String)

class UserRepositoryImpl(
    private val httpClient: HttpClient,
    private val settings: Settings
) : UserRepository {

    private val json = Json { ignoreUnknownKeys = true }
    private val settingsUserKey = "settings_user"

    override suspend fun getUsers(): DataResource<List<User>> {
        val resource = apiCall<List<UserDto>> {
            httpClient.get("/api/auth/users")
        }
        return if (resource.isSuccess() && resource.data != null) {
            DataResource.success(resource.data.map {
                User(it.id, it.name, it.email, it.password, UserRole.valueOf(it.role.uppercase()))
            })
        } else {
            DataResource.error(resource.error, emptyList())
        }
    }

    override suspend fun login(email: String, password: String, role: UserRole): DataResource<User> {
        val resource = apiCall<UserDto> {
            httpClient.post("/api/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequestDto(email, password, role.name))
            }
        }
        return if (resource.isSuccess() && resource.data != null) {
            val userDto = resource.data
            try {
                val userJson = json.encodeToString(UserDto.serializer(), userDto)
                settings.putString(settingsUserKey, userJson)
            } catch (e: Exception) {
                // Ignore serialization issues
            }
            DataResource.success(User(userDto.id, userDto.name, userDto.email, userDto.password, UserRole.valueOf(userDto.role.uppercase())))
        } else {
            DataResource.error(resource.error, null)
        }
    }

    override suspend fun register(name: String, email: String, password: String, role: UserRole): DataResource<User> {
        val resource = apiCall<UserDto> {
            httpClient.post("/api/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequestDto(name, email, password, role.name))
            }
        }
        return if (resource.isSuccess() && resource.data != null) {
            val userDto = resource.data
            try {
                val userJson = json.encodeToString(UserDto.serializer(), userDto)
                settings.putString(settingsUserKey, userJson)
            } catch (e: Exception) {
                // Ignore serialization issues
            }
            DataResource.success(User(userDto.id, userDto.name, userDto.email, userDto.password, UserRole.valueOf(userDto.role.uppercase())))
        } else {
            DataResource.error(resource.error, null)
        }
    }

    override suspend fun getSettingsUser(): User? {
        val userJson = settings.getStringOrNull(settingsUserKey) ?: return null
        return try {
            val userDto = json.decodeFromString(UserDto.serializer(), userJson)
            User(userDto.id, userDto.name, userDto.email, userDto.password, UserRole.valueOf(userDto.role.uppercase()))
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun clearSettingsUser(): Boolean {
        settings.remove(settingsUserKey)
        return true
    }

    override suspend fun deleteUser(id: String): DataResource<String> {
        val admin = getSettingsUser()
        return apiCall<String> {
            httpClient.delete("/api/auth/users/$id") {
                header("X-User-Id", admin?.id ?: "")
            }
        }
    }

    override suspend fun updateUser(id: String, name: String, email: String, password: String, role: UserRole): DataResource<String> {
        val admin = getSettingsUser()
        return apiCall<String> {
            httpClient.put("/api/auth/users/$id") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequestDto(name, email, password, role.name))
                header("X-User-Id", admin?.id ?: "")
            }
        }
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/data/utils/DataResource.kt`
```kotlin
package com.yeshuwahane.tradepulse.data.utils

data class DataResource<out T> constructor(
    var resourceState: ResourceState,
    val data: T?,
    val error: Throwable?,
    val loadingPercentage: Int? = null,
) {
    companion object {
        fun <T> initial(data: T? = null): DataResource<T> {
            return DataResource(ResourceState.INITIAL, data, null)
        }

        fun <T> loading(percentage: Int? = null, data: T? = null): DataResource<T> {
            return DataResource(ResourceState.LOADING, data, null, percentage)
        }

        fun <T> success(data: T): DataResource<T> {
            return DataResource(ResourceState.SUCCESS, data, null)
        }

        fun <T> error(error: Throwable?, data: T? = null): DataResource<T> {
            return DataResource(ResourceState.ERROR, data, error)
        }
    }

    fun isLoading() = resourceState == ResourceState.LOADING
    fun isSuccess() = resourceState == ResourceState.SUCCESS
    fun isError() = resourceState == ResourceState.ERROR
}

enum class ResourceState {
    INITIAL,
    LOADING,
    SUCCESS,
    ERROR,
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/data/utils/safeApiCall.kt`
```kotlin
package com.yeshuwahane.tradepulse.data.utils

import io.github.aakira.napier.Napier
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.plugins.ResponseException

suspend inline fun <reified T : Any> apiCall(apiCall: () -> HttpResponse): DataResource<T> {
    return try {
        val response: HttpResponse = apiCall.invoke()

        if (response.status.value in 200..299) {
            val responseBody = response.body<T>()
            DataResource.success(responseBody)
        } else {
            val errorText = try {
                response.bodyAsText()
            } catch (e: Exception) {
                response.status.description
            }
            DataResource.error(error = Throwable(errorText), data = null)
        }
    } catch (exception: Exception) {
        Napier.d("exception: ${exception.message}")
        val errorText = try {
            if (exception is ResponseException) {
                exception.response.bodyAsText()
            } else {
                exception.message ?: "Unknown error occurred"
            }
        } catch (e: Exception) {
            exception.message ?: "Unknown error occurred"
        }
        DataResource.error(error = Throwable(errorText), data = null)
    }
}

```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/di/AppModule.kt`
```kotlin
package com.yeshuwahane.tradepulse.di

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

// Data layer Repositories
import com.yeshuwahane.tradepulse.data.repository.ProductRepositoryImpl
import com.yeshuwahane.tradepulse.data.repository.UserRepositoryImpl
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository
import com.yeshuwahane.tradepulse.domain.repository.UserRepository

// Domain layer Use Cases
import com.yeshuwahane.tradepulse.domain.usecase.AddProductUseCase
import com.yeshuwahane.tradepulse.domain.usecase.ApproveProductUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetProductByIdUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetProductsUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetUsersUseCase
import com.yeshuwahane.tradepulse.domain.usecase.LoginUseCase
import com.yeshuwahane.tradepulse.domain.usecase.PlaceBidUseCase
import com.yeshuwahane.tradepulse.domain.usecase.RejectProductUseCase

// Presentation layer ViewModels
import com.yeshuwahane.tradepulse.presentation.login.LoginViewModel
import com.yeshuwahane.tradepulse.presentation.marketplace.MarketplaceViewModel
import com.yeshuwahane.tradepulse.presentation.detail.ProductDetailViewModel
import com.yeshuwahane.tradepulse.presentation.supplier.SupplierViewModel
import com.yeshuwahane.tradepulse.presentation.admin.AdminViewModel


fun appModule() = listOf(networkModule, commonModule)
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/di/CommonModule.kt`
```kotlin
package com.yeshuwahane.tradepulse.di

import com.russhwolf.settings.Settings
import com.yeshuwahane.tradepulse.data.repository.ProductRepositoryImpl
import com.yeshuwahane.tradepulse.data.repository.UserRepositoryImpl
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository
import com.yeshuwahane.tradepulse.domain.repository.UserRepository
import com.yeshuwahane.tradepulse.domain.usecase.AddProductUseCase
import com.yeshuwahane.tradepulse.domain.usecase.ApproveProductUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetProductByIdUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetProductsUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetUsersUseCase
import com.yeshuwahane.tradepulse.domain.usecase.LoginUseCase
import com.yeshuwahane.tradepulse.domain.usecase.PlaceBidUseCase
import com.yeshuwahane.tradepulse.domain.usecase.RejectProductUseCase
import com.yeshuwahane.tradepulse.presentation.admin.AdminViewModel
import com.yeshuwahane.tradepulse.presentation.detail.ProductDetailViewModel
import com.yeshuwahane.tradepulse.presentation.login.LoginViewModel
import com.yeshuwahane.tradepulse.presentation.marketplace.MarketplaceViewModel
import com.yeshuwahane.tradepulse.presentation.supplier.SupplierViewModel
import com.yeshuwahane.tradepulse.data.db.DatabaseDriverFactory
import com.yeshuwahane.tradepulse.data.db.TradePulseDb
import com.yeshuwahane.tradepulse.data.db.ProductDao
import com.yeshuwahane.tradepulse.data.db.ProductDaoImpl
import com.yeshuwahane.tradepulse.data.db.RoomDatabase
import org.koin.dsl.module

import com.yeshuwahane.tradepulse.domain.usecase.RegisterUseCase
import com.yeshuwahane.tradepulse.domain.usecase.UploadProductImageUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetSettingsUserUseCase
import com.yeshuwahane.tradepulse.domain.usecase.LogoutUseCase
import com.yeshuwahane.tradepulse.domain.usecase.DeleteUserUseCase
import com.yeshuwahane.tradepulse.domain.usecase.UpdateUserUseCase
import com.yeshuwahane.tradepulse.domain.usecase.UpdateProductUseCase

val commonModule = module {
    // Settings & Database
    single { Settings() }
    single { DatabaseDriverFactory() }
    single { TradePulseDb(get<DatabaseDriverFactory>().createDriver()) }
    single { RoomDatabase(get()) }
    single<ProductDao> { ProductDaoImpl(get()) }

    // Repositories
    single<ProductRepository> { ProductRepositoryImpl(get(), get<ProductDao>(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    // Use Cases
    single { GetProductsUseCase(get()) }
    single { GetUsersUseCase(get()) }
    single { ApproveProductUseCase(get()) }
    single { RejectProductUseCase(get()) }
    single { LoginUseCase(get()) }
    single { GetProductByIdUseCase(get()) }
    single { PlaceBidUseCase(get()) }
    single { AddProductUseCase(get()) }
    single { RegisterUseCase(get()) }
    single { UploadProductImageUseCase(get()) }
    single { GetSettingsUserUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { DeleteUserUseCase(get()) }
    single { UpdateUserUseCase(get()) }
    single { UpdateProductUseCase(get()) }

    // ViewModels
    factory { LoginViewModel(get(), get()) }
    factory { MarketplaceViewModel(get()) }
    factory { ProductDetailViewModel(get(), get(), get()) }
    factory { SupplierViewModel(get(), get(), get(), get(), get(), get()) }
    factory { AdminViewModel(get(), get(), get(), get(), get(), get(), get()) }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/di/NetworkModule.kt`
```kotlin
package com.yeshuwahane.tradepulse.di

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module


import io.ktor.client.plugins.defaultRequest
import com.yeshuwahane.tradepulse.getPlatform

val networkModule = module {
    single {
        HttpClient {
            defaultRequest {
                url("https://tradepulseapi-production.up.railway.app")
            }
            install(ContentNegotiation) {
                json(Json {
                    explicitNulls = false
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.d("Ktor: $message")
                    }
                }
                level = LogLevel.ALL
            }.also { Napier.base(DebugAntilog()) }

            install(HttpTimeout) {
                socketTimeoutMillis = 10_000
                requestTimeoutMillis = 100_000
            }
        }
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/model/Product.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.model

data class Product(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val supplierId: String,
    val isApproved: Boolean,
    val currentHighestBid: Double,
    val highestBidderName: String,
    val auctionEndTimeMillis: Long
) {
    val isAuction: Boolean
        get() = auctionEndTimeMillis > 0L
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/model/User.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.model

enum class UserRole {
    CUSTOMER,
    SUPPLIER,
    ADMIN
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: UserRole
)
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/repository/ProductRepository.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.repository

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(forceRefresh: Boolean = false): DataResource<List<Product>>
    suspend fun getProductById(id: String): DataResource<Product>
    suspend fun getCachedProducts(): List<Product>
    suspend fun getCachedProductById(id: String): Product?
    suspend fun placeBid(productId: String, amount: Double, bidderName: String): DataResource<String>
    suspend fun addProduct(
        title: String,
        description: String,
        price: Double,
        category: String,
        supplierId: String,
        isAuction: Boolean,
        durationHours: Int
    ): DataResource<Product>
    suspend fun approveProduct(productId: String): DataResource<String>
    suspend fun rejectProduct(productId: String): DataResource<String>
    suspend fun uploadProductImage(imageBytes: ByteArray): DataResource<String>
    suspend fun updateProduct(
        id: String,
        title: String,
        description: String,
        price: Double,
        category: String,
        supplierId: String,
        isAuction: Boolean,
        durationHours: Int
    ): DataResource<String>
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/repository/UserRepository.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.repository

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.model.UserRole

interface UserRepository {
    suspend fun getUsers(): DataResource<List<User>>
    suspend fun login(email: String, password: String, role: UserRole): DataResource<User>
    suspend fun register(name: String, email: String, password: String, role: UserRole): DataResource<User>
    suspend fun getSettingsUser(): User?
    suspend fun clearSettingsUser(): Boolean
    suspend fun deleteUser(id: String): DataResource<String>
    suspend fun updateUser(id: String, name: String, email: String, password: String, role: UserRole): DataResource<String>
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/AddProductUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.Product
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository

class AddProductUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(
        title: String,
        description: String,
        price: Double,
        category: String,
        supplierId: String,
        isAuction: Boolean,
        durationHours: Int
    ): DataResource<Product> {
        return productRepository.addProduct(title, description, price, category, supplierId, isAuction, durationHours)
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/ApproveProductUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository

class ApproveProductUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(productId: String): DataResource<String> {
        return productRepository.approveProduct(productId)
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/DeleteUserUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.repository.UserRepository

class DeleteUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: String): DataResource<String> {
        return userRepository.deleteUser(id)
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/GetProductByIdUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.Product
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository

class GetProductByIdUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(id: String): DataResource<Product> {
        return productRepository.getProductById(id)
    }

    suspend fun getCached(id: String): Product? {
        return productRepository.getCachedProductById(id)
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/GetProductsUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.Product
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository

class GetProductsUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(forceRefresh: Boolean = false): DataResource<List<Product>> {
        return productRepository.getProducts(forceRefresh)
    }

    suspend fun getCached(): List<Product> {
        return productRepository.getCachedProducts()
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/GetSettingsUserUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.repository.UserRepository

class GetSettingsUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): User? {
        return userRepository.getSettingsUser()
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/GetUsersUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.repository.UserRepository

class GetUsersUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): DataResource<List<User>> {
        return userRepository.getUsers()
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/LoginUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.model.UserRole
import com.yeshuwahane.tradepulse.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String, role: UserRole): DataResource<User> {
        return userRepository.login(email, password, role)
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/LogoutUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.domain.repository.UserRepository

class LogoutUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Boolean {
        return userRepository.clearSettingsUser()
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/PlaceBidUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository

class PlaceBidUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(productId: String, amount: Double, bidderName: String): DataResource<String> {
        return productRepository.placeBid(productId, amount, bidderName)
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/RegisterUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.model.UserRole
import com.yeshuwahane.tradepulse.domain.repository.UserRepository

class RegisterUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String, role: UserRole): DataResource<User> {
        return userRepository.register(name, email, password, role)
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/RejectProductUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository

class RejectProductUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(productId: String): DataResource<String> {
        return productRepository.rejectProduct(productId)
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/UpdateProductUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository

class UpdateProductUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(
        id: String,
        title: String,
        description: String,
        price: Double,
        category: String,
        supplierId: String,
        isAuction: Boolean,
        durationHours: Int
    ): DataResource<String> {
        return productRepository.updateProduct(
            id = id,
            title = title,
            description = description,
            price = price,
            category = category,
            supplierId = supplierId,
            isAuction = isAuction,
            durationHours = durationHours
        )
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/UpdateUserUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.UserRole
import com.yeshuwahane.tradepulse.domain.repository.UserRepository

class UpdateUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        id: String,
        name: String,
        email: String,
        password: String,
        role: UserRole
    ): DataResource<String> {
        return userRepository.updateUser(id, name, email, password, role)
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/domain/usecase/UploadProductImageUseCase.kt`
```kotlin
package com.yeshuwahane.tradepulse.domain.usecase

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.repository.ProductRepository

class UploadProductImageUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(imageBytes: ByteArray): DataResource<String> {
        return productRepository.uploadProductImage(imageBytes)
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/admin/AdminOverviewScreen.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yeshuwahane.tradepulse.domain.model.Product
import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.model.UserRole
import com.yeshuwahane.tradepulse.presentation.components.ProductImage
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import com.yeshuwahane.tradepulse.presentation.login.LoginScreen
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import com.yeshuwahane.tradepulse.domain.usecase.LogoutUseCase
import com.yeshuwahane.tradepulse.presentation.components.shimmerLoadingAnimation

class AdminOverviewScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<AdminViewModel>()
        val state by viewModel.state.collectAsState()

        val successMessage = state.showSuccessMessage
        if (successMessage != null) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { viewModel.onIntent(AdminIntent.DismissDialog) },
                title = { Text("Success") },
                text = { Text(successMessage) },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = { viewModel.onIntent(AdminIntent.DismissDialog) }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        val errorMessage = state.showErrorMessage
        if (errorMessage != null) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { viewModel.onIntent(AdminIntent.DismissDialog) },
                title = { Text("Error") },
                text = { Text(errorMessage) },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = { viewModel.onIntent(AdminIntent.DismissDialog) }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        val logoutUseCase = koinInject<LogoutUseCase>()
        val scope = rememberCoroutineScope()
        val tabTitles = listOf("Pending Approvals", "User Directory")

        val pagerState = rememberPagerState(
            initialPage = state.selectedTabIndex,
            pageCount = { tabTitles.size }
        )

        var editingUser by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<User?>(null) }

        LaunchedEffect(pagerState.currentPage) {
            if (state.selectedTabIndex != pagerState.currentPage) {
                viewModel.onIntent(AdminIntent.SelectTab(pagerState.currentPage))
            }
        }

        LaunchedEffect(state.selectedTabIndex) {
            if (pagerState.currentPage != state.selectedTabIndex) {
                pagerState.animateScrollToPage(state.selectedTabIndex)
            }
        }

        LaunchedEffect(Unit) {
            viewModel.onIntent(AdminIntent.LoadData)
        }

        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Admin Panel",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            state.currentAdminUser?.let { admin ->
                                val roleText = if (admin.id == "adm_01") "Chief Admin" else "Operations Manager"
                                val badgeBg = if (admin.id == "adm_01") Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                                val badgeTextColor = if (admin.id == "adm_01") Color(0xFF2E7D32) else Color(0xFFE65100)
                                Box(
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .background(badgeBg, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = roleText,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = badgeTextColor
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Platform Moderation & Control",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    IconButton(onClick = {
                        scope.launch {
                            logoutUseCase()
                            navigator.replaceAll(LoginScreen())
                        }
                    }) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Log Out / Switch Account"
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .padding(horizontal = 4.dp)
            ) {
                TabRow(
                    selectedTabIndex = state.selectedTabIndex,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = state.selectedTabIndex == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> {
                            val res = state.pendingProductsResource
                            val isRefreshing = res.isLoading()
                            if (res.isLoading() && res.data.isNullOrEmpty()) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(16.dp)
                                ) {
                                    items(3) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(140.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .shimmerLoadingAnimation()
                                        )
                                    }
                                }
                            } else if (res.isError() && res.data.isNullOrEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = res.error?.message ?: "An error occurred",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            } else {
                                androidx.compose.material3.pulltorefresh.PullToRefreshBox(
                                    isRefreshing = isRefreshing,
                                    onRefresh = { viewModel.onIntent(AdminIntent.LoadData) },
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    PendingApprovalsTab(res.data ?: emptyList(), state, viewModel)
                                }
                            }
                        }
                        1 -> {
                            val res = state.usersResource
                            val isRefreshing = res.isLoading()
                            if (res.isLoading() && res.data.isNullOrEmpty()) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(16.dp)
                                ) {
                                    items(5) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(72.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .shimmerLoadingAnimation()
                                        )
                                    }
                                }
                            } else if (res.isError() && res.data.isNullOrEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = res.error?.message ?: "An error occurred",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            } else {
                                androidx.compose.material3.pulltorefresh.PullToRefreshBox(
                                    isRefreshing = isRefreshing,
                                    onRefresh = { viewModel.onIntent(AdminIntent.LoadData) },
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    UserDirectoryTab(res.data ?: emptyList(), state, viewModel, onEditClick = { editingUser = it })
                                }
                            }
                        }
                    }
                }
            }
        }

        // Edit User Dialog
        if (editingUser != null) {
            val user = editingUser!!
            var name by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(user.name) }
            var email by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(user.email) }
            var password by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(user.password) }
            var role by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(user.role) }

            androidx.compose.material3.AlertDialog(
                onDismissRequest = { editingUser = null },
                title = { Text("Edit User Details") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Text("Role", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(UserRole.CUSTOMER, UserRole.SUPPLIER, UserRole.ADMIN).forEach { r ->
                                val selected = role == r
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable { role = r }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = r.name,
                                        fontSize = 10.sp,
                                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        viewModel.onIntent(AdminIntent.EditUser(user.id, name, email, password, role))
                        editingUser = null
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { editingUser = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }

    @Composable
    private fun PendingApprovalsTab(pendingProducts: List<Product>, state: AdminUiState, viewModel: AdminViewModel) {
        if (pendingProducts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "All Clear",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Listings Pending Approval",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(pendingProducts, key = { it.id }) { product ->
                    PendingProductCard(product, state, viewModel)
                }
            }
        }
    }

    @Composable
    private fun PendingProductCard(product: Product, state: AdminUiState, viewModel: AdminViewModel) {
        val isReadOnly = false

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val firstImage = product.imageUrl.split(",").firstOrNull() ?: product.imageUrl
                    ProductImage(
                        category = firstImage,
                        title = product.title,
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Base Price: $${product.price}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Box(
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .background(
                                    color = if (product.isAuction) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (product.isAuction) "Auction Listing" else "Fixed Price",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (product.isAuction) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = product.description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isReadOnly) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Moderation restricted to Chief Admin",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outline,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    val isProcessing = state.processingProductId == product.id
                    val isAnyProcessing = state.processingProductId != null

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { viewModel.onIntent(AdminIntent.RejectProduct(product.id)) },
                            enabled = !isAnyProcessing,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                disabledContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Reject",
                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Reject",
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.onIntent(AdminIntent.ApproveProduct(product.id)) },
                            enabled = !isAnyProcessing,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE8F5E9),
                                disabledContainerColor = Color(0xFFE8F5E9).copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(
                                    color = Color(0xFF2E7D32),
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Approve",
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Approve",
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun UserDirectoryTab(
        users: List<User>,
        state: AdminUiState,
        viewModel: AdminViewModel,
        onEditClick: (User) -> Unit
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(users, key = { it.id }) { user ->
                UserRowCard(user, state, viewModel, onEditClick)
            }
        }
    }

    @Composable
    private fun UserRowCard(
        user: User,
        state: AdminUiState,
        viewModel: AdminViewModel,
        onEditClick: (User) -> Unit
    ) {
        val currentAdmin = state.currentAdminUser
        val isChiefAdmin = currentAdmin?.id == "adm_01"
        val isSelf = user.id == currentAdmin?.id

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Icon",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = user.name + if (isSelf) " (You)" else "",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = user.email,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Password: ${user.password}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val (bgChipColor, textChipColor) = when (user.role) {
                        UserRole.CUSTOMER -> Pair(Color(0xFFE3F2FD), Color(0xFF1565C0))
                        UserRole.SUPPLIER -> Pair(Color(0xFFFFF8E1), Color(0xFFF57F17))
                        UserRole.ADMIN -> Pair(Color(0xFFF3E5F5), Color(0xFF6A1B9A))
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                color = bgChipColor,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = user.role.name.lowercase().replaceFirstChar { it.uppercase() },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = textChipColor
                        )
                    }

                    if (isChiefAdmin && !isSelf) {
                        val isDeleting = state.deletingUserId == user.id
                        IconButton(
                            onClick = { onEditClick(user) },
                            enabled = state.deletingUserId == null,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit User",
                                tint = if (state.deletingUserId == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        if (isDeleting) {
                            Box(
                                modifier = Modifier.size(28.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = Color(0xFFE53935)
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { viewModel.onIntent(AdminIntent.DeleteUser(user.id)) },
                                enabled = state.deletingUserId == null,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete User",
                                    tint = if (state.deletingUserId == null) Color(0xFFE53935) else Color(0xFFE53935).copy(alpha = 0.5f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    } else if (currentAdmin?.id == "adm_02" && !isSelf) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Read-Only",
                            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/admin/AdminUiState.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.admin

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.Product
import com.yeshuwahane.tradepulse.domain.model.User
import com.yeshuwahane.tradepulse.domain.model.UserRole

data class AdminUiState(
    val selectedTabIndex: Int = 0,
    val pendingProductsResource: DataResource<List<Product>> = DataResource.initial(),
    val usersResource: DataResource<List<User>> = DataResource.initial(),
    val currentAdminUser: User? = null,
    val showSuccessMessage: String? = null,
    val showErrorMessage: String? = null,
    val deletingUserId: String? = null,
    val processingProductId: String? = null
)

sealed interface AdminIntent {
    data class SelectTab(val index: Int) : AdminIntent
    data class ApproveProduct(val id: String) : AdminIntent
    data class RejectProduct(val id: String) : AdminIntent
    data class DeleteUser(val id: String) : AdminIntent
    data class EditUser(
        val id: String,
        val name: String,
        val email: String,
        val password: String,
        val role: UserRole
    ) : AdminIntent
    object LoadData : AdminIntent
    object DismissDialog : AdminIntent
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/admin/AdminViewModel.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.admin

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.usecase.ApproveProductUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetProductsUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetUsersUseCase
import com.yeshuwahane.tradepulse.domain.usecase.RejectProductUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetSettingsUserUseCase
import com.yeshuwahane.tradepulse.domain.usecase.DeleteUserUseCase
import com.yeshuwahane.tradepulse.domain.usecase.UpdateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val approveProductUseCase: ApproveProductUseCase,
    private val rejectProductUseCase: RejectProductUseCase,
    private val getSettingsUserUseCase: GetSettingsUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ScreenModel {
    private val _state = MutableStateFlow(AdminUiState())
    val state: StateFlow<AdminUiState> = _state.asStateFlow()

    fun onIntent(intent: AdminIntent) {
        when (intent) {
            is AdminIntent.SelectTab -> _state.update { it.copy(selectedTabIndex = intent.index) }
            is AdminIntent.ApproveProduct -> {
                _state.update { it.copy(processingProductId = intent.id) }
                screenModelScope.launch {
                    val result = approveProductUseCase(intent.id)
                    if (result.isSuccess()) {
                        _state.update { it.copy(showSuccessMessage = "Product approved successfully!", processingProductId = null) }
                    } else {
                        _state.update { it.copy(showErrorMessage = result.error?.message ?: "Failed to approve product.", processingProductId = null) }
                    }
                    loadData()
                }
            }
            is AdminIntent.RejectProduct -> {
                _state.update { it.copy(processingProductId = intent.id) }
                screenModelScope.launch {
                    val result = rejectProductUseCase(intent.id)
                    if (result.isSuccess()) {
                        _state.update { it.copy(showSuccessMessage = "Product rejected and removed successfully!", processingProductId = null) }
                    } else {
                        _state.update { it.copy(showErrorMessage = result.error?.message ?: "Failed to reject product.", processingProductId = null) }
                    }
                    loadData()
                }
            }
            is AdminIntent.DeleteUser -> {
                _state.update { it.copy(deletingUserId = intent.id) }
                screenModelScope.launch {
                    val result = deleteUserUseCase(intent.id)
                    if (result.isSuccess()) {
                        _state.update { it.copy(showSuccessMessage = "User deleted successfully!", deletingUserId = null) }
                    } else {
                        _state.update { it.copy(showErrorMessage = result.error?.message ?: "Failed to delete user.", deletingUserId = null) }
                    }
                    loadData()
                }
            }
            is AdminIntent.EditUser -> {
                screenModelScope.launch {
                    val result = updateUserUseCase(
                        id = intent.id,
                        name = intent.name,
                        email = intent.email,
                        password = intent.password,
                        role = intent.role
                    )
                    if (result.isSuccess()) {
                        _state.update { it.copy(showSuccessMessage = "User updated successfully!") }
                    } else {
                        _state.update { it.copy(showErrorMessage = result.error?.message ?: "Failed to update user.") }
                    }
                    loadData()
                }
            }
            AdminIntent.LoadData -> loadData()
            AdminIntent.DismissDialog -> {
                _state.update { it.copy(showSuccessMessage = null, showErrorMessage = null) }
            }
        }
    }

    private fun loadData() {
        _state.update {
            it.copy(
                pendingProductsResource = DataResource.loading(data = it.pendingProductsResource.data),
                usersResource = DataResource.loading(data = it.usersResource.data)
            )
        }
        screenModelScope.launch {
            val adminUser = getSettingsUserUseCase()
            val productsResult = getProductsUseCase(forceRefresh = true)
            val usersResult = getUsersUseCase()

            val pendingProductsResult = if (productsResult.isSuccess() && productsResult.data != null) {
                DataResource.success(productsResult.data.filter { !it.isApproved })
            } else {
                DataResource.error(productsResult.error, productsResult.data?.filter { !it.isApproved })
            }

            _state.update {
                it.copy(
                    pendingProductsResource = pendingProductsResult,
                    usersResource = usersResult,
                    currentAdminUser = adminUser
                )
            }
        }
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/components/ImagePicker.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun ImagePickerButton(
    onImagesSelected: (List<Pair<String, ByteArray>>) -> Unit,
    maxSelectionLimit: Int,
    modifier: Modifier = Modifier
)

expect fun byteArrayToImageBitmap(bytes: ByteArray): ImageBitmap
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/components/ProductImage.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import org.koin.compose.koinInject

@Composable
fun ProductImage(
    category: String,
    title: String,
    modifier: Modifier = Modifier
) {
    val httpClient = koinInject<HttpClient>()
    var imageBitmap by remember(category) { mutableStateOf<ImageBitmap?>(null) }
    var isLoadingImage by remember(category) { mutableStateOf(false) }

    val isNetworkImage = category.startsWith("http") || category.startsWith("/") || category.contains(".")

    LaunchedEffect(category) {
        if (isNetworkImage) {
            isLoadingImage = true
            try {
                val bytes = httpClient.get(category).readBytes()
                imageBitmap = byteArrayToImageBitmap(bytes)
            } catch (e: Exception) {
                // Fallback to gradient
            } finally {
                isLoadingImage = false
            }
        }
    }

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap!!,
            contentDescription = title,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        val actualCategory = if (isNetworkImage) "others" else category
        val colors = when (actualCategory.lowercase()) {
            "electronics" -> listOf(Color(0xFF8A2387), Color(0xFFE94057), Color(0xFFF27121))
            "audio" -> listOf(Color(0xFF00C6FF), Color(0xFF0072FF))
            "fashion" -> listOf(Color(0xFFF9D423), Color(0xFFFF4E50))
            else -> listOf(Color(0xFF4E54C8), Color(0xFF8F94FB))
        }

        Box(
            modifier = modifier
                .background(Brush.linearGradient(colors)),
            contentAlignment = Alignment.Center
        ) {
            if (isLoadingImage) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                val icon = when (actualCategory.lowercase()) {
                    "electronics" -> Icons.Default.ShoppingCart
                    "audio" -> Icons.Default.Star
                    "fashion" -> Icons.Default.List
                    else -> Icons.Default.Warning
                }

                Icon(
                    imageVector = icon,
                    contentDescription = actualCategory,
                    modifier = Modifier.fillMaxSize(0.35f),
                    tint = Color.White.copy(alpha = 0.4f)
                )

                Text(
                    text = title.take(2).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/components/Shimmer.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.shimmerLoadingAnimation(
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Modifier {
    return composed {
        val shimmerColors = listOf(
            Color.White.copy(alpha = 0.3f),
            Color.White.copy(alpha = 0.5f),
            Color.White.copy(alpha = 1.0f),
            Color.White.copy(alpha = 0.5f),
            Color.White.copy(alpha = 0.3f),
        )

        val transition = rememberInfiniteTransition(label = "")

        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "Shimmer loading animation",
        )

        this.background(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
                end = Offset(x = translateAnimation.value, y = angleOfAxisY),
            ),
        )
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/detail/ProductDetailScreen.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yeshuwahane.tradepulse.presentation.components.ProductImage
import com.yeshuwahane.tradepulse.getCurrentTimeMillis
import com.yeshuwahane.tradepulse.getPlatform
import kotlinx.coroutines.delay

import com.yeshuwahane.tradepulse.presentation.components.shimmerLoadingAnimation
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.safeDrawing

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape

class ProductDetailScreen(private val productId: String) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<ProductDetailViewModel>()
        val state by viewModel.state.collectAsState()

        if (state.showSuccess) {
            val isAuction = state.productResource.data?.isAuction ?: false
            val message = if (isAuction) "Bid placed successfully!" else "Purchase simulated successfully!"
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { viewModel.onIntent(DetailIntent.DismissDialog) },
                title = { Text("Success") },
                text = { Text(message) },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = { viewModel.onIntent(DetailIntent.DismissDialog) }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        if (state.validationError.isNotEmpty()) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { viewModel.onIntent(DetailIntent.DismissDialog) },
                title = { Text("Error") },
                text = { Text(state.validationError) },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = { viewModel.onIntent(DetailIntent.DismissDialog) }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        LaunchedEffect(productId) {
            viewModel.onIntent(DetailIntent.LoadProduct(productId))
        }

        val productResource = state.productResource
        val isAuction = productResource.data?.isAuction ?: false
        if (isAuction) {
            LaunchedEffect(productId) {
                while (true) {
                    viewModel.onIntent(DetailIntent.TickTimer(getCurrentTimeMillis()))
                    delay(1000)
                }
            }
        }

        if (productResource.isLoading()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(modifier = Modifier.height(56.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .shimmerLoadingAnimation()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(24.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmerLoadingAnimation()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmerLoadingAnimation()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmerLoadingAnimation()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .shimmerLoadingAnimation()
                    )
                }
            }
            return
        }

        if (productResource.isError()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = productResource.error?.message ?: "An error occurred",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            return
        }

        val product = productResource.data
        if (product == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Product not found",
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            return
        }

        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = "Product Details",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                val images = product.imageUrl.split(",")
                val imagePagerState = rememberPagerState(pageCount = { images.size })

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    HorizontalPager(
                        state = imagePagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        ProductImage(
                            category = images[page],
                            title = product.title,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    if (images.size > 1) {
                        Row(
                            Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            repeat(images.size) { index ->
                                val isSelected = imagePagerState.currentPage == index
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                        )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (product.isAuction) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.errorContainer,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Auction",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }

                        val remaining = product.auctionEndTimeMillis - state.currentTimeMillis
                        val timerText = if (remaining <= 0L) {
                            "Auction Ended"
                        } else {
                            val secs = remaining / 1000
                            val hours = secs / 3600
                            val mins = (secs % 3600) / 60
                            val seconds = secs % 60
                            "Ends in: ${hours.toString().padStart(2, '0')}:${
                                mins.toString().padStart(2, '0')
                            }:${seconds.toString().padStart(2, '0')}"
                        }

                        Text(
                            text = timerText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (remaining < 3600000) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Buy Out Mode",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = product.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Base Price: $${product.price}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Description",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.description,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (product.isAuction) {
                    val currentHighest = product.currentHighestBid
                    val currentBidder = product.highestBidderName

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Bidding Status",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (currentHighest > 0.0) "$$currentHighest" else "$${product.price}",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = if (currentHighest > 0.0) "Highest Bid" else "Starting Price",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = if (currentBidder.isNotEmpty()) currentBidder else "No bids yet",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Bidder",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))



                            OutlinedTextField(
                                value = state.bidAmount,
                                onValueChange = { viewModel.onIntent(DetailIntent.UpdateBidAmount(it)) },
                                label = { Text("Bid Amount ($)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { viewModel.onIntent(DetailIntent.SubmitBid(product.id)) },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                enabled = !state.isPlacingBid,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                if (state.isPlacingBid) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Text("Place Bid", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    Button(
                        onClick = { viewModel.onIntent(DetailIntent.SubmitBuyout) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                            .height(54.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Buy Now ($${product.price})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/detail/ProductDetailUiState.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.detail

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.Product

data class DetailUiState(
    val productResource: DataResource<Product> = DataResource.initial(),
    val bidderName: String = "",
    val bidAmount: String = "",
    val currentTimeMillis: Long = 0L,
    val validationError: String = "",
    val showSuccess: Boolean = false,
    val isPlacingBid: Boolean = false
)

sealed interface DetailIntent {
    data class LoadProduct(val id: String) : DetailIntent
    data class UpdateBidderName(val name: String) : DetailIntent
    data class UpdateBidAmount(val amount: String) : DetailIntent
    data class SubmitBid(val productId: String) : DetailIntent
    data class TickTimer(val time: Long) : DetailIntent
    object SubmitBuyout : DetailIntent
    object DismissDialog : DetailIntent
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/detail/ProductDetailViewModel.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.usecase.GetProductByIdUseCase
import com.yeshuwahane.tradepulse.domain.usecase.PlaceBidUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetSettingsUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val placeBidUseCase: PlaceBidUseCase,
    private val getSettingsUserUseCase: GetSettingsUserUseCase
) : ScreenModel {
    private val _state = MutableStateFlow(DetailUiState())
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    fun onIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.LoadProduct -> {
                screenModelScope.launch {
                    val user = getSettingsUserUseCase()
                    if (user != null) {
                        _state.update { it.copy(bidderName = user.name) }
                    }
                    val cached = getProductByIdUseCase.getCached(intent.id)
                    if (cached != null) {
                        _state.update { it.copy(productResource = DataResource.success(cached), validationError = "", showSuccess = false) }
                    } else {
                        _state.update { it.copy(productResource = DataResource.loading(data = it.productResource.data), validationError = "", showSuccess = false) }
                    }
                    val prodResult = getProductByIdUseCase(intent.id)
                    _state.update { it.copy(productResource = prodResult) }
                }
            }
            is DetailIntent.UpdateBidderName -> {
                _state.update { it.copy(bidderName = intent.name, validationError = "") }
            }
            is DetailIntent.UpdateBidAmount -> {
                _state.update { it.copy(bidAmount = intent.amount, validationError = "") }
            }
            is DetailIntent.TickTimer -> {
                _state.update { it.copy(currentTimeMillis = intent.time) }
            }
            is DetailIntent.SubmitBid -> {
                val currentState = _state.value
                val prod = currentState.productResource.data
                if (prod != null) {
                    val amount = currentState.bidAmount.toDoubleOrNull()
                    val minRequired = if (prod.currentHighestBid > 0.0) prod.currentHighestBid else prod.price

                    if (currentState.bidderName.isBlank()) {
                        _state.update { it.copy(validationError = "Please enter your name.") }
                    } else if (amount == null) {
                        _state.update { it.copy(validationError = "Please enter a valid numeric amount.") }
                    } else if (amount <= minRequired) {
                        _state.update { it.copy(validationError = "Bid must be greater than $$minRequired") }
                    } else {
                        _state.update { it.copy(isPlacingBid = true) }
                        screenModelScope.launch {
                            val placeBidResult = placeBidUseCase(intent.productId, amount, currentState.bidderName)
                            if (placeBidResult.isSuccess()) {
                                val updatedProductResult = getProductByIdUseCase(intent.productId)
                                _state.update {
                                    it.copy(
                                        productResource = updatedProductResult,
                                        bidAmount = "",
                                        validationError = "",
                                        showSuccess = true,
                                        isPlacingBid = false
                                    )
                                }
                            } else {
                                val errorMsg = placeBidResult.error?.message ?: "Failed to place bid."
                                _state.update { it.copy(validationError = errorMsg, isPlacingBid = false) }
                            }
                        }
                    }
                }
            }
            DetailIntent.SubmitBuyout -> {
                _state.update { it.copy(showSuccess = true) }
            }
            DetailIntent.DismissDialog -> {
                _state.update { it.copy(showSuccess = false, validationError = "") }
            }
        }
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/login/LoginScreen.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yeshuwahane.tradepulse.domain.model.UserRole
import com.yeshuwahane.tradepulse.presentation.admin.AdminOverviewScreen
import com.yeshuwahane.tradepulse.presentation.marketplace.CustomerMarketplaceScreen
import com.yeshuwahane.tradepulse.presentation.supplier.SupplierDashboardScreen

class LoginScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<LoginViewModel>()
        val state by viewModel.state.collectAsState()
        val effect by viewModel.effect.collectAsState()

        var isSignUpMode by remember { mutableStateOf(false) }

        val pagerState = rememberPagerState(
            initialPage = state.selectedRole.ordinal,
            pageCount = { UserRole.values().size }
        )
        val scope = rememberCoroutineScope()

        LaunchedEffect(pagerState.currentPage) {
            val role = UserRole.values()[pagerState.currentPage]
            if (state.selectedRole != role) {
                viewModel.onIntent(LoginIntent.SelectRole(role))
                isSignUpMode = false
            }
        }

        LaunchedEffect(state.selectedRole) {
            if (pagerState.currentPage != state.selectedRole.ordinal) {
                pagerState.animateScrollToPage(state.selectedRole.ordinal)
            }
        }

        LaunchedEffect(effect) {
            effect?.let {
                when (it) {
                    LoginEffect.NavigateToCustomerMarketplace -> navigator.replaceAll(
                        CustomerMarketplaceScreen()
                    )
                    LoginEffect.NavigateToSupplierDashboard -> navigator.replaceAll(
                        SupplierDashboardScreen()
                    )
                    LoginEffect.NavigateToAdminDashboard -> navigator.replaceAll(AdminOverviewScreen())
                }
                viewModel.resetEffect()
            }
        }

        val errorMessage = state.errorMessage
        if (errorMessage.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = { viewModel.onIntent(LoginIntent.DismissDialog) },
                title = {
                    Text(
                        text = "Authentication Failed",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                },
                text = {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.onIntent(LoginIntent.DismissDialog) }
                    ) {
                        Text(
                            text = "Dismiss",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            )
        }

        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "TRADEPULSE",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 3.sp
                    )
                    Text(
                        text = "B2B Trading & Auction Terminal",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                TabRow(
                    selectedTabIndex = state.selectedRole.ordinal,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    UserRole.values().forEach { role ->
                        Tab(
                            selected = state.selectedRole == role,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(role.ordinal)
                                }
                            },
                            text = {
                                Text(
                                    text = role.name.lowercase().replaceFirstChar { it.uppercase() },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val pageRole = UserRole.values()[page]
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (isSignUpMode) "Supplier/Client Signup" else "${pageRole.name} Login",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                AnimatedVisibility(visible = isSignUpMode) {
                                    Column {
                                        OutlinedTextField(
                                            value = state.name,
                                            onValueChange = { viewModel.onIntent(LoginIntent.UpdateName(it)) },
                                            label = { Text("Display / Company Name") },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Default.Person,
                                                    contentDescription = "Name"
                                                )
                                            },
                                            singleLine = true,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }

                                OutlinedTextField(
                                    value = state.email,
                                    onValueChange = { viewModel.onIntent(LoginIntent.UpdateEmail(it)) },
                                    label = { Text("Email Address") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Email,
                                            contentDescription = "Email"
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = state.password,
                                    onValueChange = { viewModel.onIntent(LoginIntent.UpdatePassword(it)) },
                                    label = { Text("Password") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Lock,
                                            contentDescription = "Password"
                                        )
                                    },
                                    visualTransformation = PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        if (isSignUpMode) {
                                            viewModel.onIntent(LoginIntent.SubmitRegister)
                                        } else {
                                            viewModel.onIntent(LoginIntent.SubmitLogin)
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = when (pageRole) {
                                            UserRole.CUSTOMER -> MaterialTheme.colorScheme.primary
                                            UserRole.SUPPLIER -> MaterialTheme.colorScheme.secondary
                                            UserRole.ADMIN -> MaterialTheme.colorScheme.tertiary
                                        }
                                    ),
                                    enabled = !state.isLoading
                                ) {
                                    if (state.isLoading) {
                                        CircularProgressIndicator(
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else {
                                        Text(
                                            text = if (isSignUpMode) "Register & Authenticate" else "Secure Login",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                if (pageRole != UserRole.ADMIN) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = if (isSignUpMode) "Already have an account? " else "Need a new account? ",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = if (isSignUpMode) "Log In" else "Sign Up",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.clickable {
                                                isSignUpMode = !isSignUpMode
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Auto-Fill Simulator Accounts",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            when (pageRole) {
                                UserRole.CUSTOMER -> {
                                    AutoFillButton(
                                        label = "Alice Smith (Customer)",
                                        email = "alice@customer.com",
                                        psw = "alice123",
                                        onClick = {
                                            viewModel.onIntent(LoginIntent.UpdateEmail("alice@customer.com"))
                                            viewModel.onIntent(LoginIntent.UpdatePassword("alice123"))
                                        }
                                    )
                                    AutoFillButton(
                                        label = "Bob Jones (Customer)",
                                        email = "bob@customer.com",
                                        psw = "bob123",
                                        onClick = {
                                            viewModel.onIntent(LoginIntent.UpdateEmail("bob@customer.com"))
                                            viewModel.onIntent(LoginIntent.UpdatePassword("bob123"))
                                        }
                                    )
                                }
                                UserRole.SUPPLIER -> {
                                    AutoFillButton(
                                        label = "Global Tech (Supplier)",
                                        email = "info@globaltech.com",
                                        psw = "global123",
                                        onClick = {
                                            viewModel.onIntent(LoginIntent.UpdateEmail("info@globaltech.com"))
                                            viewModel.onIntent(LoginIntent.UpdatePassword("global123"))
                                        }
                                    )
                                    AutoFillButton(
                                        label = "Apex Electronics (Supplier)",
                                        email = "sales@apexelectronics.com",
                                        psw = "apex123",
                                        onClick = {
                                            viewModel.onIntent(LoginIntent.UpdateEmail("sales@apexelectronics.com"))
                                            viewModel.onIntent(LoginIntent.UpdatePassword("apex123"))
                                        }
                                    )
                                }
                                UserRole.ADMIN -> {
                                    AutoFillButton(
                                        label = "Chief Admin (Admin)",
                                        email = "admin@zeerostock.com",
                                        psw = "admin123",
                                        onClick = {
                                            viewModel.onIntent(LoginIntent.UpdateEmail("admin@zeerostock.com"))
                                            viewModel.onIntent(LoginIntent.UpdatePassword("admin123"))
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    AutoFillButton(
                                        label = "Operations Manager (Admin)",
                                        email = "manager@zeerostock.com",
                                        psw = "manager123",
                                        onClick = {
                                            viewModel.onIntent(LoginIntent.UpdateEmail("manager@zeerostock.com"))
                                            viewModel.onIntent(LoginIntent.UpdatePassword("manager123"))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    @Composable
    private fun AutoFillButton(
        label: String,
        email: String,
        psw: String,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.5f
                )
            )
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Autofill",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Email: $email | Psw: $psw",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/login/LoginUiState.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.login

import com.yeshuwahane.tradepulse.domain.model.UserRole

data class LoginUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val selectedRole: UserRole = UserRole.CUSTOMER,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isLoggedIn: Boolean = false
)

sealed interface LoginIntent {
    data class UpdateName(val name: String) : LoginIntent
    data class UpdateEmail(val email: String) : LoginIntent
    data class UpdatePassword(val password: String) : LoginIntent
    data class SelectRole(val role: UserRole) : LoginIntent
    object SubmitLogin : LoginIntent
    object SubmitRegister : LoginIntent
    object DismissDialog : LoginIntent
}

sealed interface LoginEffect {
    object NavigateToCustomerMarketplace : LoginEffect
    object NavigateToSupplierDashboard : LoginEffect
    object NavigateToAdminDashboard : LoginEffect
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/login/LoginViewModel.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yeshuwahane.tradepulse.domain.usecase.LoginUseCase
import com.yeshuwahane.tradepulse.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ScreenModel {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _effect = MutableStateFlow<LoginEffect?>(null)
    val effect: StateFlow<LoginEffect?> = _effect.asStateFlow()

    fun resetEffect() {
        _effect.value = null
    }

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UpdateName -> _state.update { it.copy(name = intent.name, errorMessage = "") }
            is LoginIntent.UpdateEmail -> _state.update { it.copy(email = intent.email, errorMessage = "") }
            is LoginIntent.UpdatePassword -> _state.update { it.copy(password = intent.password, errorMessage = "") }
            is LoginIntent.SelectRole -> _state.update { it.copy(selectedRole = intent.role, errorMessage = "") }
            is LoginIntent.SubmitLogin -> performLogin()
            is LoginIntent.SubmitRegister -> performRegister()
            LoginIntent.DismissDialog -> _state.update { it.copy(errorMessage = "") }
        }
    }

    private fun performLogin() {
        val currentState = _state.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _state.update { it.copy(errorMessage = "Credentials cannot be blank.") }
            return
        }
        _state.update { it.copy(isLoading = true) }
        screenModelScope.launch {
            val matchedUserResource = loginUseCase(
                email = currentState.email,
                password = currentState.password,
                role = currentState.selectedRole
            )

            if (matchedUserResource.isSuccess() && matchedUserResource.data != null) {
                _state.update { it.copy(isLoading = false, isLoggedIn = true) }
                when (currentState.selectedRole) {
                    com.yeshuwahane.tradepulse.domain.model.UserRole.CUSTOMER -> _effect.value = LoginEffect.NavigateToCustomerMarketplace
                    com.yeshuwahane.tradepulse.domain.model.UserRole.SUPPLIER -> _effect.value = LoginEffect.NavigateToSupplierDashboard
                    com.yeshuwahane.tradepulse.domain.model.UserRole.ADMIN -> _effect.value = LoginEffect.NavigateToAdminDashboard
                }
            } else {
                val errorMsg = matchedUserResource.error?.message ?: "Invalid credentials or role mismatch. Check User Directory in Admin dashboard."
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMsg
                    )
                }
            }
        }
    }

    private fun performRegister() {
        val currentState = _state.value
        if (currentState.name.isBlank() || currentState.email.isBlank() || currentState.password.isBlank()) {
            _state.update { it.copy(errorMessage = "All fields are required for sign up.") }
            return
        }
        _state.update { it.copy(isLoading = true) }
        screenModelScope.launch {
            val registeredUserResource = registerUseCase(
                name = currentState.name,
                email = currentState.email,
                password = currentState.password,
                role = currentState.selectedRole
            )

            if (registeredUserResource.isSuccess() && registeredUserResource.data != null) {
                _state.update { it.copy(isLoading = false, isLoggedIn = true) }
                when (currentState.selectedRole) {
                    com.yeshuwahane.tradepulse.domain.model.UserRole.CUSTOMER -> _effect.value = LoginEffect.NavigateToCustomerMarketplace
                    com.yeshuwahane.tradepulse.domain.model.UserRole.SUPPLIER -> _effect.value = LoginEffect.NavigateToSupplierDashboard
                    com.yeshuwahane.tradepulse.domain.model.UserRole.ADMIN -> _effect.value = LoginEffect.NavigateToAdminDashboard
                }
            } else {
                val errorMsg = registeredUserResource.error?.message ?: "Registration failed. Email might already exist."
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMsg
                    )
                }
            }
        }
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/marketplace/CustomerMarketplaceScreen.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.marketplace

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yeshuwahane.tradepulse.domain.model.Product
import com.yeshuwahane.tradepulse.presentation.components.ProductImage
import com.yeshuwahane.tradepulse.presentation.login.LoginScreen
import com.yeshuwahane.tradepulse.presentation.detail.ProductDetailScreen
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import com.yeshuwahane.tradepulse.domain.usecase.LogoutUseCase
import com.yeshuwahane.tradepulse.presentation.components.shimmerLoadingAnimation

class CustomerMarketplaceScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<MarketplaceViewModel>()
        val state by viewModel.state.collectAsState()
        val logoutUseCase = koinInject<LogoutUseCase>()
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            viewModel.onIntent(MarketplaceIntent.LoadProducts)
        }

        val productsResource = state.productsResource

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "TradePulsestock Marketplace",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = "B2B Active Listings",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                logoutUseCase()
                                navigator.replaceAll(LoginScreen())
                            }
                        }) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Log Out / Switch Account")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        ) { paddingValues ->
            val isRefreshing = productsResource.isLoading()

            if (productsResource.isLoading() && productsResource.data.isNullOrEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues)
                ) {
                    items(6) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .shimmerLoadingAnimation()
                        )
                    }
                }
            } else if (productsResource.isError() && productsResource.data.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = productsResource.error?.message ?: "An error occurred",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                val products = productsResource.data ?: emptyList()
                androidx.compose.material3.pulltorefresh.PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = { viewModel.onIntent(MarketplaceIntent.LoadProducts) },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues)
                ) {
                    if (products.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "No Products",
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.outline
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No Approved Products Available",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = "Check back later or register as a supplier to add stock.",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 24.dp)
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${products.size} Items Available",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(products, key = { it.id }) { product ->
                                    val firstImage = product.imageUrl.split(",").firstOrNull() ?: product.imageUrl
                                    ProductCard(
                                        product = product.copy(imageUrl = firstImage),
                                        onClick = { navigator.push(ProductDetailScreen(product.id)) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ProductCard(
        product: Product,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                ProductImage(
                    category = product.imageUrl,
                    title = product.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    if (product.isAuction) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.errorContainer,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Auction Active",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Buy Out",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = product.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    val displayPrice = if (product.isAuction && product.currentHighestBid > 0.0) {
                        product.currentHighestBid
                    } else {
                        product.price
                    }

                    Text(
                        text = "$${"%.2f".format(displayPrice)}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = if (product.isAuction) "Current Bid" else "Fixed Price",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

fun String.format(vararg args: Any?): String {
    val parts = this.split("%.2f")
    if (parts.size == 2 && args.isNotEmpty()) {
        val num = args[0] as? Double ?: 0.0
        val sign = if (num < 0) "-" else ""
        val absNum = kotlin.math.abs(num)
        val temp = (absNum * 100 + 0.5).toLong()
        val dollars = temp / 100
        val cents = temp % 100
        val centsStr = if (cents < 10) "0$cents" else "$cents"
        return "${parts[0]}$sign$dollars.$centsStr${parts[1]}"
    }
    return this
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/marketplace/MarketplaceUiState.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.marketplace

import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.model.Product

data class MarketplaceUiState(
    val productsResource: DataResource<List<Product>> = DataResource.initial()
)

sealed interface MarketplaceIntent {
    object LoadProducts : MarketplaceIntent
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/marketplace/MarketplaceViewModel.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.marketplace

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MarketplaceViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : ScreenModel {
    private val _state = MutableStateFlow(MarketplaceUiState())
    val state: StateFlow<MarketplaceUiState> = _state.asStateFlow()

    fun onIntent(intent: MarketplaceIntent) {
        when (intent) {
            MarketplaceIntent.LoadProducts -> {
                screenModelScope.launch {
                    val cached = getProductsUseCase.getCached().filter { p -> p.isApproved }
                    if (cached.isNotEmpty()) {
                        _state.update { it.copy(productsResource = DataResource.success(cached)) }
                    } else {
                        _state.update { it.copy(productsResource = DataResource.loading(data = it.productsResource.data)) }
                    }
                    val productsResult = getProductsUseCase()
                    val filteredResult = if (productsResult.isSuccess() && productsResult.data != null) {
                        DataResource.success(productsResult.data.filter { p -> p.isApproved })
                    } else {
                        DataResource.error(productsResult.error, productsResult.data?.filter { p -> p.isApproved })
                    }
                    _state.update { it.copy(productsResource = filteredResult) }
                }
            }
        }
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/supplier/SupplierDashboardScreen.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.supplier

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yeshuwahane.tradepulse.domain.model.Product
import com.yeshuwahane.tradepulse.domain.usecase.LogoutUseCase
import com.yeshuwahane.tradepulse.presentation.components.ImagePickerButton
import com.yeshuwahane.tradepulse.presentation.components.ProductImage
import com.yeshuwahane.tradepulse.presentation.components.byteArrayToImageBitmap
import com.yeshuwahane.tradepulse.presentation.components.shimmerLoadingAnimation
import com.yeshuwahane.tradepulse.presentation.login.LoginScreen
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class SupplierDashboardScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<SupplierViewModel>()
        val state by viewModel.state.collectAsState()
        val logoutUseCase = koinInject<LogoutUseCase>()
        val scope = rememberCoroutineScope()

        val categories = listOf("electronics", "audio", "fashion", "others")
        val tabTitles = listOf("My Listings", if (state.editingProductId != null) "Edit Stock" else "Add Stock")

        LaunchedEffect(Unit) {
            viewModel.onIntent(SupplierIntent.LoadData)
        }

        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Supplier Dashboard",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        state.currentSupplier?.let { user ->
                            Text(
                                text = "Logged in as ${user.name}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                    IconButton(onClick = {
                        scope.launch {
                            logoutUseCase()
                            navigator.replaceAll(LoginScreen())
                        }
                    }) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Log Out / Switch Account"
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
            ) {
                TabRow(
                    selectedTabIndex = state.selectedTabIndex,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = state.selectedTabIndex == index,
                            onClick = { viewModel.onIntent(SupplierIntent.SelectTab(index)) },
                            text = { Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp) }
                        )
                    }
                }

                if (state.selectedTabIndex == 0) {
                    SupplierListingsTab(state.supplierProducts, state, viewModel)
                } else {
                    SupplierUploadFormTab(state, viewModel, categories)
                }

                if (state.showSuccess) {
                    androidx.compose.material3.AlertDialog(
                        onDismissRequest = { viewModel.onIntent(SupplierIntent.DismissDialog) },
                        title = { Text("Success") },
                        text = {
                            Text(
                                if (state.editingProductId != null) "Listing updated successfully!"
                                else "Listing uploaded and pending approval!"
                            )
                        },
                        confirmButton = {
                            androidx.compose.material3.TextButton(
                                onClick = { viewModel.onIntent(SupplierIntent.DismissDialog) }
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }

                if (state.validationError.isNotEmpty()) {
                    androidx.compose.material3.AlertDialog(
                        onDismissRequest = { viewModel.onIntent(SupplierIntent.DismissDialog) },
                        title = { Text("Error") },
                        text = { Text(state.validationError) },
                        confirmButton = {
                            androidx.compose.material3.TextButton(
                                onClick = { viewModel.onIntent(SupplierIntent.DismissDialog) }
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun SupplierListingsTab(
        products: List<Product>,
        state: SupplierUiState,
        viewModel: SupplierViewModel
    ) {
        if (state.isLoading && products.isEmpty()) {
            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(5) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shimmerLoadingAnimation()
                    )
                }
            }
        } else {
            androidx.compose.material3.pulltorefresh.PullToRefreshBox(
                isRefreshing = state.isLoading,
                onRefresh = { viewModel.onIntent(SupplierIntent.LoadData) },
                modifier = Modifier.fillMaxSize()
            ) {
                if (products.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No stock listings found.\nClick the 'Add Stock' tab to upload your first product.",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.outline,
                            fontSize = 15.sp
                        )
                    }
                } else {
                    androidx.compose.foundation.lazy.LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(products.size, key = { products[it].id }) { index ->
                            val product = products[index]
                            SupplierProductCard(product, viewModel)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SupplierProductCard(
        product: Product,
        viewModel: SupplierViewModel
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val firstImage = product.imageUrl.split(",").firstOrNull() ?: product.imageUrl
                    ProductImage(
                        category = firstImage,
                        title = product.title,
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Base Price: $${product.price}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.outline
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        val badgeBg = if (product.isApproved) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                        val badgeTextColor = if (product.isApproved) Color(0xFF2E7D32) else Color(0xFFE65100)
                        val badgeText = if (product.isApproved) "Approved" else "Pending Approval"

                        Box(
                            modifier = Modifier
                                .background(badgeBg, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badgeText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = badgeTextColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.onIntent(SupplierIntent.EditProduct(product)) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Edit",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = { viewModel.onIntent(SupplierIntent.RemoveProduct(product.id)) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Remove",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun SupplierUploadFormTab(
        state: SupplierUiState,
        viewModel: SupplierViewModel,
        categories: List<String>
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (state.editingProductId != null) "Edit Stock Listing" else "Submit New Stock Listing",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "Listings require administrative approval before appearing in the marketplace feed.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.onIntent(SupplierIntent.UpdateTitle(it)) },
                label = { Text("Product Title") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onIntent(SupplierIntent.UpdateDescription(it)) },
                label = { Text("Product Description") },
                minLines = 3,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.priceString,
                onValueChange = { viewModel.onIntent(SupplierIntent.UpdatePrice(it)) },
                label = { Text("Starting / Base Price ($)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Product Gallery Images",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Add one or more images of the item.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val maxRemaining = 8 - state.selectedImages.size
                        if (maxRemaining > 0) {
                            ImagePickerButton(
                                onImagesSelected = { images ->
                                    viewModel.onIntent(SupplierIntent.AddSelectedImages(images))
                                },
                                maxSelectionLimit = maxRemaining,
                                modifier = Modifier.height(44.dp)
                            )
                        } else {
                            Button(
                                onClick = {},
                                enabled = false,
                                modifier = Modifier.height(44.dp)
                            ) {
                                Text("Maximum 8 images added")
                            }
                        }
                    }

                    if (state.selectedImages.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        androidx.compose.foundation.lazy.LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(state.selectedImages.size) { index ->
                                val (name, bytes) = state.selectedImages[index]
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                                ) {
                                    val bitmap = remember(bytes) {
                                        try {
                                            byteArrayToImageBitmap(bytes)
                                        } catch (e: Exception) {
                                            null
                                        }
                                    }
                                    if (bitmap != null) {
                                        Image(
                                            bitmap = bitmap,
                                            contentDescription = name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(MaterialTheme.colorScheme.secondaryContainer),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = name.takeLast(6),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.padding(4.dp)
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(2.dp)
                                            .size(20.dp)
                                            .clip(androidx.compose.foundation.shape.CircleShape)
                                            .background(Color(0xFFEF5350))
                                            .clickable {
                                                viewModel.onIntent(SupplierIntent.RemoveSelectedImage(index))
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Remove",
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Product Image & Category Theme",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = state.selectedCategory == category
                    val gradientColors = when (category) {
                        "electronics" -> listOf(Color(0xFF8A2387), Color(0xFFE94057))
                        "audio" -> listOf(Color(0xFF00C6FF), Color(0xFF0072FF))
                        "fashion" -> listOf(Color(0xFFF9D423), Color(0xFFFF4E50))
                        else -> listOf(Color(0xFF4E54C8), Color(0xFF8F94FB))
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.linearGradient(gradientColors))
                            .border(
                                width = if (isSelected) 3.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                viewModel.onIntent(SupplierIntent.UpdateCategory(category))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category.replaceFirstChar { it.uppercase() },
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Auction Listing",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Enable bidding for users",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
                Switch(
                    checked = state.isAuction,
                    onCheckedChange = { viewModel.onIntent(SupplierIntent.ToggleAuction(it)) }
                )
            }

            AnimatedVisibility(visible = state.isAuction) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = state.durationHoursString,
                        onValueChange = { viewModel.onIntent(SupplierIntent.UpdateDuration(it)) },
                        label = { Text("Auction Duration (Hours)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (state.editingProductId != null) {
                    Button(
                        onClick = { viewModel.onIntent(SupplierIntent.CancelEdit) },
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text("Cancel", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Button(
                    onClick = { viewModel.onIntent(SupplierIntent.SubmitUpload) },
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (state.editingProductId != null) Icons.Default.Check else Icons.Default.Add,
                                contentDescription = "Submit"
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (state.editingProductId != null) "Save Changes" else "Upload Product",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/supplier/SupplierUiState.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.supplier

import com.yeshuwahane.tradepulse.domain.model.Product
import com.yeshuwahane.tradepulse.domain.model.User

data class SupplierUiState(
    val title: String = "",
    val description: String = "",
    val priceString: String = "",
    val selectedCategory: String = "electronics",
    val isAuction: Boolean = false,
    val durationHoursString: String = "24",
    val isLoading: Boolean = false,
    val showSuccess: Boolean = false,
    val validationError: String = "",
    val selectedImages: List<Pair<String, ByteArray>> = emptyList(),
    val selectedTabIndex: Int = 0,
    val supplierProducts: List<Product> = emptyList(),
    val editingProductId: String? = null,
    val currentSupplier: User? = null
)

sealed interface SupplierIntent {
    data class UpdateTitle(val title: String) : SupplierIntent
    data class UpdateDescription(val description: String) : SupplierIntent
    data class UpdatePrice(val price: String) : SupplierIntent
    data class UpdateCategory(val category: String) : SupplierIntent
    data class ToggleAuction(val isAuction: Boolean) : SupplierIntent
    data class UpdateDuration(val duration: String) : SupplierIntent
    data class AddSelectedImages(val images: List<Pair<String, ByteArray>>) : SupplierIntent
    data class RemoveSelectedImage(val index: Int) : SupplierIntent
    object SubmitUpload : SupplierIntent
    object DismissDialog : SupplierIntent
    data class SelectTab(val index: Int) : SupplierIntent
    data class EditProduct(val product: Product) : SupplierIntent
    data class RemoveProduct(val productId: String) : SupplierIntent
    object CancelEdit : SupplierIntent
    object LoadData : SupplierIntent
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/presentation/supplier/SupplierViewModel.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.supplier

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yeshuwahane.tradepulse.data.utils.DataResource
import com.yeshuwahane.tradepulse.domain.usecase.AddProductUseCase
import com.yeshuwahane.tradepulse.domain.usecase.UploadProductImageUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetProductsUseCase
import com.yeshuwahane.tradepulse.domain.usecase.GetSettingsUserUseCase
import com.yeshuwahane.tradepulse.domain.usecase.RejectProductUseCase
import com.yeshuwahane.tradepulse.domain.usecase.UpdateProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SupplierViewModel(
    private val addProductUseCase: AddProductUseCase,
    private val uploadProductImageUseCase: UploadProductImageUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val getSettingsUserUseCase: GetSettingsUserUseCase,
    private val rejectProductUseCase: RejectProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase
) : ScreenModel {
    private val _state = MutableStateFlow(SupplierUiState())
    val state: StateFlow<SupplierUiState> = _state.asStateFlow()

    fun onIntent(intent: SupplierIntent) {
        when (intent) {
            is SupplierIntent.UpdateTitle -> _state.update { it.copy(title = intent.title, validationError = "", showSuccess = false) }
            is SupplierIntent.UpdateDescription -> _state.update { it.copy(description = intent.description, validationError = "", showSuccess = false) }
            is SupplierIntent.UpdatePrice -> _state.update { it.copy(priceString = intent.price, validationError = "", showSuccess = false) }
            is SupplierIntent.UpdateCategory -> _state.update { it.copy(selectedCategory = intent.category, validationError = "", showSuccess = false) }
            is SupplierIntent.ToggleAuction -> _state.update { it.copy(isAuction = intent.isAuction, validationError = "", showSuccess = false) }
            is SupplierIntent.UpdateDuration -> _state.update { it.copy(durationHoursString = intent.duration, validationError = "", showSuccess = false) }
            is SupplierIntent.AddSelectedImages -> {
                _state.update {
                    val updatedList = (it.selectedImages + intent.images).take(8)
                    it.copy(selectedImages = updatedList, validationError = "", showSuccess = false)
                }
            }
            is SupplierIntent.RemoveSelectedImage -> {
                _state.update {
                    if (intent.index in it.selectedImages.indices) {
                        val updatedList = it.selectedImages.toMutableList().apply { removeAt(intent.index) }
                        it.copy(selectedImages = updatedList, validationError = "", showSuccess = false)
                    } else {
                        it
                    }
                }
            }
            SupplierIntent.SubmitUpload -> performUpload()
            SupplierIntent.DismissDialog -> _state.update { it.copy(showSuccess = false, validationError = "") }
            is SupplierIntent.SelectTab -> _state.update { it.copy(selectedTabIndex = intent.index) }
            is SupplierIntent.EditProduct -> {
                val prod = intent.product
                _state.update {
                    it.copy(
                        title = prod.title,
                        description = prod.description,
                        priceString = prod.price.toString(),
                        selectedCategory = prod.imageUrl.split(",").firstOrNull() ?: "electronics",
                        isAuction = prod.isAuction,
                        durationHoursString = "24",
                        editingProductId = prod.id,
                        selectedTabIndex = 1, // Switch to form tab
                        validationError = "",
                        showSuccess = false
                    )
                }
            }
            is SupplierIntent.RemoveProduct -> {
                screenModelScope.launch {
                    val result = rejectProductUseCase(intent.productId)
                    if (result.isSuccess()) {
                        loadData()
                    } else {
                        _state.update { it.copy(validationError = result.error?.message ?: "Failed to remove product.") }
                    }
                }
            }
            SupplierIntent.CancelEdit -> {
                _state.update {
                    it.copy(
                        title = "",
                        description = "",
                        priceString = "",
                        selectedCategory = "electronics",
                        isAuction = false,
                        durationHoursString = "24",
                        editingProductId = null,
                        selectedTabIndex = 0, // Switch back to list tab
                        selectedImages = emptyList(),
                        validationError = "",
                        showSuccess = false
                    )
                }
            }
            SupplierIntent.LoadData -> loadData()
        }
    }

    private fun loadData() {
        _state.update { it.copy(isLoading = true) }
        screenModelScope.launch {
            val user = getSettingsUserUseCase()
            val productsResult = getProductsUseCase(forceRefresh = true)
            if (productsResult.isSuccess() && productsResult.data != null) {
                val supplierProducts = if (user != null) {
                    productsResult.data.filter { it.supplierId == user.id }
                } else {
                    productsResult.data.filter { it.supplierId == "sup_current" }
                }
                _state.update {
                    it.copy(
                        supplierProducts = supplierProducts,
                        currentSupplier = user,
                        isLoading = false
                    )
                }
            } else {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun performUpload() {
        val currentState = _state.value
        val price = currentState.priceString.toDoubleOrNull()
        val duration = currentState.durationHoursString.toIntOrNull()

        if (currentState.title.isBlank()) {
            _state.update { it.copy(validationError = "Please enter product title.") }
        } else if (currentState.description.isBlank()) {
            _state.update { it.copy(validationError = "Please enter description.") }
        } else if (price == null || price <= 0.0) {
            _state.update { it.copy(validationError = "Please enter a valid base price.") }
        } else if (currentState.isAuction && (duration == null || duration <= 0)) {
            _state.update { it.copy(validationError = "Please enter a valid auction duration in hours.") }
        } else {
            _state.update { it.copy(isLoading = true, validationError = "") }
            screenModelScope.launch {
                val imageList = mutableListOf<String>()
                
                // Upload each selected gallery image sequentially
                for (imagePair in currentState.selectedImages) {
                    val uploadResult = uploadProductImageUseCase(imagePair.second)
                    if (uploadResult.isSuccess() && uploadResult.data != null) {
                        imageList.add(uploadResult.data)
                    }
                }
                
                // Fallback to beautiful categories if no gallery images were selected/uploaded
                if (imageList.isEmpty()) {
                    imageList.add(currentState.selectedCategory)
                    val derivedTheme = when (currentState.selectedCategory) {
                        "electronics" -> "audio"
                        "audio" -> "fashion"
                        "fashion" -> "others"
                        else -> "electronics"
                    }
                    imageList.add(derivedTheme)
                }
                
                val finalImageUrl = imageList.joinToString(",")
                val supplierId = currentState.currentSupplier?.id ?: "sup_current"

                val isEditing = currentState.editingProductId != null
                val result = if (isEditing) {
                    updateProductUseCase(
                        id = currentState.editingProductId!!,
                        title = currentState.title,
                        description = currentState.description,
                        price = price,
                        category = finalImageUrl,
                        supplierId = supplierId,
                        isAuction = currentState.isAuction,
                        durationHours = duration ?: 24
                    )
                } else {
                    addProductUseCase(
                        title = currentState.title,
                        description = currentState.description,
                        price = price,
                        category = finalImageUrl,
                        supplierId = supplierId,
                        isAuction = currentState.isAuction,
                        durationHours = duration ?: 24
                    )
                }

                if (result.isSuccess()) {
                    // Reset editing form fields, show success popup, switch back to listings
                    _state.update {
                        it.copy(
                            title = "",
                            description = "",
                            priceString = "",
                            selectedCategory = "electronics",
                            isAuction = false,
                            durationHoursString = "24",
                            selectedImages = emptyList(),
                            editingProductId = null,
                            showSuccess = true,
                            isLoading = false,
                            selectedTabIndex = 0
                        )
                    }
                    loadData()
                } else {
                    val errorMsg = result.error?.message ?: "Failed to save product listing."
                    _state.update { it.copy(isLoading = false, validationError = errorMsg) }
                }
            }
        }
    }
}
```

### File: `shared/src/commonMain/kotlin/com/yeshuwahane/tradepulse/theme/Theme.kt`
```kotlin
package com.yeshuwahane.tradepulse.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),
    background = Color(0xFF121214),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1E1D22),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF323038),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFF625B71),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),
    tertiary = Color(0xFF7D5260),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),
    background = Color(0xFFF7F6FA),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B)
)

@Composable
fun TradePulseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
```

### File: `shared/src/commonMain/sqldelight/com/yeshuwahane/tradepulse/data/db/TradePulseDb.sq`
```sql
CREATE TABLE UserEntity (
    id TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    password TEXT NOT NULL,
    role TEXT NOT NULL
);

CREATE TABLE ProductEntity (
    id TEXT NOT NULL PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    price REAL NOT NULL,
    imageUrl TEXT NOT NULL,
    supplierId TEXT NOT NULL,
    isApproved INTEGER NOT NULL, -- 0 for false, 1 for true
    currentHighestBid REAL NOT NULL,
    highestBidderName TEXT NOT NULL,
    auctionEndTimeMillis INTEGER NOT NULL
);

-- Queries for User
insertUser:
INSERT OR REPLACE INTO UserEntity(id, name, email, password, role)
VALUES (?, ?, ?, ?, ?);

getUser:
SELECT * FROM UserEntity LIMIT 1;

clearUsers:
DELETE FROM UserEntity;

-- Queries for Product
insertProduct:
INSERT OR REPLACE INTO ProductEntity(id, title, description, price, imageUrl, supplierId, isApproved, currentHighestBid, highestBidderName, auctionEndTimeMillis)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);

getAllProducts:
SELECT * FROM ProductEntity;

clearProducts:
DELETE FROM ProductEntity;
```

### File: `shared/src/iosMain/kotlin/com/yeshuwahane/tradepulse/MainViewController.kt`
```kotlin
package com.yeshuwahane.tradepulse

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController { App() }
```

### File: `shared/src/iosMain/kotlin/com/yeshuwahane/tradepulse/Platform.ios.kt`
```kotlin
package com.yeshuwahane.tradepulse

import platform.UIKit.UIDevice
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.posix.gettimeofday
import platform.posix.timeval

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

@OptIn(ExperimentalForeignApi::class)
actual fun getCurrentTimeMillis(): Long = memScoped {
    val timeVal = alloc<timeval>()
    gettimeofday(timeVal.ptr, null)
    (timeVal.tv_sec * 1000L) + (timeVal.tv_usec / 1000L)
}
```

### File: `shared/src/iosMain/kotlin/com/yeshuwahane/tradepulse/data/db/PlatformDatabaseDriver.ios.kt`
```kotlin
package com.yeshuwahane.tradepulse.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(TradePulseDb.Schema, "tradepulse.db")
    }
}
```

### File: `shared/src/iosMain/kotlin/com/yeshuwahane/tradepulse/presentation/components/ImagePicker.ios.kt`
```kotlin
package com.yeshuwahane.tradepulse.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.Image
import com.yeshuwahane.tradepulse.getCurrentTimeMillis
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSCodingProtocol
import platform.Foundation.NSItemProviderReadingProtocol

private var activeDelegate: PHPickerViewControllerDelegateProtocol? = null

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val size = this.length.toInt()
    val byteArray = ByteArray(size)
    if (size > 0) {
        byteArray.usePinned { pinned ->
            platform.posix.memcpy(pinned.addressOf(0), this.bytes, this.length)
        }
    }
    return byteArray
}

@Composable
actual fun ImagePickerButton(
    onImagesSelected: (List<Pair<String, ByteArray>>) -> Unit,
    maxSelectionLimit: Int,
    modifier: Modifier
) {
    Button(
        onClick = {
            val window = UIApplication.sharedApplication.keyWindow 
                ?: UIApplication.sharedApplication.windows.firstOrNull() as? UIWindow
            var topViewController = window?.rootViewController
            while (topViewController?.presentedViewController != null) {
                topViewController = topViewController.presentedViewController
            }

            if (topViewController != null) {
                val configuration = PHPickerConfiguration()
                configuration.filter = PHPickerFilter.imagesFilter()
                configuration.selectionLimit = maxSelectionLimit.toLong()

                val picker = PHPickerViewController(configuration)
                val delegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
                    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
                        picker.dismissViewControllerAnimated(true, null)
                        activeDelegate = null

                        val results = didFinishPicking.filterIsInstance<PHPickerResult>()
                        if (results.isEmpty()) return

                        val selectedImages = mutableListOf<Pair<String, ByteArray>>()
                        var remaining = results.size

                        for (result in results) {
                            val itemProvider = result.itemProvider
                            if (itemProvider.hasItemConformingToTypeIdentifier("public.image")) {
                                itemProvider.loadDataRepresentationForTypeIdentifier("public.image") { nsData, error ->
                                    if (nsData != null) {
                                        val bytes = nsData.toByteArray()
                                        val name = "ios_image_${getCurrentTimeMillis()}_${selectedImages.size}.jpg"
                                        selectedImages.add(name to bytes)
                                    }
                                    
                                    remaining--
                                    if (remaining == 0) {
                                        dispatch_async(dispatch_get_main_queue()) {
                                            onImagesSelected(selectedImages)
                                        }
                                    }
                                }
                            } else {
                                remaining--
                                if (remaining == 0) {
                                    dispatch_async(dispatch_get_main_queue()) {
                                        onImagesSelected(selectedImages)
                                    }
                                }
                            }
                        }
                    }
                }

                activeDelegate = delegate
                picker.delegate = delegate
                topViewController.presentViewController(picker, animated = true, completion = null)
            }
        },
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add Images")
        Spacer(modifier = Modifier.width(6.dp))
        Text("Add Images from iOS Gallery")
    }
}

actual fun byteArrayToImageBitmap(bytes: ByteArray): ImageBitmap {
    return Image.makeFromEncoded(bytes).toComposeImageBitmap()
}
```


---

## Part 2: Ktor Backend API

Create the following files for the Backend project. Each file path and its full content is specified below.

### File: `build.gradle.kts`
```kotlin
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
}

group = "com.tradepulse"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

application {
    mainClass.set("com.tradepulse.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf(
        "-Dio.ktor.development=$isDevelopment",
        "--enable-native-access=ALL-UNNAMED"
    )
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Database: SQLite, PostgreSQL & Exposed ORM
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
```

### File: `gradle.properties`
```properties
ktor_version=2.2.3
kotlin_version=1.8.10
logback_version=1.2.11
kotlin.code.style=official
```

### File: `gradle/wrapper/gradle-wrapper.properties`
```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.14.4-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

### File: `settings.gradle.kts`
```kotlin
rootProject.name = "tradepulse_api"
```

### File: `src/main/kotlin/com/tradepulse/Application.kt`
```kotlin
package com.tradepulse

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.tradepulse.plugins.*

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDatabase()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
```

### File: `src/main/kotlin/com/tradepulse/models/BidRequest.kt`
```kotlin
package com.tradepulse.models

import kotlinx.serialization.Serializable

@Serializable
data class BidRequest(
    val amount: Double,
    val bidderName: String
)
```

### File: `src/main/kotlin/com/tradepulse/models/LoginRequest.kt`
```kotlin
package com.tradepulse.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
    val role: UserRole
)
```

### File: `src/main/kotlin/com/tradepulse/models/Product.kt`
```kotlin
package com.tradepulse.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val supplierId: String,
    val isApproved: Boolean,
    val currentHighestBid: Double,
    val highestBidderName: String,
    val auctionEndTimeMillis: Long
)
```

### File: `src/main/kotlin/com/tradepulse/models/RegisterRequest.kt`
```kotlin
package com.tradepulse.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: UserRole
)
```

### File: `src/main/kotlin/com/tradepulse/models/UploadProductRequest.kt`
```kotlin
package com.tradepulse.models

import kotlinx.serialization.Serializable

@Serializable
data class UploadProductRequest(
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val supplierId: String,
    val isAuction: Boolean,
    val durationHours: Int
)
```

### File: `src/main/kotlin/com/tradepulse/models/User.kt`
```kotlin
package com.tradepulse.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: UserRole
)
```

### File: `src/main/kotlin/com/tradepulse/models/UserRole.kt`
```kotlin
package com.tradepulse.models

public enum class UserRole {
    CUSTOMER,
    SUPPLIER,
    ADMIN
}
```

### File: `src/main/kotlin/com/tradepulse/plugins/Database.kt`
```kotlin
package com.tradepulse.plugins

import com.tradepulse.models.UserRole
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.awt.Color
import java.awt.Font
import java.awt.GradientPaint
import java.awt.image.BufferedImage
import java.io.File
import java.net.URI
import javax.imageio.ImageIO

object UsersTable : Table("users") {
    val id = varchar("id", 50)
    val name = varchar("name", 100)
    val email = varchar("email", 100).uniqueIndex()
    val password = varchar("password", 100)
    val role = varchar("role", 20)

    override val primaryKey = PrimaryKey(id)
}

object ProductsTable : Table("products") {
    val id = varchar("id", 50)
    val title = varchar("title", 100)
    val description = text("description")
    val price = double("price")
    val imageUrl = varchar("image_url", 255)
    val supplierId = varchar("supplier_id", 50)
    val isApproved = bool("is_approved")
    val currentHighestBid = double("current_highest_bid")
    val highestBidderName = varchar("highest_bidder_name", 100)
    val auctionEndTimeMillis = long("auction_end_time_millis")

    override val primaryKey = PrimaryKey(id)
}

object ImagesTable : Table("images") {
    val id = varchar("id", 50)
    val content = text("content")

    override val primaryKey = PrimaryKey(id)
}

fun generatePlaceholderImage(fileName: String, text: String, color1: Color, color2: Color) {
    val dir = File("uploads")
    if (!dir.exists()) dir.mkdirs()
    val file = File(dir, fileName)
    if (file.exists()) return

    val width = 600
    val height = 400
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val g2d = bufferedImage.createGraphics()

    // Draw gradient background
    val gp = GradientPaint(0f, 0f, color1, width.toFloat(), height.toFloat(), color2)
    g2d.paint = gp
    g2d.fillRect(0, 0, width, height)

    // Draw text overlay
    g2d.color = Color.WHITE
    g2d.font = Font("Arial", Font.BOLD, 36)
    val metrics = g2d.fontMetrics
    val x = (width - metrics.stringWidth(text)) / 2
    val y = ((height - metrics.height) / 2) + metrics.ascent
    g2d.drawString(text, x, y)
    g2d.dispose()

    ImageIO.write(bufferedImage, "png", file)
}

fun convertDatabaseUrlToJdbc(url: String): String {
    if (url.startsWith("postgres://") || url.startsWith("postgresql://")) {
        val uri = URI(url)
        val port = if (uri.port == -1) "" else ":${uri.port}"
        val query = buildList {
            if (!uri.rawQuery.isNullOrBlank()) add(uri.rawQuery)

            val userInfo = uri.rawUserInfo?.split(":", limit = 2)
            if (userInfo?.size == 2) {
                add("user=${userInfo[0]}")
                add("password=${userInfo[1]}")
            }
        }.joinToString("&")
        val querySuffix = if (query.isBlank()) "" else "?$query"

        return "jdbc:postgresql://${uri.host}$port${uri.rawPath}$querySuffix"
    }
    return url
}

fun configureDatabase() {
    val rawDbUrl = System.getenv("JDBC_DATABASE_URL") ?: System.getenv("DATABASE_URL")
    val database = if (!rawDbUrl.isNullOrBlank()) {
        val dbUrl = convertDatabaseUrlToJdbc(rawDbUrl)
        val driver = if (dbUrl.contains("postgresql")) "org.postgresql.Driver" else "org.sqlite.JDBC"
        Database.connect(
            url = dbUrl,
            driver = driver
        )
    } else {
        val dbFile = File("tradepulse.db")
        Database.connect(
            url = "jdbc:sqlite:${dbFile.absolutePath}",
            driver = "org.sqlite.JDBC"
        )
    }

    generatePlaceholderImage("iphone15.png", "iPhone 15 Pro Max", Color(0xFF, 0x3B, 0x30), Color(0xFF, 0x95, 0x00))
    generatePlaceholderImage("macbook.png", "MacBook Pro M3", Color(0x5A, 0x2A, 0x8C), Color(0x30, 0x75, 0xDC))
    generatePlaceholderImage("headphones.png", "Sony WH-1000XM5", Color(0x00, 0xC6, 0xFF), Color(0x00, 0x72, 0xFF))
    generatePlaceholderImage("jordan1.png", "Air Jordan 1 Chicago", Color(0xF9, 0xD4, 0x23), Color(0xFF, 0x4E, 0x50))
    generatePlaceholderImage("jacket.png", "Vintage Leather Jacket", Color(0x4E, 0x54, 0xC8), Color(0x8F, 0x94, 0xFB))
    generatePlaceholderImage("keyboard.png", "Gaming Keyboard", Color(0x11, 0x99, 0x8E), Color(0x38, 0xEF, 0x7D))

    transaction(database) {
        SchemaUtils.create(UsersTable, ProductsTable, ImagesTable)

        // Seed users
        if (UsersTable.selectAll().count() == 0L) {
            UsersTable.insert {
                it[id] = "usr_01"
                it[name] = "Alice Smith"
                it[email] = "alice@customer.com"
                it[password] = "alice123"
                it[role] = UserRole.CUSTOMER.name
            }
            UsersTable.insert {
                it[id] = "usr_02"
                it[name] = "Bob Jones"
                it[email] = "bob@customer.com"
                it[password] = "bob123"
                it[role] = UserRole.CUSTOMER.name
            }
            UsersTable.insert {
                it[id] = "usr_03"
                it[name] = "Charlie Brown"
                it[email] = "charlie@customer.com"
                it[password] = "charlie123"
                it[role] = UserRole.CUSTOMER.name
            }
            UsersTable.insert {
                it[id] = "sup_01"
                it[name] = "Global Tech Supplies"
                it[email] = "info@globaltech.com"
                it[password] = "global123"
                it[role] = UserRole.SUPPLIER.name
            }
            UsersTable.insert {
                it[id] = "sup_02"
                it[name] = "Apex Electronics"
                it[email] = "sales@apexelectronics.com"
                it[password] = "apex123"
                it[role] = UserRole.SUPPLIER.name
            }
            UsersTable.insert {
                it[id] = "sup_03"
                it[name] = "Retro Thrift Co."
                it[email] = "retrothrift@gmail.com"
                it[password] = "retro123"
                it[role] = UserRole.SUPPLIER.name
            }
            UsersTable.insert {
                it[id] = "adm_01"
                it[name] = "Chief Admin"
                it[email] = "admin@tradepulse.com"
                it[password] = "admin123"
                it[role] = UserRole.ADMIN.name
            }
            UsersTable.insert {
                it[id] = "adm_02"
                it[name] = "Operations Manager"
                it[email] = "manager@tradepulse.com"
                it[password] = "manager123"
                it[role] = UserRole.ADMIN.name
            }
        }

        // Migrate product imageUrls if they have the old non-url
        val hasOldImageUrl = ProductsTable.selectAll().any { row ->
            val url = row[ProductsTable.imageUrl]
            url == "electronics" || url == "audio" || url == "fashion"
        }
        if (hasOldImageUrl) {
            ProductsTable.deleteAll()
        }

        // Seed products
        if (ProductsTable.selectAll().count() == 0L) {
            val now = System.currentTimeMillis()
            ProductsTable.insert {
                it[id] = "1"
                it[title] = "iPhone 15 Pro Max (256GB)"
                it[description] = "Titanium design, A17 Pro chip, customizable Action button, and the most powerful iPhone camera system ever. Excellent condition, barely used."
                it[price] = 999.00
                it[imageUrl] = "/uploads/iphone15.png,electronics,audio"
                it[supplierId] = "sup_01"
                it[isApproved] = true
                it[currentHighestBid] = 1050.00
                it[highestBidderName] = "Alice Smith"
                it[auctionEndTimeMillis] = now + 3600000L * 2L
            }
            ProductsTable.insert {
                it[id] = "2"
                it[title] = "MacBook Pro M3 Max"
                it[description] = "16-inch liquid retina XDR display, 36GB unified memory, 1TB SSD. The ultimate powerhouse for developers and creators alike. Sealed in box."
                it[price] = 2499.00
                it[imageUrl] = "/uploads/macbook.png,electronics,audio"
                it[supplierId] = "sup_02"
                it[isApproved] = true
                it[currentHighestBid] = 2600.00
                it[highestBidderName] = "Bob Jones"
                it[auctionEndTimeMillis] = now + 3600000L * 5L
            }
            ProductsTable.insert {
                it[id] = "3"
                it[title] = "Sony WH-1000XM5 Headphones"
                it[description] = "Industry-leading noise canceling wireless headphones with auto noise-canceling optimizer, crystal clear hands-free calling, and Alexa voice control."
                it[price] = 348.00
                it[imageUrl] = "/uploads/headphones.png,audio"
                it[supplierId] = "sup_01"
                it[isApproved] = true
                it[currentHighestBid] = 0.0
                it[highestBidderName] = ""
                it[auctionEndTimeMillis] = 0L
            }
            ProductsTable.insert {
                it[id] = "4"
                it[title] = "Air Jordan 1 Retro High OG"
                it[description] = "Classic Chicago colorway, premium leather upper, comfortable air-sole cushioning. Perfect collector's item in size 10."
                it[price] = 180.00
                it[imageUrl] = "/uploads/jordan1.png,fashion"
                it[supplierId] = "sup_03"
                it[isApproved] = true
                it[currentHighestBid] = 210.00
                it[highestBidderName] = "Charlie Brown"
                it[auctionEndTimeMillis] = now + 3600000L * 12L
            }
            ProductsTable.insert {
                it[id] = "5"
                it[title] = "Vintage Leather Jacket"
                it[description] = "Genuine brown leather jacket, distressed style from the 90s. Heavyweight material, perfect for autumn and winter."
                it[price] = 120.00
                it[imageUrl] = "/uploads/jacket.png,fashion"
                it[supplierId] = "sup_03"
                it[isApproved] = false
                it[currentHighestBid] = 0.0
                it[highestBidderName] = ""
                it[auctionEndTimeMillis] = 0L
            }
            ProductsTable.insert {
                it[id] = "6"
                it[title] = "Mechanical Gaming Keyboard"
                it[description] = "Custom hot-swappable mechanical keyboard, linear yellow switches, PBT keycaps, RGB backlighting. Brand new construction."
                it[price] = 85.00
                it[imageUrl] = "/uploads/keyboard.png,electronics"
                it[supplierId] = "sup_02"
                it[isApproved] = false
                it[currentHighestBid] = 0.0
                it[highestBidderName] = ""
                it[auctionEndTimeMillis] = now + 3600000L * 24L
            }
        }
    }
}
```

### File: `src/main/kotlin/com/tradepulse/plugins/Monitoring.kt`
```kotlin
package com.tradepulse.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
    }
}
```

### File: `src/main/kotlin/com/tradepulse/plugins/Routing.kt`
```kotlin
package com.tradepulse.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.tradepulse.routes.*

fun Application.configureRouting() {
    routing {
        authRouting()
        productRouting()
    }
}
```

### File: `src/main/kotlin/com/tradepulse/plugins/Serialization.kt`
```kotlin
package com.tradepulse.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}
```

### File: `src/main/kotlin/com/tradepulse/routes/AuthRouting.kt`
```kotlin
package com.tradepulse.routes

import com.tradepulse.models.LoginRequest
import com.tradepulse.models.RegisterRequest
import com.tradepulse.models.User
import com.tradepulse.models.UserRole
import com.tradepulse.plugins.UsersTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

fun Route.authRouting() {
    route("/api/auth") {
        post("/login") {
            val request = call.receive<LoginRequest>()
            val user = transaction {
                UsersTable.select {
                    (UsersTable.email eq request.email) and
                    (UsersTable.password eq request.password) and
                    (UsersTable.role eq request.role.name)
                }.map { row ->
                    User(
                        id = row[UsersTable.id],
                        name = row[UsersTable.name],
                        email = row[UsersTable.email],
                        password = row[UsersTable.password],
                        role = UserRole.valueOf(row[UsersTable.role])
                    )
                }.firstOrNull()
            }

            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials or role mismatch.")
            }
        }

        post("/register") {
            val request = call.receive<RegisterRequest>()
            
            val existingUser = transaction {
                UsersTable.select { UsersTable.email eq request.email }.count() > 0
            }

            if (existingUser) {
                call.respond(HttpStatusCode.Conflict, "Email is already registered.")
                return@post
            }

            val newId = UUID.randomUUID().toString()
            val newUser = transaction {
                UsersTable.insert {
                    it[id] = newId
                    it[name] = request.name
                    it[email] = request.email
                    it[password] = request.password
                    it[role] = request.role.name
                }
                User(
                    id = newId,
                    name = request.name,
                    email = request.email,
                    password = request.password,
                    role = request.role
                )
            }

            call.respond(HttpStatusCode.Created, newUser)
        }

        get("/users") {
            val users = transaction {
                UsersTable.selectAll().map { row ->
                    User(
                        id = row[UsersTable.id],
                        name = row[UsersTable.name],
                        email = row[UsersTable.email],
                        password = row[UsersTable.password],
                        role = UserRole.valueOf(row[UsersTable.role])
                    )
                }
            }
            call.respond(HttpStatusCode.OK, users)
        }

        delete("/users/{id}") {
            val requesterId = call.request.headers["X-User-Id"]
            if (requesterId == "adm_02") {
                return@delete call.respond(HttpStatusCode.Forbidden, "Operations Manager does not have permission to delete users.")
            }
            val idParam = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing user ID.")
            val success = transaction {
                UsersTable.deleteWhere { UsersTable.id eq idParam } > 0
            }
            if (success) {
                call.respond(HttpStatusCode.OK, "User deleted successfully.")
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found.")
            }
        }

        put("/users/{id}") {
            val requesterId = call.request.headers["X-User-Id"]
            if (requesterId == "adm_02") {
                return@put call.respond(HttpStatusCode.Forbidden, "Operations Manager does not have permission to edit users.")
            }
            val idParam = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest, "Missing user ID.")
            val request = call.receive<RegisterRequest>()
            val success = transaction {
                UsersTable.update({ UsersTable.id eq idParam }) {
                    it[name] = request.name
                    it[email] = request.email
                    it[password] = request.password
                    it[role] = request.role.name
                } > 0
            }
            if (success) {
                call.respond(HttpStatusCode.OK, "User updated successfully.")
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found.")
            }
        }
    }
}
```

### File: `src/main/kotlin/com/tradepulse/routes/ProductRouting.kt`
```kotlin
package com.tradepulse.routes

import com.tradepulse.models.BidRequest
import com.tradepulse.models.Product
import com.tradepulse.models.UploadProductRequest
import com.tradepulse.plugins.ProductsTable
import com.tradepulse.plugins.ImagesTable
import io.ktor.server.response.respondBytes
import java.util.Base64
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.util.UUID

private var productsLastUpdated: Long = System.currentTimeMillis()

private fun notifyProductUpdate() {
    productsLastUpdated = System.currentTimeMillis()
}

fun Route.productRouting() {
    // Serve static uploads
    static("/uploads") {
        files("uploads")
    }

    route("/api/products") {
        get("/last-updated") {
            call.respond(HttpStatusCode.OK, mapOf("lastUpdated" to productsLastUpdated))
        }

        get {
            val products = transaction {
                ProductsTable.selectAll().map { row ->
                    Product(
                        id = row[ProductsTable.id],
                        title = row[ProductsTable.title],
                        description = row[ProductsTable.description],
                        price = row[ProductsTable.price],
                        imageUrl = row[ProductsTable.imageUrl],
                        supplierId = row[ProductsTable.supplierId],
                        isApproved = row[ProductsTable.isApproved],
                        currentHighestBid = row[ProductsTable.currentHighestBid],
                        highestBidderName = row[ProductsTable.highestBidderName],
                        auctionEndTimeMillis = row[ProductsTable.auctionEndTimeMillis]
                    )
                }
            }
            call.respond(HttpStatusCode.OK, products)
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing product ID.")
            val product = transaction {
                ProductsTable.select { ProductsTable.id eq id }.map { row ->
                    Product(
                        id = row[ProductsTable.id],
                        title = row[ProductsTable.title],
                        description = row[ProductsTable.description],
                        price = row[ProductsTable.price],
                        imageUrl = row[ProductsTable.imageUrl],
                        supplierId = row[ProductsTable.supplierId],
                        isApproved = row[ProductsTable.isApproved],
                        currentHighestBid = row[ProductsTable.currentHighestBid],
                        highestBidderName = row[ProductsTable.highestBidderName],
                        auctionEndTimeMillis = row[ProductsTable.auctionEndTimeMillis]
                    )
                }.firstOrNull()
            }

            if (product != null) {
                call.respond(HttpStatusCode.OK, product)
            } else {
                call.respond(HttpStatusCode.NotFound, "Product not found.")
            }
        }

        post {
            val request = call.receive<UploadProductRequest>()
            val newId = (transaction {
                ProductsTable.selectAll().mapNotNull { row -> row[ProductsTable.id].toIntOrNull() }.maxOrNull() ?: 0
            } + 1).toString()

            val endTime = if (request.isAuction) {
                System.currentTimeMillis() + (request.durationHours.toLong() * 3600L * 1000L)
            } else {
                0L
            }

            val newProduct = transaction {
                ProductsTable.insert {
                    it[id] = newId
                    it[title] = request.title
                    it[description] = request.description
                    it[price] = request.price
                    it[imageUrl] = request.category
                    it[supplierId] = request.supplierId
                    it[isApproved] = false
                    it[currentHighestBid] = 0.0
                    it[highestBidderName] = ""
                    it[auctionEndTimeMillis] = endTime
                }
                Product(
                    id = newId,
                    title = request.title,
                    description = request.description,
                    price = request.price,
                    imageUrl = request.category,
                    supplierId = request.supplierId,
                    isApproved = false,
                    currentHighestBid = 0.0,
                    highestBidderName = "",
                    auctionEndTimeMillis = endTime
                )
            }

            notifyProductUpdate()
            call.respond(HttpStatusCode.Created, newProduct)
        }

        post("/{id}/approve") {
            val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing product ID.")
            val success = transaction {
                ProductsTable.update({ ProductsTable.id eq id }) {
                    it[isApproved] = true
                } > 0
            }
            if (success) {
                notifyProductUpdate()
                call.respond(HttpStatusCode.OK, "Product approved.")
            } else {
                call.respond(HttpStatusCode.NotFound, "Product not found.")
            }
        }

        post("/{id}/reject") {
            val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing product ID.")
            val success = transaction {
                ProductsTable.deleteWhere { ProductsTable.id eq id } > 0
            }
            if (success) {
                notifyProductUpdate()
                call.respond(HttpStatusCode.OK, "Product rejected/deleted.")
            } else {
                call.respond(HttpStatusCode.NotFound, "Product not found.")
            }
        }

        post("/{id}/update") {
            val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing product ID.")
            val request = call.receive<UploadProductRequest>()
            val endTime = if (request.isAuction) {
                System.currentTimeMillis() + (request.durationHours.toLong() * 3600L * 1000L)
            } else {
                0L
            }
            val success = transaction {
                ProductsTable.update({ ProductsTable.id eq id }) {
                    it[title] = request.title
                    it[description] = request.description
                    it[price] = request.price
                    it[imageUrl] = request.category
                    it[isApproved] = false
                    it[auctionEndTimeMillis] = endTime
                } > 0
            }
            if (success) {
                notifyProductUpdate()
                call.respond(HttpStatusCode.OK, "Product updated.")
            } else {
                call.respond(HttpStatusCode.NotFound, "Product not found.")
            }
        }

        post("/{id}/bid") {
            val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing product ID.")
            val request = call.receive<BidRequest>()

            val product = transaction {
                ProductsTable.select { ProductsTable.id eq id }.map { row ->
                    Product(
                        id = row[ProductsTable.id],
                        title = row[ProductsTable.title],
                        description = row[ProductsTable.description],
                        price = row[ProductsTable.price],
                        imageUrl = row[ProductsTable.imageUrl],
                        supplierId = row[ProductsTable.supplierId],
                        isApproved = row[ProductsTable.isApproved],
                        currentHighestBid = row[ProductsTable.currentHighestBid],
                        highestBidderName = row[ProductsTable.highestBidderName],
                        auctionEndTimeMillis = row[ProductsTable.auctionEndTimeMillis]
                    )
                }.firstOrNull()
            } ?: return@post call.respond(HttpStatusCode.NotFound, "Product not found.")

            val minRequired = if (product.currentHighestBid > 0.0) product.currentHighestBid else product.price
            if (request.amount <= minRequired) {
                call.respond(HttpStatusCode.BadRequest, "Bid must be greater than $$minRequired")
                return@post
            }

            val success = transaction {
                ProductsTable.update({ ProductsTable.id eq id }) {
                    it[currentHighestBid] = request.amount
                    it[highestBidderName] = request.bidderName
                } > 0
            }

            if (success) {
                notifyProductUpdate()
                call.respond(HttpStatusCode.OK, "Bid placed successfully.")
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Failed to place bid.")
            }
        }

        post("/upload-image") {
            val multipart = call.receiveMultipart()
            var imageId = ""
            multipart.forEachPart { part ->
                if (part is PartData.FileItem) {
                    val fileBytes = part.streamProvider().readBytes()
                    val base64Content = Base64.getEncoder().encodeToString(fileBytes)
                    imageId = UUID.randomUUID().toString()
                    transaction {
                        ImagesTable.insert {
                            it[id] = imageId
                            it[content] = base64Content
                        }
                    }
                }
                part.dispose()
            }
            if (imageId.isNotEmpty()) {
                call.respond(HttpStatusCode.OK, mapOf("url" to "/api/images/$imageId"))
            } else {
                call.respond(HttpStatusCode.BadRequest, "No image file uploaded.")
            }
        }
    }

    get("/api/images/{id}") {
        val idParam = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing image ID.")
        val content = transaction {
            ImagesTable.select { ImagesTable.id eq idParam }
                .map { it[ImagesTable.content] }
                .firstOrNull()
        }
        if (content != null) {
            val bytes = Base64.getDecoder().decode(content)
            call.respondBytes(bytes, ContentType.Image.PNG)
        } else {
            call.respond(HttpStatusCode.NotFound, "Image not found.")
        }
    }
}
```


---

## Part 3: Steps to Run and Troubleshoot

### 1. Backend Setup
1. Open the backend project in IntelliJ IDEA.
2. Verify dependency resolution in `build.gradle.kts`.
3. Set environment variable `NEON_DB_URL` (optional) to connect to Neon PostgreSQL, or let it fall back to local SQLite automatically if not set.
4. Run the Ktor server by executing `com.tradepulse.ApplicationKt` or via `./gradlew run`.

### 2. Client Setup
1. Open the client project in Android Studio.
2. Sync Gradle dependencies.
3. If renaming has caused cache issues or launch failures like `Activity class {com.yeshuwahane.zeero/com.yeshuwahane.tradepulse.MainActivity} does not exist`, perform these steps:
   - Run `./gradlew clean` in terminal.
   - Click **File -> Invalidate Caches / Restart** in Android Studio.
   - Uninstall any older version of the app from your Android device/emulator.
   - Run the app again.
4. For iOS:
   - Run `./gradlew :shared:podInstall` if using CocoaPods, or build directly in Xcode by opening the `iosApp/iosApp.xcodeproj`.

### END OF MASTER PROMPT

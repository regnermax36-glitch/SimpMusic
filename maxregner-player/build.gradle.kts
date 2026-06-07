import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    android {
        namespace = "com.maxrave.maxregneplayer"
        compileSdk = 36
        minSdk = 26
        compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
    }
    jvm {
        compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.coroutines.android)
        }
        // Android uses MediaCodec + MediaExtractor + AudioTrack (pure Android SDK)
        androidMain.dependencies {
            implementation(libs.coroutines.android)
        }
        // Desktop uses javax.sound.sampled (pure JDK — no extra deps)
        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

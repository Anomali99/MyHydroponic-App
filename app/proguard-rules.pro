# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ===================================================================
# ATURAN UMUM
# ===================================================================

# Ini akan menyimpan nomor baris di stack trace, SANGAT PENTING
# untuk men-debug crash di versi rilis.
-keepattributes SourceFile,LineNumberTable

# Jika Anda menggunakan @Keep, ini memberi tahu R8 untuk mematuhinya
-keep class androidx.annotation.Keep

# ===================================================================
# 1. RETROFIT & GSON (WAJIB ANDA UBAH)
# ===================================================================
# Ini adalah penyebab crash #1.
# Ganti "com.proyekanda.data.model.**" dengan nama paket
# tempat Anda menyimpan semua data class (model) untuk respons API.

-keep class id.my.anomali99.myhydroponic.data.remote.api.dto.** { *; }

# Jika Anda menggunakan @SerializedName, aturan ini juga membantu
-keepattributes *Annotation*

# ===================================================================
# 2. HILT (DEPENDENCY INJECTION)
# ===================================================================
# Meskipun Hilt punya aturan sendiri, ini adalah aturan 'aman'
# untuk memastikan aplikasi dan @HiltAndroidApp Anda tidak dihapus.
# Ganti "com.proyekanda.MyApplication" dengan nama kelas Application Anda

-keep class id.my.anomali99.myhydroponic.MyHydroponicApp { *; }

# Aturan umum Hilt untuk menjaga kode yang dihasilkan
-keep class * implements dagger.hilt.internal.GeneratedEntryPoint { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Menjaga anotasi yang digunakan Hilt
-keep @dagger.hilt.android.HiltAndroidApp class *
-keep @dagger.hilt.DefineComponent class *
-keep @dagger.hilt.EntryPoint class *
-keep @dagger.hilt.InstallIn class *

# ===================================================================
# 3. JETPACK COMPOSE
# ===================================================================
# Aturan standar untuk memastikan Compose berjalan optimal di rilis

-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
-keep class * {
    @androidx.compose.runtime.Composable <fields>;
}
-keep class **.*$Composable*

# ===================================================================
# 4. ECLIPSE PAHO (MQTT)
# ===================================================================
# Paho adalah library Java lama dan butuh aturan 'keep'
# agar service dan callback-nya tidak dihapus.

-keep class org.eclipse.paho.client.mqttv3.** { *; }
-keep class org.eclipse.paho.android.service.** { *; }

-dontwarn org.eclipse.paho.client.mqttv3.**
-dontwarn org.eclipse.paho.android.service.**

# ===================================================================
# 5. FIREBASE MESSAGING (FCM)
# ===================================================================
# Menjaga service FCM Anda agar bisa menerima notifikasi
# Ganti "com.proyekanda.service.MyFirebaseMessagingService"
# dengan nama service FCM Anda.

-keep class id.my.anomali99.myhydroponic.data.remote.fcm.MyFirebaseMessagingService { *; }

# ===================================================================
# 6. KELAS PARCELABLE (OPSIONAL TAPI DISARANKAN)
# ===================================================================
# Jika Anda mengirim data class antar Activity/Composable
# menggunakan Parcelable.

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
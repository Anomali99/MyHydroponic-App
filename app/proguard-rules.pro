# Keep stack trace line numbers for debugging
-keepattributes SourceFile,LineNumberTable

# Respect the @Keep annotation
-keep class androidx.annotation.Keep

# Keep Data Transfer Objects
-keep class id.my.anomali99.myhydroponic.data.remote.api.dto.** { *; }

# Keep Data Models (Critical for MQTT and Gson)
-keep class id.my.anomali99.myhydroponic.data.model.** { *; }

# Keep Gson specific attributes
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes EnclosingMethod

# Keep Hilt Application and DI classes
-keep class id.my.anomali99.myhydroponic.MyHydroponicApp { *; }
-keep class id.my.anomali99.myhydroponic.di.** { *; }

# Keep Hilt generated components
-keep class * implements dagger.hilt.internal.GeneratedEntryPoint { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Keep Hilt annotations
-keep @dagger.hilt.android.HiltAndroidApp class *
-keep @dagger.hilt.DefineComponent class *
-keep @dagger.hilt.EntryPoint class *
-keep @dagger.hilt.InstallIn class *

# Keep Jetpack Compose composable functions
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
-keep class * {
    @androidx.compose.runtime.Composable <fields>;
}
-keep class **.*$Composable*

# Keep Paho MQTT library classes (Critical for dynamic reflection)
-keep class org.eclipse.paho.client.mqttv3.** { *; }
-keep class org.eclipse.paho.android.service.** { *; }
-dontwarn org.eclipse.paho.client.mqttv3.**
-dontwarn org.eclipse.paho.android.service.**

# Keep Firebase Messaging Service
-keep class id.my.anomali99.myhydroponic.data.remote.fcm.MyFirebaseMessagingService { *; }

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Suppress harmless warnings
-dontwarn okio.**
-dontwarn javax.annotation.**
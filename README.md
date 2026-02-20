# MyHydroponic App (Mobile App)

Repositori ini berisi kode sumber aplikasi Android (berbasis Kotlin) untuk sistem otomasi **MyHydroponic**. Aplikasi ini berfungsi sebagai antarmuka pengguna (UI) utama untuk memantau data sensor secara *real-time*, mengontrol aktuator (pompa), dan menerima notifikasi *push* (melalui Firebase).

Aplikasi ini berkomunikasi dengan *logic engine* (n8n) melalui REST API (Webhook) dan berkomunikasi dua arah secara *real-time* dengan *broker* MQTT menggunakan protokol WebSockets (`wss://`).


---

## 📋 Persyaratan Sistem (Prasyarat)
Sebelum membuka dan mem-build proyek ini, pastikan Anda memiliki:
1. **Android Studio** (versi terbaru direkomendasikan).
2. Android SDK dengan `minSdkVersion` yang sesuai dengan `build.gradle` proyek ini.
3. File `google-services.json` dari proyek Firebase Anda. File ini wajib ditambahkan untuk mengaktifkan fitur Firebase Cloud Messaging (Push Notification). Letakkan file tersebut di dalam folder `app/` dengan struktur letak seperti berikut:

   ```text
   MyHydroponic-App/
   ├── app/
   │   ├── src/
   │   ├── build.gradle
   │   └── google-services.json  <-- LETAKKAN DI SINI
   ├── gradle/
   ├── build.gradle
   └── settings.gradle
   ```
---

## ⚙️ Konfigurasi API dan MQTT

Sebelum melakukan *build* atau menjalankan aplikasi di emulator/perangkat asli, Anda wajib menyesuaikan URL server dan kredensial MQTT agar aplikasi dapat terhubung ke server MyHydroponic milik Anda.

Buka file konfigurasi pada *path* berikut:
`app/src/main/java/id/my/anomali99/myhydroponic/utils/Constants.kt`

Sesuaikan nilai di dalam `object Constants` dengan konfigurasi *server* Anda:

```kotlin
package id.my.anomali99.myhydroponic.utils

object Constants {
    // URL dasar untuk HTTP Request (mengarah ke n8n Webhooks)
    const val BASE_URL = "https://n8n.anomali99.my.id/webhook/"

    // Konfigurasi koneksi MQTT (menggunakan WebSockets / WSS)
    const val MQTT_URI = "wss://mqtt.anomali99.my.id"
    const val MQTT_USER = "mobile_user"
    const val MQTT_PASS = "7125"

    // Topik MQTT (Pub/Sub)
    const val ENV_DATA = "environment/data"
    const val ENV_REFRESH = "environment/refresh"
    const val PUMP_MANUALLY = "pump/manually"
    const val DEVICE_STATUS = "device/status"
}
```

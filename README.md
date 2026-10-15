# Categorysym Launcher 📱

> **"A mobile design philosophy inspired by the ancestors of this industry, prioritizing utility over decoration."**

Categorysym, modern arayüzlerin aşırı animasyon ve süsleme odaklı trendlerine meydan okuyan, konsept odaklı bir Android Launcher uygulamasıdır. İşlevsel bütünlüğe, kategorik düzenlemeye ve minimalist bir estetiğe odaklanarak akıllı telefonunuzu amacınızın gerçek bir uzantısı haline getirir.

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Platform: Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://android.com)
[![Language: Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![UI: Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg)](https://developer.android.com/jetpack/compose)

---

## 🏛 Manifesto

Süslü ve karmaşık arayüz dönüşümlerinin aksına Categorysym netliği savunur:

> *"Karmaşıklığı animasyonların arkasına saklamaz, net işlevleri aşırı yüklemez. En kişisel cihazınızı benliğinizin bir uzantısı olarak ele alarak, yaptığınız her şeyi geleceğin ihtiyaçlarıyla uyumlu hale getirir."*

---

## ✨ Tüm Öne Çıkan Özellikler ve Yenilikler

### 1. 🚀 Dinamik & Özel Kategori Sistemi (Categorization Engine)
- **20+ Fonksiyonel Kategori:** Uygulamaları *Communicator*, *Databank*, *X (Devices)*, *WWW*, *Market*, *Office*, *Uncategorized*, *System* gibi 8 varsayılan ve 12 isteğe bağlı kategoride otomatik sınıflandırır.
- **Özel Kategori Oluşturma (Custom Categories):** Kullanıcılar kendi kategorilerini özel İsim, Renk (Hex), Notlar ve Örnek Uygulamalar ile oluşturabilir, düzenleyebilir veya silebilir.
- **Akıllı Öğrenen Sıralama:** Kullanım sıklığına göre en çok kullanılan uygulamaları kategorilerin en başına getirir.
- **Manuel Kategori Taşıma:** Herhangi bir uygulamaya uzun basarak istenen kategoriye manuel taşıma ve tercihi kalıcı olarak kaydetme.
- **Kategori Açma/Satın Alma Slot Sistemi:** Ekstra kategori slotları yönetimi.

### 2. 🔍 Gelişmiş Arama & Otomatik Kaydırma (Smart Search & Auto-Scroll)
- **Otomatik Klavye Odaklanması:** Arama butonuna basıldığı anda arama çubuğuna odaklanılır ve klavye otomatik açılır.
- **Akıllı Kategoriye Kaydırma (Auto-Scroll):** Aranan uygulama mevcut ekranda görünmüyorsa, sistem uygulamanın bulunduğu kategoriye **otomatik olarak yumuşak bir şekilde kaydırma (scroll)** yapar.
- **Kararlı İmleç Yönetimi:** Arama çubuğunda harf yazılırken imleç her zaman yazılan metnin sağında (en sonunda) sabit kalır, ekran zıplamaları ve imleç kaymaları önlenmiştir.

### 3. 📦 Detaylı Uygulama Yönetimi & Kaldırma (App Context Sheet)
Uygulamalara uzun basıldığında açılan alt menü (`ModalBottomSheet`) üzerinden:
- **Uygulama Bilgileri (App Info):** Doğrudan Android sistem ayarlarındaki uygulama detayları sayfasını açar (`Settings.ACTION_APPLICATION_DETAILS_SETTINGS`).
- **Uygulamayı Kaldır (Uninstall App):** `REQUEST_DELETE_PACKAGES` izni ve `Intent.ACTION_DELETE` ile uygulamayı cihazdan güvenli şekilde silme diyaloğunu başlatır.
- **Uygulamayı Gizleme / Gizlemekten Çıkarma (Hide / Unhide App):** Hassas uygulamaları ana ekrandan gizler.

### 4. 🔒 Biyometrik Korumalı Gizli Bölüm (Secure Hidden Vault)
- **Gelişmiş Güvenlik:** Gizlenen uygulamalar özel bir biyometrik korumalı sayfada (`HiddenAppsScreen`) saklanır.
- **Çoklu Doğrulama:** Parmak izi, Yüz tanıma veya Cihaz PIN/Deseni (`BiometricPrompt` + `DEVICE_CREDENTIAL`) desteği.
- **Otomatik Kilitleme:** Gizli bölümden çıkıldığında veya farklı bir sayfaya kaydırıldığında gizli kasa otomatik olarak kilitlenir.

### 5. 📰 Entegre Keşfet / Haber Akışı (Discover Screen)
- Sol taraftaki ilk sayfada (Page 0) entegre WebView destekli Keşfet/Haber akışı.
- Donanım/Yazılım geri tuşu entegrasyonu (`BackHandler`) ile WebView içi gezinme desteği.

### 6. 🎨 Uyumlu Durum Çubuğu & Arayüz Desteği (Adaptive Edge-to-Edge)
- `SystemBarStyle.light` entegrasyonu ile cihaz ister Karanlık Mod ister Aydınlık Modda olsun, açık renkli zemin (`0xFFF7F7F9`) üzerinde okunaklı koyu renk durum çubuğu (status bar) ve navigasyon simgeleri.

### 7. 📑 Senkronize Sayfalama & Akıllı İndikatör (Pager & Page Indicators)
- Sayfalar arası dikey kaydırma pozisyonları birbiriyle senkronize tutulur.
- **Akıllı Sayfa Göstergesi:** Haber Akışı (Gazete ikonu) ➔ Ana Sayfalar (1, 2, 3...) ➔ Gizli Bölüm (Kilit ikonu).

---

## 🛠 Teknoloji Yığını (Tech Stack)

Categorysym en güncel Android geliştirme standartlarına uygun olarak inşa edilmiştir:

- **Programlama Dili:** %100 Kotlin
- **Kullanıcı Arayüzü:** Jetpack Compose (Declarative UI & Material 3)
- **Mimarisi:** MVVM (Model-View-ViewModel) + Clean Data Layer
- **Reaktif Veri Akışı:** Kotlin Coroutines & StateFlow
- **Yerel Depolama:** Jetpack DataStore Preferences (Asenkron Tercih Yönetimi)
- **Biyometrik Güvenlik:** AndroidX Biometric Library
- **Görsel Yükleme:** Coil (Compose Extension)
- **Bağımlılık Yönetimi:** Gradle Version Catalog (`libs.versions.toml`)
- **Hedef Sürüm:** Android 15 / 16 Desteği (`targetSdk = 36`, `compileSdk = 37`)

---

## 📜 Lisans

Bu proje **GNU General Public License v3.0 (GPLv3)** ile lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına göz atabilirsiniz.

---

## 👤 Geliştirici (Author)

**Hayri Taha ÇATAR**

- **LinkedIn:** [https://www.linkedin.com/in/hayritahacatar](https://www.linkedin.com/in/hayritahacatar)
- **GitHub:** [https://github.com/hayritahacatar](https://github.com/hayritahacatar)

---
*Created with the vision of purposeful and intentional design.*

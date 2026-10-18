# Categorysym Launcher 📱

> **"A mobile design philosophy inspired by the ancestors of this industry, prioritizing utility over decoration."**

Categorysym is an open-source, concept-driven Android launcher that challenges modern, overly decorative, animation-heavy interface trends. It focuses on functional integrity, smart categorization, and a minimalist aesthetic—treating your smartphone as a true extension of your intent.

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](LICENSE)
[![Platform: Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://android.com)
[![Language: Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![UI: Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg)](https://developer.android.com/jetpack/compose)
[![Localization: EN | TR](https://img.shields.io/badge/Localization-English%20%7C%20Turkish-orange.svg)](#-full-localization--multi-language-support)

---

## 🏛 The Manifesto

In an era of flashy overhauls and distracting UI elements, Categorysym stands for absolute clarity:

> *"It neither hides complexity behind animations nor overhauls unambiguous functions. By treating your most personal device as an extension of self, it harmonizes everything you do with the needs of the future."*

---

## ✨ Comprehensive Features (v1.0.0)

### 1. 🚀 Dynamic & Custom Categorization Engine
- **20+ Functional Categories:** Automatically categorizes installed applications into 8 default (*Communicator*, *Databank*, *X (Devices)*, *WWW*, *Market*, *Office*, *Uncategorized*, *System*) and 12 optional categories (*Social Media*, *Mass Media*, *Games*, *Entertainment*, *Finance*, *Reference*, *Books*, *Education*, *Business*, *Health*, *Bet*, *Productivity*).
- **Comprehensive App Matching:** Out-of-the-box automatic recognition for over 100+ popular applications across **US**, **UK**, and **Turkey (TR)** regions (e.g., GarantiBBVA, İşCep, YapıKredi, PayPal, Chase, Revolut, HBO Max, Disney+, Netflix, ChatGPT, Gemini, WhatsApp, Telegram, Instagram, TikTok, Trendyol, Amazon, eBay, Target, Walmart, etc.).
- **Custom Category Creation:** Create, edit, or delete custom categories with custom names, Hex colors, descriptions, and example application lists.
- **Adaptive Usage-Based Sorting:** Learns your workflow and automatically brings your most frequently used apps to the top of each category.
- **Manual App Re-assignment:** Long-press any application to move it to a different category and permanently store your preference.
- **Category Slot Management:** Expansion slots system for managing active categories.

---

### 2. 🌍 Full Localization & Multi-Language Support (EN US/UK & TR)
- **Modular i18n Architecture:** Built using native Android String Resources (`res/values/strings.xml` for English US/UK default, `res/values-tr/strings.xml` for Turkish).
- **Localized Category Display Names:** Automatically translates standard category titles depending on the device language setting while preserving internal data keys:
  - Communicator ➔ **İletişim**
  - Databank ➔ **Veri Bankası**
  - X (Devices) ➔ **X (Cihazlar)**
  - WWW ➔ **WWW (İnternet)**
  - Market ➔ **Market**
  - Office ➔ **Ofis**
  - Uncategorized ➔ **Kategorisiz**
  - System ➔ **Sistem**
  - Social Media ➔ **Sosyal Medya**
  - Mass Media ➔ **Geleneksel Medya**
  - Games ➔ **Oyunlar**
  - Entertainment ➔ **Eğlence**
  - Finance ➔ **Finans**
  - Reference ➔ **Referans**
  - Books ➔ **Kütüphane**
  - Education ➔ **Eğitim**
  - Business ➔ **İş & Kariyer**
  - Health ➔ **Sağlık**
  - Bet ➔ **Spor & Şans**
  - Productivity ➔ **Üretkenlik & YZ**

---

### 3. 🔍 Advanced App Search & Auto-Scroll Navigation
- **Automatic Keyboard Focus:** Tapping the search icon immediately focuses the input field and displays the soft keyboard (`FocusRequester` + `LocalSoftwareKeyboardController`).
- **Target Category Auto-Scroll:** When searching for an application that is not currently visible on the screen, the launcher **automatically performs a smooth scroll** directly to the target category.
- **Stable Cursor Management:** Prevents layout jumping or cursor resetting while typing—keeping the cursor reliably at the end of the text input (`TextFieldValue`).

---

### 4. 📦 Comprehensive App Context Management & Uninstallation
Long-pressing any application opens a bottom context sheet (`ModalBottomSheet`) offering:
- **App Info:** Direct access to system application settings (`Settings.ACTION_APPLICATION_DETAILS_SETTINGS`).
- **Uninstall App:** Directly triggers the system package uninstaller dialog via `REQUEST_DELETE_PACKAGES` permission and `Intent.ACTION_DELETE`.
- **Hide / Unhide App:** Easily move sensitive or infrequently used applications to the secure hidden vault.

---

### 5. 🔒 Biometric Vault (Hidden Apps)
- **Enhanced Privacy:** Hidden applications are isolated in a dedicated biometric vault (`HiddenAppsScreen`).
- **Multi-Factor Security:** Authenticate via Fingerprint, Face Recognition, or Device Credentials (PIN/Pattern) via `BiometricPrompt`.
- **Auto-Locking:** The hidden vault automatically re-locks whenever you navigate away or swipe to another page.

---

### 6. 📰 Integrated Discover & News Feed
- Built-in news feed on the left-most page (Page 0) powered by an embedded WebView (`DiscoverScreen`).
- Full hardware and software back button support (`BackHandler`) for seamless WebView history navigation.

---

### 7. 🎨 Adaptive Edge-to-Edge System Bar Legibility
- Enforces legible dark status bar and navigation bar icons via `SystemBarStyle.light` over the light background (`0xFFF7F7F9`), regardless of system-wide dark mode settings.

---

### 8. 📑 Synchronized Page Layout & Smart Page Indicator
- Synchronizes vertical scroll offsets across horizontal pages (`HorizontalPager` + `LazyListState`).
- **Smart Page Indicator:** Displays News Feed (Newspaper Icon) ➔ Launcher Pages (1, 2, 3...) ➔ Biometric Vault (Lock Icon).

---

## 🛠 Tech Stack & Architecture

Categorysym is built following modern Android engineering standards:

- **Language:** 100% Kotlin
- **UI Framework:** Jetpack Compose (Declarative UI & Material 3)
- **Architecture Pattern:** MVVM (Model-View-ViewModel) + Clean Data Layer
- **Reactive Data Flow:** Kotlin Coroutines & `StateFlow`
- **Data Persistence:** Jetpack DataStore Preferences (Asynchronous preference management)
- **Biometric Authentication:** AndroidX Biometric Library
- **Image Loading & Caching:** Coil (Compose Extension)
- **Dependency Management:** Gradle Version Catalog (`libs.versions.toml`)
- **SDK Target:** Target SDK 36 (Android 15 / 16 support, `compileSdk = 37`)

---

## 🏬 Google Play Store Listing Copy (Play Console Metinleri)

For Google Play Store submission, use the following pre-formatted text:

### 🇬🇧 English (US / UK)
- **Short Description (Maks 80 Chars):**
  > A concept-driven, minimalist Android launcher prioritizing utility over decoration.
- **Full Description:**
  > Categorysym is an open-source, concept-driven Android launcher that challenges modern, overly decorative interface trends. It focuses on functional integrity, smart categorization, and a minimalist aesthetic.
  >
  > ✨ KEY FEATURES:
  > - **Smart Categorization:** Automatically sorts your apps into 20+ functional categories (Communicator, Databank, Office, WWW, Market, Games, Finance, etc.).
  > - **Biometric Vault:** Secure sensitive applications behind device Fingerprint, Face ID, or PIN/Pattern.
  > - **Smart Search & Auto-Scroll:** Instant keyboard focus with automatic smooth scrolling directly to the category containing your search result.
  > - **Full App Management:** Direct access to App Info, Hide/Unhide options, and quick Uninstallation.
  > - **Localization:** Complete support for English (US/UK) and Turkish (TR).
  > - **News Feed:** Integrated Discover feed on the left page.
  > - **Privacy First:** 100% open-source with no data collection or tracking.

### 🇹🇷 Türkçe (TR)
- **Kısa Açıklama (Maks 80 Karakter):**
  > Süsten çok işlevselliğe öncelik veren konsept odaklı, minimalist Android başlatıcısı.
- **Tam Açıklama:**
  > Categorysym, modern arayüzlerin aşırı süsleme ve karmaşık animasyon trendlerine meydan okuyan, konsept odaklı açık kaynaklı bir Android Launcher uygulamasıdır.
  >
  > ✨ ÖNE ÇIKAN ÖZELLİKLER:
  > - **Akıllı Kategorileştirme:** Uygulamalarınızı otomatik olarak 20+ fonksiyonel kategoriye ayırır (İletişim, Veri Bankası, Ofis, İnternet, Market, Oyunlar, Finans vb.).
  > - **Biyometrik Gizli Kasa:** Hassas uygulamalarınızı Parmak İzi, Yüz Tanıma veya Cihaz PIN/Deseni ile koruyun.
  > - **Akıllı Arama & Otomatik Kaydırma:** Odaklanan klavye ile aradığınız uygulamanın bulunduğu kategoriye otomatik yumuşak kaydırma.
  > - **Uygulama Yönetimi:** Uygulama Bilgisi, Gizleme ve hızlı Uygulamayı Kaldır desteği.
  > - **Çoklu Dil Desteği:** Türkçe ve İngilizce (US/UK) dilleriyle tam uyum.
  > - **Haber Akışı:** Sol sayfada entegre Keşfet/Haber akışı.
  > - **Önce Gizlilik:** Hiçbir veri toplamayan, tamamen cihazda yerel çalışan açık kaynak mimari.

---

## 📜 License

This project is licensed under the **GNU General Public License v3.0 (GPLv3)** - see the [LICENSE](LICENSE) file for details. This ensures that the codebase remains free and open-source forever.

---

## 👤 Author

**Hayri Taha ÇATAR**

- **LinkedIn:** [https://www.linkedin.com/in/hayritahacatar](https://www.linkedin.com/in/hayritahacatar)
- **GitHub:** [https://github.com/hayritahacatar](https://github.com/hayritahacatar)

---
*Created with the vision of purposeful and intentional mobile design.*

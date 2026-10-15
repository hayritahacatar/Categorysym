# Categorysym Launcher 📱

> **"A mobile design philosophy inspired by the ancestors of this industry, prioritizing utility over decoration."**

Categorysym is a concept-driven Android launcher that challenges modern, overly decorative, animation-heavy interface trends. It focuses on functional integrity, efficient categorization, and a minimalist aesthetic—treating your smartphone as a true extension of your intent.

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Platform: Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://android.com)
[![Language: Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![UI: Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg)](https://developer.android.com/jetpack/compose)

---

## 🏛 The Manifesto

In an era of flashy overhauls and distracting UI elements, Categorysym stands for absolute clarity:

> *"It neither hides complexity behind animations nor overhauls unambiguous functions. By treating your most personal device as an extension of self, it harmonizes everything you do with the needs of the future."*

---

## ✨ Comprehensive Features

### 1. 🚀 Dynamic & Custom Categorization Engine
- **20+ Functional Categories:** Automatically sorts installed applications into 8 default (*Communicator*, *Databank*, *X (Devices)*, *WWW*, *Market*, *Office*, *Uncategorized*, *System*) and 12 optional categories.
- **Custom Category Creation:** Users can create, edit, or delete custom categories with tailored names, Hex colors, descriptions, and example application lists.
- **Adaptive Usage-Based Sorting:** Learns your workflow and automatically brings your most frequently used apps to the top of each category.
- **Manual App Re-assignment:** Long-press any application to move it to a different category and permanently store your preference.
- **Category Slot Management:** Expansion slots system for managing active categories.

### 2. 🔍 Advanced App Search & Auto-Scroll Navigation
- **Automatic Keyboard Focus:** Tapping the search icon immediately focuses the input field and displays the soft keyboard (`FocusRequester` + `LocalSoftwareKeyboardController`).
- **Target Category Auto-Scroll:** When searching for an application that is not currently visible on the screen, the launcher **automatically performs a smooth scroll** directly to the target category.
- **Stable Cursor Management:** Prevents layout jumping or cursor resetting while typing—keeping the cursor reliably at the end of the text input (`TextFieldValue`).

### 3. 📦 Comprehensive App Management & Uninstallation
Long-pressing any application opens a bottom context sheet (`ModalBottomSheet`) offering:
- **App Info:** Direct access to system application settings (`Settings.ACTION_APPLICATION_DETAILS_SETTINGS`).
- **Uninstall App:** Directly triggers the system package uninstaller dialog via `REQUEST_DELETE_PACKAGES` permission and `Intent.ACTION_DELETE`.
- **Hide / Unhide App:** Easily move sensitive or infrequently used applications to the secure hidden vault.

### 4. 🔒 Biometric Vault (Hidden Apps)
- **Enhanced Privacy:** Hidden applications are isolated in a dedicated biometric vault (`HiddenAppsScreen`).
- **Multi-Factor Security:** Authenticate via Fingerprint, Face Recognition, or Device Credentials (PIN/Pattern) via `BiometricPrompt`.
- **Auto-Locking:** The hidden vault automatically re-locks whenever you navigate away or swipe to another page.

### 5. 📰 Integrated Discover & News Feed
- Built-in news feed on the left-most page (Page 0) powered by an embedded WebView (`DiscoverScreen`).
- Full hardware and software back button support (`BackHandler`) for seamless WebView history navigation.

### 6. 🎨 Adaptive Edge-to-Edge System Bar Legibility
- Enforces legible dark status bar and navigation bar icons via `SystemBarStyle.light` over the light background (`0xFFF7F7F9`), regardless of system-wide dark mode settings.

### 7. 📑 Synchronized Page Layout & Smart Page Indicator
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

## 📜 License

This project is licensed under the **GNU General Public License v3.0 (GPLv3)** - see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Hayri Taha ÇATAR**

- **LinkedIn:** [https://www.linkedin.com/in/hayritahacatar](https://www.linkedin.com/in/hayritahacatar)
- **GitHub:** [https://github.com/hayritahacatar](https://github.com/hayritahacatar)

---
*Created with the vision of purposeful and intentional mobile design.*

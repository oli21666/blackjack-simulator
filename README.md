# ♠️ Gra Blackjack / Blackjack Android Game ♥️

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.24-blue.svg?logo=kotlin)
![Android](https://img.shields.io/badge/Android-SDK%2029%2B-green.svg?logo=android)
![Material3](https://img.shields.io/badge/Material%20Design-3-purple.svg)
![License](https://img.shields.io/badge/License-MIT-amber.svg)

[**Polski**](#-opis-projektu-po-polsku) | [**English**](#-project-description-in-english)

---

## 🇵🇱 Opis Projektu (po polsku)

**Gra Blackjack** to nowoczesna, w pełni interaktywna aplikacja na system Android symulująca klasyczną kasynową grę w 21 (Blackjack). Aplikacja oferuje realistyczną szatę graficzną sukna kasynowego, płynną obsługę zakładów za pomocą kolorowych żetonów, dynamiczne rozliczanie punktacji oraz animowaną turę krupiera.

### 🌟 Główne Funkcje

- 🎨 **Kasynowy Interfejs UI**: Elegancki stół w kolorze głębokiej zieleni ze złotymi akcentami, czytelnymi kartami oraz zaokrąglonymi wskaźnikami punktacji i salda.
- 🎰 **But Kasynowy (6 Talii / 312 Kart)**: Symulacja profesjonalnego podajnika kart (shoe) z 6 przetasowanymi taliami oraz automatyczną regułą *Cut Card* (automatyczne tasowanie po zużyciu ~80% kart).
- 🃏 **Elastyczna Punktacja Asów**: Automatyczne przeliczanie wartości Asa (11 lub 1 pkt) zapobiegające niesłusznej furze.
- 💰 **System Zakładów i Żetonów**:
  - Kolorowe żetony kasynowe: **+$10**, **+$25**, **+$100**, **+$500**.
  - Opcja czyszczenia zakładu oraz automatyczny restart salda do $1000 przy utracie środków.
- ⚡ **Pełne Zasady Kasynowe**:
  - **ROZDAJ** (Deal)
  - **DOBIERZ** (Hit)
  - **PASUJ** (Stand)
  - **PODWÓJ** (Double Down – podwojenie stawki i dobranie 1 karty)
  - Wypłata **3:2** za naturalnego Blackjacka, **1:1** za standardową wygraną, zwrot za remis (*Push*).
- 🎬 **Kinowe Tempo Krupiera**: Krupier odkrywa zakrytą kartę i dobiera karty z realistycznym opóźnieniem (700 ms per karta).
- ⚠️ **Odpowiedzialna Gra**: Stałe ostrzeżenie dotyczące symulacyjnego charakteru gry i odpowiedzialnego hazardu.

---

### 🛠️ Architektura i Technologia

- **Język**: Kotlin
- **Architektura**: Deterministyczny silnik stanu gry (`BlackjackGame`) zintegrowany z `MainActivity`
- **Interfejs UI**: `ConstraintLayout`, `MaterialCardView`, Custom Vector Drawables
- **Minimalny SDK**: Android 10.0 (API Level 29)
- **Kompilacyjny SDK**: Android 14 / Android 15 (API Level 35/37)

---

### 🚀 Jak Uruchomić Projekt

1. Sklonuj repozytorium:
   ```bash
   git clone https://github.com/twoj-login/gra_blackjack.git
   ```
2. Otwórz projekt w **Android Studio**.
3. Zsynchronizuj pliki Gradle (`Gradle Sync`).
4. Uruchom aplikację na emulatorze lub fizycznym urządzeniu z systemem Android:
   ```bash
   ./gradlew assembleDebug
   ```

---

<br/>

## 🇬🇧 Project Description (in English)

**Blackjack Game** is a modern, fully interactive Android application simulating a classic casino Blackjack experience. The app features a realistic casino felt design, intuitive chip betting, dynamic card score calculations, and animated dealer card draws.

### 🌟 Key Features

- 🎨 **Casino Table UI**: Elegant deep-green felt background with gold accents, crisp playing cards, and rounded score/balance badges.
- 🎰 **6-Deck Shoe Simulation (312 Cards)**: Simulates a professional 6-deck casino shoe using the Fisher-Yates shuffle algorithm and the *Cut Card* rule (~80% shoe penetration reshuffle).
- 🃏 **Dynamic Ace Valuation**: Automatic Ace score reduction (11 to 1 pt) to prevent accidental busts.
- 💰 **Chip Betting System**:
  - Colorful casino chips: **+$10**, **+$25**, **+$100**, **+$500**.
  - Clear bet option and auto-reload balance to $1000 when out of chips.
- ⚡ **Full Casino Blackjack Rules**:
  - **DEAL**
  - **HIT**
  - **STAND**
  - **DOUBLE DOWN** (Doubles current bet and draws exactly 1 card)
  - **3:2 Payout** for natural Blackjack, **1:1** for standard wins, push refunds.
- 🎬 **Cinematic Dealer Draw Loop**: Smooth 700 ms delayed card draws for realistic casino pacing.
- ⚠️ **Responsible Gaming**: Built-in 18+ gambling disclaimer highlighting virtual currency simulation.

---

### 🛠️ Architecture & Tech Stack

- **Language**: Kotlin
- **Architecture**: Deterministic Game Engine (`BlackjackGame`) integrated with `MainActivity`
- **UI Framework**: `ConstraintLayout`, `MaterialCardView`, Custom Vector Drawables
- **Min SDK**: Android 10.0 (API Level 29)
- **Target SDK**: Android 14 / Android 15 (API Level 35/37)

---

### 🚀 How to Build and Run

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/gra_blackjack.git
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle project files.
4. Build and run on an Android emulator or physical device:
   ```bash
   ./gradlew assembleDebug
   ```

---

## ⚖️ Responsible Gaming Disclaimer / Ostrzeżenie

> 🇵🇱 **PL**: *Gra ma charakter wyłącznie symulacyjny (wirtualna waluta). Hazard może uzależniać. Graj odpowiedzialnie (18+).*  
> 🇬🇧 **EN**: *This game is for simulation and entertainment purposes only (virtual currency). Gambling can be addictive. Play responsibly (18+).*

---

## 📜 License

Distributed under the MIT License. See `LICENSE` for more information.

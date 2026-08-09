# EchoPulse

**Daily energy pulse + focus sessions + micro-habits + evening reflection** for 2026.

EchoPulse helps you feel the rhythm of your day. Track a live Pulse Score driven by completed habits, focused minutes and whether you closed the day with a short reflection. Clean Material 3 design with full dark mode support.

## Features

- **Pulse Score** – Dynamic 0–100 score combining habits, focus time and reflection
- **Focus Timer** – 15 / 25 / 45 / 60 minute deep-work sessions with history
- **Micro Habits** – Lightweight habit tracker with streak and best-streak tracking
- **Evening Reflect** – Guided energy + mood + free-text journal entry
- **Home Dashboard** – Instant overview of today’s pulse, focus minutes and habits
- **Room persistence** – All data survives restarts
- **Material 3 + Dark mode** – Polished, system-aware theming

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + StateFlow
- Room (SQLite)
- Single-module project
- minSdk 26 · targetSdk / compileSdk 35

## Build

```bash
cd apps/2026-08-09-EchoPulse
chmod +x gradlew
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

## Package

`com.dailyapps.echopulse`

Created as part of the Daily Pro Productivity Apps series.

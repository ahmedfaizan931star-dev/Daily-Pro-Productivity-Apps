# MomentumVault

**Focus sessions + habit streaks + energy/mood reflection** that power a live Momentum Score (0–100).

MomentumVault helps you protect deep work, build consistent micro-habits, and close every day with a short reflection. A single momentum number keeps you honest and motivated. Clean Material 3 design with full dark-mode support.

## Features

- **Momentum Score** – Dynamic 0–100 score from habits (40%), focused minutes (40%) and daily reflection (20%)
- **Deep Focus Timer** – 15 / 25 / 45 / 60 minute sessions with automatic logging
- **Habit Tracker** – Create habits, toggle completion, track current & best streaks
- **Daily Reflect** – Energy + mood (1–5) + free-text journal
- **Home Dashboard** – Instant overview of today’s momentum, focus minutes and habits
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
cd apps/2026-08-10-MomentumVault
chmod +x gradlew
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

## Package

`com.dailyapps.momentumvault`

Created as part of the Daily Pro Productivity Apps series on 2026-08-10.

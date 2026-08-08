# ClarityMatrix

**Professional Eisenhower Matrix prioritizer + Focus Timer** for 2026.

Organize tasks into the classic four quadrants (Do First / Schedule / Delegate / Eliminate), run focused work sessions, and review daily insights — all in a clean Material 3 experience with full dark mode support.

## Features

- **Eisenhower Matrix** – Visual quadrants with color-coded tasks
- **Smart task management** – Add, complete, delete, and move tasks between quadrants
- **Integrated Focus Timer** – Start a Pomodoro-style session directly from any task (15 / 25 / 45 / 60 min)
- **Daily Insights** – Active count, completed today, distribution by quadrant, recent history
- **Room persistence** – All data survives app restarts
- **Material 3 + Dark mode** – Polished UI that follows system theme

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
cd apps/2026-08-08-ClarityMatrix
chmod +x gradlew
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

## Package

`com.dailyapps.claritymatrix`

Created as part of the Daily Pro Productivity Apps series.

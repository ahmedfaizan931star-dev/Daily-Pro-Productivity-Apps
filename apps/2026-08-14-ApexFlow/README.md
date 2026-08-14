# ApexFlow

**ApexFlow** is a professional-grade productivity Android app for 2026. It combines adaptive focus timers, priority-based task management, and clear daily insights so you can operate at peak performance.

## Features

- **Home Dashboard** — Today’s focus minutes, completed tasks, active workload, and weekly session count at a glance.
- **Focus Sessions** — Customizable Pomodoro-style timer (15 / 25 / 45 / 60 min) with start, pause, reset, and early-complete.
- **Priority Tasks** — Capture tasks with High / Medium / Low priority, check them off, and delete when done. Data persists locally via Room.
- **Insights** — Session history and summary stats for continuous improvement.
- **Modern UI** — Jetpack Compose + Material 3, full dark-mode support, clean navigation.

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + StateFlow
- Room (local persistence)
- Coroutines
- Min SDK 26, Target / Compile SDK 35

## Package

`com.dailyapps.apexflow`

## Build

```bash
cd apps/2026-08-14-ApexFlow
./gradlew assembleDebug
```

The debug APK will be at:

`app/build/outputs/apk/debug/app-debug.apk`

GitHub Actions workflow `build-apexflow.yml` automatically builds the APK on push to this folder.

## Architecture

- **Presentation**: Compose screens + ViewModel
- **Domain / Data**: Repository + Room DAOs / entities
- Single-module clean structure suitable for further expansion

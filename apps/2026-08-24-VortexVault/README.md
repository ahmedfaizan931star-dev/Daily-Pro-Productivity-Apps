# VortexVault

**VortexVault** is a professional productivity Android app for 2026. Capture your daily momentum with a beautiful Material 3 experience.

## Features

- **Home Dashboard** — Today’s overview: focus minutes, habit streaks, and priority tasks at a glance
- **Focus Timer** — Customizable Pomodoro-style sessions (25/5, 50/10, or custom) with session history
- **Habits** — Create habits, mark daily completions, and track multi-day streaks
- **Priority Tasks** — Simple task list with priority levels and completion tracking
- **Dark mode** fully supported via Material 3 dynamic theming
- **Offline-first** with Room persistence

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- ViewModel + StateFlow
- Navigation Compose
- Room
- Minimum SDK 26 / Target SDK 35

## Build

```bash
cd apps/2026-08-24-VortexVault
./gradlew assembleDebug
```

APK will be available under `app/build/outputs/apk/debug/`.

## Package

`com.dailyapps.vortexvault`

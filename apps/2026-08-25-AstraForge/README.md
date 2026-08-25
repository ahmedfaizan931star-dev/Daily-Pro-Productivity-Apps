# AstraForge

**AstraForge** is a professional-level productivity Android app for 2026. Align your deep work with stellar focus sessions, habit constellations, and daily mission logs — all offline-first with a polished Material 3 experience.

## Features

- **Home Dashboard** — Today’s overview: focus minutes, habit streaks, and priority missions at a glance
- **Focus Sessions** — Customizable deep-work timers (25/5, 50/10, or custom) with session history
- **Habit Constellations** — Create habits, mark daily completions, and track multi-day streaks
- **Mission Tasks** — Simple priority task list with completion tracking
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
cd apps/2026-08-25-AstraForge
./gradlew assembleDebug
```

APK will be available under `app/build/outputs/apk/debug/`.

## Package

`com.dailyapps.astraforge`

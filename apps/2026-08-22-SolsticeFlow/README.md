# SolsticeFlow

**Circadian Productivity & Energy Tracker** — 2026-08-22

A modern Android productivity app that helps you align deep work with your natural energy rhythm.

## Features

- **Home Dashboard** — Quick overview of focus minutes, current energy, and habit completion
- **Energy Check-in** — Log energy levels (1–5) with optional notes throughout the day
- **Focus Timer** — 25-minute Pomodoro-style sessions that record energy at start
- **Habit Tracker** — Create habits, mark daily completion, track streaks
- **Persistent storage** — Room database for all logs, sessions and habits
- **Material 3 + Dark mode** — Clean, polished UI with system theme support

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- ViewModel + StateFlow
- Navigation Compose
- Room
- Minimum SDK 26 / Target SDK 35

## Build

```bash
cd apps/2026-08-22-SolsticeFlow
./gradlew assembleDebug
```

Or use the GitHub Actions workflow `Build SolsticeFlow APK`.

## Package

`com.dailyapps.solsticeflow`

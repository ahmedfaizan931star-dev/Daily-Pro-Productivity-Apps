# CadenceCore

**Design and live your ideal daily cadence.**

CadenceCore is a modern Android productivity app that helps you build a sustainable daily rhythm by combining habits, focused work blocks, and reflective journaling in one polished experience.

## Features

- **Home Dashboard** — At-a-glance view of today’s habit completion, focus minutes, sessions, and mood.
- **Habits** — Create habits, log daily progress with one tap, and track streaks.
- **Focus Timer** — Configurable Pomodoro-style sessions (15 / 25 / 45 / 60 min) with automatic logging.
- **Reflect** — End-of-day mood + energy check-in and free-form journal that persists with Room.

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3 (dynamic color + dark mode)
- Navigation Compose
- ViewModel + StateFlow
- Room (habits, logs, focus sessions, reflections)
- Single-module project, minSdk 26, targetSdk 35

## Build

```bash
cd apps/2026-08-11-CadenceCore
chmod +x gradlew
./gradlew assembleDebug
```

The debug APK will be at:
`app/build/outputs/apk/debug/app-debug.apk`

## Package

`com.dailyapps.cadencecore`

Created for the Daily Pro Productivity Apps series — 2026-08-11.

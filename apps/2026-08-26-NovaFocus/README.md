# NovaFocus

**Modern focus timer + habit tracker + priority tasks** for 2026.

A polished single-module Android productivity app built with Kotlin, Jetpack Compose, Material 3, Room, and ViewModel + StateFlow.

## Features

- **Home Dashboard** — Daily overview of focus minutes, habit completion, and open tasks
- **Focus Timer** — Pomodoro-style sessions (15 / 25 / 45 / 60 min) with circular progress and auto-logging
- **Habits** — Create habits with emoji, check them off daily, and track streaks
- **Tasks** — Priority tasks (Low / Medium / High) with completion and persistence
- **Dark & Light theme** — Full Material 3 theming with system dark mode support
- **Local persistence** — Room database for habits, tasks, and focus sessions

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + StateFlow
- Room (KSP)
- DataStore-ready structure
- Min SDK 26, Target / Compile SDK 35

## Package

`com.dailyapps.novafocus`

## Build

```bash
cd apps/2026-08-26-NovaFocus
chmod +x gradlew
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

## GitHub Actions

A dedicated workflow (`.github/workflows/build-novafocus.yml`) builds the debug APK on every push to this app folder and uploads it as an artifact.

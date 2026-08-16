# OrbitMind

**Orbital Priority & Focus System** — A modern productivity app for 2026.

OrbitMind organizes your work into priority *orbits*:
- **Core** (innermost) — highest priority tasks that demand immediate attention
- **Mid** — important but flexible work
- **Outer** — lower urgency items

## Features

- **Priority Orbits** — Visual task system with 3 orbit levels
- **Focus Timer** — Configurable Pomodoro-style deep work sessions (15/25/45/60 min)
- **Habit Moons** — Streak-based habit tracking with daily completion
- **Constellation Insights** — Daily stats, focus history, and streak overview
- **Material 3 + Dark Mode** — Polished, adaptive UI
- **Room Persistence** — Offline-first local database

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + StateFlow
- Room + KSP
- Min SDK 26 / Target SDK 35

## Build

```bash
cd apps/2026-08-16-OrbitMind
chmod +x gradlew
./gradlew assembleDebug
```

APK will be at `app/build/outputs/apk/debug/`.

GitHub Actions workflow `build-orbitmind.yml` automatically builds on push to this folder.

## Package

`com.dailyapps.orbitmind`

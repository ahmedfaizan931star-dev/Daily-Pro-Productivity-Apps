# AetherForge

**Energy-aware daily productivity forge for 2026.**

AetherForge helps you prioritize tasks by energy level, run focused deep-work sessions, and reflect on your daily output so you can sustain high performance without burnout.

## Features

- **Dashboard** – Live energy score, today’s priorities, quick actions
- **Tasks** – Add/edit prioritized tasks with energy cost tags and completion tracking
- **Focus** – Configurable Pomodoro-style timer linked to current task
- **Insights** – Completion rates, streak tracking, weekly energy trends

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Single-module
- Min SDK 26 / Target & Compile SDK 35
- ViewModel + StateFlow
- Navigation Compose
- Room for persistence
- Clean presentation + data layers

## Build

```bash
cd apps/2026-08-15-AetherForge
./gradlew assembleDebug
```

APK will be available under `app/build/outputs/apk/debug/`.

Requires JDK 17+.

## Package

`com.dailyapps.aetherforge`

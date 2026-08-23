# NexusFlow

**Professional daily productivity app for 2026**

NexusFlow unifies focus sessions, habit building, and priority tasks into one clean, offline-first experience built with modern Android (Kotlin + Jetpack Compose + Material 3).

## Features

- **Home Dashboard** – Daily overview of focus minutes, habit completion, and task progress
- **Focus Timer** – Pomodoro-style sessions (15 / 25 / 45 / 60 min) with automatic logging
- **Habits** – Create habits, mark daily completion, track streaks
- **Tasks** – Priority-based task list (Low / Medium / High) with completion toggle
- **Dark mode** & dynamic color support
- **Fully offline** – Room database, no accounts or internet required

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + StateFlow
- Room (persistence)
- Single-module project
- minSdk 26, targetSdk 35

## Build

```bash
cd apps/2026-08-23-NexusFlow
./gradlew assembleDebug
```

APK will be at `app/build/outputs/apk/debug/`.

Or trigger the GitHub Action **Build NexusFlow APK** and download the artifact.

## Package

`com.dailyapps.nexusflow`

---
Created 2026-08-23 for Daily-Pro-Productivity-Apps

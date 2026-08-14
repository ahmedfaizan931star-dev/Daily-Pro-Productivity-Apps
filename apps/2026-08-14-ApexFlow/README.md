# ApexFlow

**Peak Productivity Companion for 2026**

ApexFlow is a polished, modern Android productivity app built with Kotlin and Jetpack Compose + Material 3. It helps you manage tasks by priority, run focused work sessions, and track daily progress.

## Features

- **Home Dashboard**: Quick overview of focus minutes, completed tasks, active tasks, and weekly sessions.
- **Tasks**: Create, prioritize (High / Medium / Low), complete, and delete tasks. Data persists with Room.
- **Focus Timer**: Configurable Pomodoro-style timer (15/25/45/60 min) with start/pause/reset and manual session completion.
- **Insights**: Daily stats and history of recent focus sessions.

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- ViewModel + StateFlow
- Navigation Compose
- Room Database
- Min SDK 26, Target SDK 35

## Build

```bash
cd apps/2026-08-14-ApexFlow
./gradlew assembleDebug
```

APK will be at `app/build/outputs/apk/debug/`.

GitHub Actions workflow automatically builds on push to this folder.

## Package

`com.dailyapps.apexflow`

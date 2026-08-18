# SynapseStack

**SynapseStack** is a modern, polished productivity Android app designed for deep work in 2026.

Stack your tasks, protect your focus with timed sessions, build habit streaks, and watch your daily momentum score grow.

## Features

- **Home Dashboard** – Momentum score, quick stats, overview of active tasks & habits
- **Deep Focus Timer** – Customizable 15/25/45/60-minute focus blocks with history
- **Task Stack** – Priority task list with complete / delete
- **Habit Tracker** – Simple streaks with one-tap completion
- **Insights** – Daily & all-time stats, progress visualization

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + StateFlow
- Room Database
- Single-module clean structure
- Min SDK 26 · Target / Compile SDK 35
- Dark / Light theme support

## Build

```bash
cd apps/2026-08-18-SynapseStack
./gradlew assembleDebug
```

The debug APK will be at:
`app/build/outputs/apk/debug/app-debug.apk`

## Package

`com.dailyapps.synapsestack`

---

Created as part of the Daily Pro Productivity Apps series.

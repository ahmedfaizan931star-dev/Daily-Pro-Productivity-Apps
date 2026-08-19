# CascadeFlow

**CascadeFlow** is a polished 2026 productivity Android app that turns your day into a priority waterfall.

Tasks cascade by priority (Critical → High → Medium → Low), energy requirements guide what you tackle next, timed focus blocks protect deep work, and habit streams keep consistency alive. A live Cascade Score shows how well your day is flowing.

## Features

- **Home Dashboard** – Cascade Score, quick stats, active priority preview, habit overview
- **Priority Cascade** – Add tasks with priority + energy level, complete or delete with one tap
- **Deep Focus Timer** – 15 / 25 / 45 / 60 min blocks with live countdown and session history
- **Habit Streams** – Create habits, toggle daily completion, track streaks
- **Material 3** – Full light / dark theme support, modern navigation

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + StateFlow
- Room (tasks, habits, focus sessions)
- Single-module clean structure
- Min SDK 26 · Target / Compile SDK 35

## Build

```bash
cd apps/2026-08-19-CascadeFlow
./gradlew assembleDebug
```

Debug APK location:
`app/build/outputs/apk/debug/app-debug.apk`

## Package

`com.dailyapps.cascadeflow`

---

Created as part of the Daily Pro Productivity Apps series.

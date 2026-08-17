# NestFlow

**NestFlow** is a modern productivity Android app that helps you organize life into nested goal “nests”, chain habits, run deep focus sessions, and track daily energy so productivity compounds naturally.

## Features

- **Home Dashboard** – Energy level logging, today’s focus minutes, open tasks overview, and active nests.
- **Nests** – Create focused life/work areas. Attach habits (with streaks) and tasks (with priority) inside each nest.
- **Focus Timer** – 15 / 25 / 45 / 60-minute deep-work sessions with automatic logging.
- **Insights & Reflection** – Total focus stats, best streaks, completed tasks, and daily mood + journal entries.

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- ViewModel + StateFlow
- Navigation Compose
- Room (local persistence)
- Single-module, minSdk 26, targetSdk 35

## Build

```bash
cd apps/2026-08-17-NestFlow
./gradlew assembleDebug
```

Or open the folder in Android Studio and run the `app` configuration.

## Package

`com.dailyapps.nestflow`

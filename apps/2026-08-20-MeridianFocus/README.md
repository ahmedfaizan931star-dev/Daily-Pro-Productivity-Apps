# MeridianFocus

**Align your day with your personal energy meridians.**

MeridianFocus is a modern Android productivity app (2026) that helps you schedule deep work, habits, and tasks around your natural peak energy windows (Morning / Afternoon / Evening / Night).

## Features

- **Home Dashboard** — Greeting + current meridian hint, today’s focus minutes, habit progress, and priority task list
- **Focus Timer** — Customizable Pomodoro (15 / 25 / 50 min) with circular progress, pause/resume, and session tracking
- **Habits** — Create habits, mark them done, and maintain streaks
- **Insights / Reflection** — Log peak energy window, mood (1–5), free-form notes, and review today’s sessions
- **Material 3** — Light & dark theme support, polished cards and navigation

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + StateFlow
- Room (persistence)
- Single-module project
- minSdk 26 · target/compileSdk 35

## Build

```bash
cd apps/2026-08-20-MeridianFocus
./gradlew assembleDebug
```

Or open the folder in Android Studio and run the `app` configuration.

## Package

`com.dailyapps.meridianfocus`

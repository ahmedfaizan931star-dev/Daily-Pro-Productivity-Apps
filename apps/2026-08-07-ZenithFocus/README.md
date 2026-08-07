# ZenithFocus

**Energy-aligned prioritization + adaptive focus sessions + daily reflection**

A polished, modern productivity Android app built for 2026. ZenithFocus helps you match tasks to your energy levels, apply the Eisenhower Matrix for clear prioritization, run deep-work focus sessions, and close each day with meaningful reflection.

## Features

- **Dashboard** – Instant overview of today's focus minutes, completed tasks, open priorities, and energy check-ins
- **Task Matrix** – Full Eisenhower Matrix (Do First / Schedule / Delegate / Eliminate) with energy tags (High / Medium / Low)
- **Focus Timer** – Pomodoro (25m), Deep Work (50m), and Flow (90m) modes with beautiful circular progress and session logging
- **Daily Reflection** – Wins, challenges, gratitude, and tomorrow's intention – stored locally
- **Energy Tracking** – Log morning / afternoon / evening energy to align future work
- **Material 3 + Dark mode** – Clean, modern UI that respects system theme
- **Local persistence** – Room database for tasks, focus sessions, and reflections

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + StateFlow
- Room
- Minimum SDK 26 / Target SDK 35

## Package

`com.dailyapps.zenithfocus`

## Build

```bash
# From this directory
./gradlew assembleDebug
```

If `gradlew` is missing or the wrapper jar is not present, generate it:

```bash
gradle wrapper --gradle-version 8.7
./gradlew assembleDebug
```

Or open the project in Android Studio (Hedgehog or newer) and run.

## Screens

1. **Home** – Stats, energy buttons, quick actions, today's open priorities
2. **Tasks** – Full matrix with filters and add-task dialog
3. **Focus** – Full-screen timer with mode selection and controls
4. **Reflect** – Structured evening journal

---

Generated as part of the Daily Pro Productivity Apps series.
